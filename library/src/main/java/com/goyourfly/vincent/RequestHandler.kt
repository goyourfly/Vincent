package com.goyourfly.vincent

import android.net.Uri
import com.goyourfly.vincent.decoder.BitmapDecoder
import com.goyourfly.vincent.decoder.DecodeManager

/**
 * Created by gaoyufei on 2017/5/31.
 */
open class RequestHandler<T>(){
    interface RequestListener<T>{

        fun onSuccess(t:T)

        fun onError(e:Exception)

    }

    open fun fetchSync(key:String,uri:Uri):T? = null

}
