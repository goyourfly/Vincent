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
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.TimeUnit

/**
 * Created by gaoyufei on 2017/5/31.
 */
class HttpRequestHandler(val fileCacheManager: CacheManager<File>):RequestHandler<File>() {
    val TIMEOUT = 10 * 1000
    @Throws(IOException::class,NetworkErrorException::class)
    override fun fetchSync(key:String,uri: Uri): File? {
        "HttpRequestHandlerStart:${uri.toString()}".logD()

        val url = URL(uri.toString())
        val con:HttpURLConnection = url.openConnection() as HttpURLConnection
        con.connectTimeout = TIMEOUT
        con.requestMethod = "GET"
        if(con.responseCode != HttpURLConnection.HTTP_OK){
            fileCacheManager.delete(key)
            return null
        }

        var inputStream:InputStream? = null
        var outputStream:OutputStream? = null
        try {
            inputStream = con.getInputStream()
            val bs = ByteArray(1024)

            fileCacheManager.delete(key)
            val file = fileCacheManager.get(key)
            fileCacheManager.set(key,file)

            outputStream = FileOutputStream(file)
            while(true){
                val len = inputStream.read(bs)
                if(len == -1){
                    break
                }
                outputStream.write(bs,0,len)
            }
            outputStream.close()
            inputStream.close()
            "HttpRequestHandlerResponse:${uri.toString()},file:${file.absolutePath}".logD()
            if(file.length() < 10L){
                fileCacheManager.delete(key)
                return null
            }
            return file
        }catch (e:IOException){
            fileCacheManager.delete(key)
            outputStream?.close()
            inputStream?.close()
            return null
        }
    }
}
