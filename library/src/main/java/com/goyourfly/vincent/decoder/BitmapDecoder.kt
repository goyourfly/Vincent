package com.goyourfly.vincent.decoder

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import com.goyourfly.vincent.common.getImgType
import java.io.File

/**
 * Created by gaoyufei on 2017/6/1.
 */

class BitmapDecoder : Decoder {
    val TYPE_JPEG = "image/jpeg"
    val TYPE_PNG = "image/png"
    val TYPE_BMP = "image/bmp"
    val TYPE_WEBP = "image/webp"
    override fun canDecode(file: File): Boolean {
        return when(file.getImgType()){
            TYPE_JPEG -> true
            TYPE_PNG -> true
            TYPE_WEBP -> true
            TYPE_BMP -> true
            else -> false
        }
    }

    override fun decode(file: File): Drawable? {
        return BitmapDrawable(BitmapFactory.decodeFile(file.path))
    }

}