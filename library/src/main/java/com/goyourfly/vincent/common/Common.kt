package com.goyourfly.vincent.common

import android.util.Log
import android.os.Build
import android.graphics.Bitmap
import java.io.File
import android.R.attr.path
import android.graphics.BitmapFactory




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

fun File.getImgType():String?{
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeFile(absolutePath, options)
    val type = options.outMimeType
    "Type:$type".logD()
    return type
}