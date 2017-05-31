package com.goyourfly.vincent

import android.graphics.Bitmap

/**
 * Created by gaoyufei on 2017/5/31.
 */
interface Target{

    fun onComplete(bitmap: Bitmap)

    fun onError(e:Exception)
}
