package com.goyourfly.vincent.handle

import android.net.Uri
import com.goyourfly.vincent.decoder.BitmapDecoder
import com.goyourfly.vincent.decoder.DecodeManager

/**
 * Created by gaoyufei on 2017/5/31.
 * Base class for handle the uri
 */
open class RequestHandler<T>(){

    open fun canHandle(uri:Uri) = false

    open fun fetchSync(key:String,uri: Uri):T? = null

}
