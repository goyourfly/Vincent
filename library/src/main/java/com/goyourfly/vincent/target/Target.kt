package com.goyourfly.vincent.target

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import java.util.*

/**
 * Created by gaoyufei on 2017/5/31.
 */
open class Target{

    protected var targetId = UUID.randomUUID().toString()

    fun getId() = targetId

    open fun onComplete(drawable: Drawable){}

    open fun onError(e:Exception){}

    override fun toString(): String {
        return targetId
    }

    override fun equals(other: Any?): Boolean {
        if(other == null)
            return false
        if(other is Target){
            return targetId == other.getId()
        }
        return false
    }
}
