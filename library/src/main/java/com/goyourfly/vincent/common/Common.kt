package com.goyourfly.vincent.common

import android.util.Log

/**
 * Created by gaoyufei on 2017/5/31.
 */
val isDebug = true

fun String.logD(tag: String) {
    if (isDebug)
        Log.d(tag, this)
}