package com.goyourfly.vincent.scale

import android.graphics.Bitmap

/**
 * Created by gaoyufei on 2017/6/20.
 */

abstract class ScaleType {
    companion object{
        @JvmStatic
        val CENTER_CROP = CenterCrop()
    }
    /**
     * 将普通的Bitmap裁剪成目标尺寸
     */
    abstract fun scale(bitmap: Bitmap, expectWidth: Int, expectHeight: Int): Bitmap
}
