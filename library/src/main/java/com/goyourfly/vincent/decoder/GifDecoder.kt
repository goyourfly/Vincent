package com.goyourfly.vincent.decoder

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import com.goyourfly.vincent.common.logD
import java.io.File
import java.io.FileInputStream

/**
 * Created by gaoyufei on 2017/6/3.
 */

class GifDecoder: Decoder {

    override fun canDecode(file: File): Boolean {
        return true
    }

    override fun decode(file: File): Drawable? {
        val input = FileInputStream(file)
        val gifDecodeHelper = GifDecodeHelper()
        gifDecodeHelper.read(input, file.length().toInt())
        return GifDrawable(gifDecodeHelper)
    }

}
