package com.goyourfly.vincent.common

import android.util.Log
import android.os.Build
import android.graphics.Bitmap
import java.io.File
import android.R.attr.path
import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.content.pm.ApplicationInfo.FLAG_LARGE_HEAP
import android.graphics.BitmapFactory


/**
 * Created by gaoyufei on 2017/5/31.
 */
val isDebug = true

fun String.logD(tag: String) {
    if (isDebug)
        Log.d(tag, this)
}

fun String.logD() {
    if (isDebug)
        Log.d("${Thread.currentThread().name}:------------>", this)
}

fun String.logE() {
    if (isDebug)
        Log.e("------------>", this)
}


fun Bitmap.byteSize(): Int {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        return allocationByteCount
    } else {
        return byteCount
    }
}

fun File.getImgType(): String? {
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeFile(absolutePath, options)
    val type = options.outMimeType
    "Type:$type".logD()
    return type
}

fun Bitmap.centerCrop(width: Int, height: Int): Bitmap {
    val bitmap = this
    // if width and height neither zero, resize the bitmap
    val bitmapNewWidth = bitmap.width
    val bitmapNewHeight = bitmap.height

    val scaleRate = bitmapNewWidth.toFloat() / bitmapNewHeight.toFloat()
    val scaleX = bitmapNewWidth.toFloat() / width.toFloat()
    val scaleY = bitmapNewHeight.toFloat() / height.toFloat()

    var scaleBitmapWidth = 0
    var scaleBitmapHeight = 0
    if (scaleX > scaleY) {
        scaleBitmapHeight = height
        scaleBitmapWidth = (height * scaleRate).toInt()
    } else {
        scaleBitmapWidth = width
        scaleBitmapHeight = (width / scaleRate).toInt()
    }
    "Decode:ScaleBitmapSize:$scaleBitmapWidth,$scaleBitmapHeight".logD()
    val scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaleBitmapWidth, scaleBitmapHeight, false)

    if (width != 0 && height != 0) {
        val newBitmap = Bitmap.createBitmap(scaledBitmap, (scaleBitmapWidth - width) / 2, (scaleBitmapHeight - height) / 2, width, height)
        return newBitmap
    }
    return scaledBitmap
}


fun calculateMemoryCacheSize(context: Context): Int {
    val am = getService<ActivityManager>(context, ACTIVITY_SERVICE)
    val largeHeap = context.applicationInfo.flags and FLAG_LARGE_HEAP != 0
    val memoryClass = if (largeHeap) am.getLargeMemoryClass() else am.getMemoryClass()
    // Target ~15% of the available heap.
    return (1024L * 1024L * memoryClass.toLong() / 7).toInt()
}


fun <T> getService(context: Context, service: String): T {
    return context.getSystemService(service) as T
}