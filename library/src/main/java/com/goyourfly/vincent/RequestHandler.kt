package com.goyourfly.vincent

import android.net.Uri
import com.goyourfly.vincent.decoder.BitmapDecoder
import com.goyourfly.vincent.decoder.DecodeManager

/**
 * Created by gaoyufei on 2017/5/31.
 */
open class RequestHandler<T>(val decoder: DecodeManager){
    interface RequestListener<T>{

        fun onSuccess(t:T)

        fun onError(e:Exception)

    }

    open fun fetchSync(uri:Uri):T? = null

    open fun fetchAsync(uri: Uri, listener:RequestListener<T>){}
}
