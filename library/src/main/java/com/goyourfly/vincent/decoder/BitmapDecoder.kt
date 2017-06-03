package com.goyourfly.vincent.decoder

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import java.io.File

/**
 * Created by gaoyufei on 2017/6/1.
 */

class BitmapDecoder : Decoder {

    override fun canDecode(file: File): Boolean {
        return true
    }

    override fun decode(file: File): Drawable? {
        return BitmapDrawable(BitmapFactory.decodeFile(file.path))
    }

}