package com.goyourfly.vincent

import android.accounts.NetworkErrorException
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.goyourfly.vincent.common.logD
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

/**
 * Created by gaoyufei on 2017/5/31.
 */
class OkHttpRequestHandler:RequestHandler<Bitmap> {
    val client = OkHttpClient()
    override fun fetchAsync(uri: Uri, listener: RequestHandler.RequestListener<Bitmap>) {
    }

    override fun fetchSync(uri: Uri): Bitmap {
        "RequestStart:${uri.toString()}".logD("OKHTTP")
        val request = Request.Builder()
                .url(uri.toString())
                .build()
        val response = client.newCall(request).execute()
        val body = response.body()
        "RequestResponse:${body.toString()}".logD("OKHTTP")
        if (!response.isSuccessful()) {
            body?.close();
            throw NetworkErrorException(""+response.code());
        }
        return BitmapFactory.decodeByteArray(body!!.bytes(),0,body.bytes().size)
    }
}
