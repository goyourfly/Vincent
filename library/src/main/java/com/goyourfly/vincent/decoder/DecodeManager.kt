package com.goyourfly.vincent.decoder

import android.graphics.Bitmap
import java.io.File
import java.io.InputStream

/**
 * Created by gaoyufei on 2017/6/1.
 */

object DecodeManager{
    val decoderList = arrayOf(NormalBitmapDecoder())

    fun decode(stream: InputStream?):Bitmap?{
        if(stream == null
                || stream.available()!=0)
            return null
        for (decoder in decoderList){
            if(decoder.canDecode(stream)){
                return decoder.decode(stream)
            }
        }
        return null
    }

    fun decode(bytes: ByteArray):Bitmap?{
        for (decoder in decoderList){
            if(decoder.canDecode(bytes)){
                return decoder.decode(bytes)
            }
        }
        return null
    }

    fun decode(file:File):Bitmap?{
        for (decoder in decoderList){
            if(decoder.canDecode(file)){
                return decoder.decode(file)
            }
        }
        return null
    }
}
