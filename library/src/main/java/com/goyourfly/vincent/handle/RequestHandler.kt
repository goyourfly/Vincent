package com.goyourfly.vincent.handle

import android.net.Uri

/**
 * Created by gaoyufei on 2017/5/31.
 * Base class for handle the uri
 */
interface RequestHandler<T> {

    fun canHandle(uri: Uri) = false

    fun fetchSync(key: String, uri: Uri): T

}
