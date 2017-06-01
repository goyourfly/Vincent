package com.goyourfly.vincent.decoder

import android.graphics.Bitmap
import java.io.InputStream

/**
 * Created by gaoyufei on 2017/6/1.
 */

interface BitmapDecoder {

    fun decode(stream: InputStream):Bitmap?

    fun decode(bytes: ByteArray):Bitmap?

    fun canDecode():Boolean
}