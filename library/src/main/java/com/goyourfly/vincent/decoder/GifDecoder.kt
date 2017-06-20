package com.goyourfly.vincent.decoder

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.goyourfly.vincent.common.getImgType
import com.goyourfly.vincent.common.logD
import com.goyourfly.vincent.scale.ScaleType
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

/**
 * Created by gaoyufei on 2017/6/3.
 */

class GifDecoder : Decoder {
    val TYPE = "image/gif"
    override fun canDecode(streamProvider: StreamProvider): Boolean {
        val bitmapOption = BitmapFactory.Options()
        bitmapOption.inJustDecodeBounds = true
        BitmapFactory.decodeStream(streamProvider.getInputStream(), null, bitmapOption)
        return bitmapOption.outMimeType == TYPE
    }


    override fun decode(context: Context,
                        streamProvider: StreamProvider,
                        scaleType: ScaleType,
                        fit:Boolean,
                        width: Int, height: Int): Drawable? {
        val gifDecodeHelper = GifDecodeHelper()
        gifDecodeHelper.read(streamProvider.getInputStream(), -1)
        return GifDrawable(gifDecodeHelper)
    }

}
