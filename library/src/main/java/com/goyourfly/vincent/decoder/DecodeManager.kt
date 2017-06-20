package com.goyourfly.vincent.decoder

import android.content.Context
import android.graphics.drawable.Drawable
import com.goyourfly.vincent.scale.ScaleType
import java.io.File
import java.io.InputStream

/**
 * Created by gaoyufei on 2017/6/1.
 */

object DecodeManager{
    val decoderList = arrayOf(BitmapDecoder(),GifDecoder())

    fun decode(context: Context, streamProvider: StreamProvider, scaleType: ScaleType, width:Int, height:Int):Drawable?{
        for (decoder in decoderList){
            if(decoder.canDecode(streamProvider)){
                return decoder.decode(context,streamProvider,scaleType,width,height)
            }
        }
        return null
    }
}
