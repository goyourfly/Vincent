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

fun String.logD(){
    if(isDebug)
        Log.d("------------>",this)
}

fun String.logE(){
    if(isDebug)
        Log.e("------------>",this)
}