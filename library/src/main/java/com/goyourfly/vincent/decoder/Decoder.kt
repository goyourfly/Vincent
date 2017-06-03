package com.goyourfly.vincent.decoder

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import java.io.File
import java.io.InputStream

/**
 * Created by gaoyufei on 2017/6/1.
 */

interface Decoder {


    fun decode(file:File):Drawable?

    fun canDecode(file: File):Boolean
}
