package com.goyourfly.vincent.decoder

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.InputStream

/**
 * Created by gaoyufei on 2017/6/1.
 */

class NormalBitmapDecoder:BitmapDecoder{

    override fun canDecode(file: File): Boolean {
        return true
    }

    override fun decode(file: File): Bitmap? {
        return BitmapFactory.decodeFile(file.path)
    }

}