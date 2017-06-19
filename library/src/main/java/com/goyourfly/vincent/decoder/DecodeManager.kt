package com.goyourfly.vincent.decoder

import android.graphics.drawable.Drawable
import java.io.File
import java.io.InputStream

/**
 * Created by gaoyufei on 2017/6/1.
 */

object DecodeManager{
    val decoderList = arrayOf(BitmapDecoder(),GifDecoder())

    fun decode(streamProvider: StreamProvider,width:Int,height:Int):Drawable?{
        for (decoder in decoderList){
            if(decoder.canDecode(streamProvider)){
                return decoder.decode(streamProvider,width,height)
            }
        }
        return null
    }
}
