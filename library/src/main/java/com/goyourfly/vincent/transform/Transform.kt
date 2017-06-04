package com.goyourfly.vincent.transform

import android.graphics.Bitmap

/**
 * Created by Administrator on 2017/6/4 0004.
 */
interface Transform{
    fun transform(bitmap: Bitmap):Bitmap
}