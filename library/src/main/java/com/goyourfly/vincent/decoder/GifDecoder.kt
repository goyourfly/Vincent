package com.goyourfly.vincent.decoder

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import com.goyourfly.vincent.common.getImgType
import com.goyourfly.vincent.common.logD
import java.io.File
import java.io.FileInputStream

/**
 * Created by gaoyufei on 2017/6/3.
 */

class GifDecoder: Decoder {
    val TYPE = "image/gif"
    override fun canDecode(file: File): Boolean {
        return file.getImgType() == TYPE
    }



    override fun decode(file: File,width:Int,height:Int): Drawable? {
        val input = FileInputStream(file)
        val gifDecodeHelper = GifDecodeHelper()
        gifDecodeHelper.read(input, file.length().toInt())
        return GifDrawable(gifDecodeHelper)
    }

}
