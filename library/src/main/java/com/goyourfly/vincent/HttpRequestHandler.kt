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
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL
import java.util.concurrent.TimeUnit

/**
 * Created by gaoyufei on 2017/5/31.
 */
class HttpRequestHandler(val fileCacheManager: CacheManager<File>):RequestHandler<File>() {
    @Throws(IOException::class,NetworkErrorException::class)
    override fun fetchSync(key:String,uri: Uri): File? {
        "HttpRequestHandlerStart:${uri.toString()}".logD()
        fileCacheManager.delete(key)
        val file = fileCacheManager.get(key)
        fileCacheManager.set(key,file)

        val url = URL(uri.toString())
        val con = url.openConnection()
        val inputStream = con.getInputStream()

        val bs = ByteArray(1024)
        val outputStream = FileOutputStream(file)
        var len = 0
        while(true){
            len = inputStream.read(bs)
            if(len == -1){
                break
            }
            outputStream.write(bs)
        }

        outputStream.close()
        inputStream.close()
        "HttpRequestHandlerResponse:${file.absolutePath}".logD()
        return file
    }
}
