package com.goyourfly.vincent.decoder

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.InputStream

/**
 * Created by gaoyufei on 2017/6/1.
 */

class NormalBitmapDecoder:BitmapDecoder{
    override fun decode(stream: InputStream) :Bitmap?{
        return BitmapFactory.decodeStream(stream)
    }

    override fun decode(bytes: ByteArray) :Bitmap?{
        return BitmapFactory.decodeByteArray(bytes,0,bytes.size)
    }

    override fun canDecode(): Boolean {
        return true
    }

}