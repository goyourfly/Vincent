package com.goyourfly.vincent.target

import android.graphics.Bitmap
import java.util.*

/**
 * Created by gaoyufei on 2017/5/31.
 */
open class Target{

    val targetId = UUID.randomUUID().toString()

    open fun onComplete(bitmap: Bitmap){}

    open fun onError(e:Exception){}

    override fun toString(): String {
        return targetId
    }
}
