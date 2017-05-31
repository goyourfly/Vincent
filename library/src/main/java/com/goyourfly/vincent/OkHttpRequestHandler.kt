package com.goyourfly.vincent

import android.graphics.Bitmap
import android.net.Uri

/**
 * Created by gaoyufei on 2017/5/31.
 */
class OkHttpRequestHandler:RequestHandler<Bitmap> {
    val client = OkHttpClient()
    override fun fetchAsync(uri: Uri, listener: RequestHandler.RequestListener<Bitmap>) {
    }

    override fun fetchSync(uri: Uri): Bitmap {
        val request = Requ
    }
}
