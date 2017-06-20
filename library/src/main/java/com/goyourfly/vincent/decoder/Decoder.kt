package com.goyourfly.vincent.decoder

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.goyourfly.vincent.scale.ScaleType
import java.io.File
import java.io.InputStream

/**
 * Created by gaoyufei on 2017/6/1.
 */

interface Decoder {


    /**
     * 如果宽高都为0，则不缩放图片
     * 如果其中一个为0，则按比例缩放
     * 如果都不为0，裁剪
     */
    fun decode(context: Context,streamProvider: StreamProvider,scaleType: ScaleType,width:Int,height:Int):Drawable?

    fun canDecode(streamProvider: StreamProvider):Boolean
}
