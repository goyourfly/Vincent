package com.goyourfly.vincent.cache

import android.graphics.Bitmap
import com.goyourfly.vincent.RequestInfo

/**
 * Created by gaoyufei on 2017/5/31.
 */
data class CacheSeed(val key:String, val info: RequestInfo, val value: Bitmap)
