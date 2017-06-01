package com.goyourfly.vincent

import android.accounts.NetworkErrorException
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.goyourfly.vincent.common.logD
import com.goyourfly.vincent.decoder.BitmapDecoder
import com.goyourfly.vincent.decoder.DecodeManager
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * Created by gaoyufei on 2017/5/31.
 */
class OkHttpRequestHandler():RequestHandler<Bitmap>() {

    val client = OkHttpClient.Builder()
            .connectTimeout(10,TimeUnit.SECONDS)
            .writeTimeout(10,TimeUnit.SECONDS)
            .readTimeout(30,TimeUnit.SECONDS)
            .build()

    override fun fetchAsync(uri: Uri, listener: RequestHandler.RequestListener<Bitmap>) {

    }

    @Throws(IOException::class,NetworkErrorException::class)
    override fun fetchSync(uri: Uri): Bitmap? {
        "RequestStart:${uri.toString()}".logD()
        val request = Request.Builder()
                .url(uri.toString())
                .build()
        val response = client.newCall(request).execute()
        "RequestMid:${uri.toString()}".logD()
        val bitmap = DecodeManager.decode(response.body()!!.byteStream())
        val body = response.body()
        if (!response.isSuccessful()) {
            body?.close();
            throw NetworkErrorException(""+response.code());
        }
        "RequestResponse:${bitmap.toString()}".logD()
        return bitmap
    }
}
