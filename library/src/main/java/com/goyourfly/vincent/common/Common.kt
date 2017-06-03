package com.goyourfly.vincent.common

import android.util.Log
import android.os.Build
import android.graphics.Bitmap



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
        Log.d("${Thread.currentThread().name}:------------>",this)
}

fun String.logE(){
    if(isDebug)
        Log.e("------------>",this)
}


fun Bitmap.byteSize(): Int {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        return allocationByteCount
    }  else {
        return rowBytes * height
    }
}