package com.goyourfly.vincent

import android.net.Uri

/**
 * Created by gaoyufei on 2017/5/31.
 */
interface RequestHandler<T>{
    interface RequestListener<T>{

        fun onSuccess(t:T)

        fun onError(e:Exception)

    }

    fun fetchSync(uri:Uri):T

    fun fetchAsync(uri: Uri,listener:RequestListener<T>)
}
