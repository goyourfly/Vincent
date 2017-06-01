package com.goyourfly.vincent

import android.accounts.NetworkErrorException
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.goyourfly.vincent.cache.CacheManager
import com.goyourfly.vincent.cache.FileCacheManager
import com.goyourfly.vincent.common.logD
import com.goyourfly.vincent.decoder.BitmapDecoder
import com.goyourfly.vincent.decoder.DecodeManager
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.Okio
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * Created by gaoyufei on 2017/5/31.
 */
class OkHttpRequestHandler(val fileCacheManager: CacheManager<File>):RequestHandler<File>() {

    val client = OkHttpClient.Builder()
            .connectTimeout(10,TimeUnit.SECONDS)
            .writeTimeout(10,TimeUnit.SECONDS)
            .readTimeout(30,TimeUnit.SECONDS)
            .build()


    @Throws(IOException::class,NetworkErrorException::class)
    override fun fetchSync(key:String,uri: Uri): File? {
        "RequestStart:${uri.toString()}".logD()
        val request = Request.Builder()
                .url(uri.toString())
                .build()
        fileCacheManager.delete(key)
        val file = fileCacheManager.get(key)
        fileCacheManager.set(key,file)
        val response = client.newCall(request).execute()
        val buffer = Okio.buffer(Okio.sink(file))
        buffer.writeAll(response.body()?.source())
        buffer.close()
        "RequestResponse:${file.absolutePath}".logD()
        return file
    }
}
