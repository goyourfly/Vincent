package com.goyourfly.vincent.decoder

import android.graphics.drawable.Drawable
import java.io.File

/**
 * Created by gaoyufei on 2017/6/1.
 */

object DecodeManager{
    val decoderList = arrayOf(GifDecoder(),BitmapDecoder())

    fun decode(file:File):Drawable?{
        for (decoder in decoderList){
            if(decoder.canDecode(file)){
                return decoder.decode(file)
            }
        }
        return null
    }
}
