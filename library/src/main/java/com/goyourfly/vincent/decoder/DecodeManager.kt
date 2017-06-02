package com.goyourfly.vincent.decoder

import android.graphics.Bitmap
import java.io.File
import java.io.InputStream

/**
 * Created by gaoyufei on 2017/6/1.
 */

object DecodeManager{
    val decoderList = arrayOf(NormalBitmapDecoder())

    fun decode(file:File):Bitmap?{
        for (decoder in decoderList){
            if(decoder.canDecode(file)){
                return decoder.decode(file)
            }
        }
        return null
    }
}
