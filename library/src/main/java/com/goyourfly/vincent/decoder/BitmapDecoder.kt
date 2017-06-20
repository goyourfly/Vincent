package com.goyourfly.vincent.decoder

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import com.goyourfly.vincent.common.logD
import com.goyourfly.vincent.scale.ScaleType


/**
 * Created by gaoyufei on 2017/6/1.
 */

class BitmapDecoder : Decoder {
    val TYPE_JPEG = "image/jpeg"
    val TYPE_PNG = "image/png"
    val TYPE_BMP = "image/bmp"
    val TYPE_WEBP = "image/webp"
    override fun canDecode(streamProvider: StreamProvider): Boolean {
        val bitmapOption = BitmapFactory.Options()
        bitmapOption.inJustDecodeBounds = true
        BitmapFactory.decodeStream(streamProvider.getInputStream(),null,bitmapOption)
        "FileType:${bitmapOption.outMimeType}".logD()
        return when (bitmapOption.outMimeType) {
            TYPE_JPEG -> true
            TYPE_PNG -> true
            TYPE_WEBP -> true
            TYPE_BMP -> true
            else -> false
        }
    }

    override fun decode(context: Context,streamProvider: StreamProvider,scaleType: ScaleType, width: Int, height: Int): Drawable? {
        "Decode:size:$width,$height".logD()
        if (width == 0 && height == 0) {
            return BitmapDrawable(context.resources,BitmapFactory.decodeStream(streamProvider.getInputStream()))
        }
        val bitmap = scaleBitmap(streamProvider, width, height)
        return BitmapDrawable(context.resources,scaleType.scale(bitmap,width,height))
    }


    /**
     * This is my code
     */
    fun scaleBitmap(streamProvider: StreamProvider, width: Int, height: Int): Bitmap {
        // Read bitmap origin size
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeStream(streamProvider.getInputStream(),null, options)
        val bitmapWidth = options.outWidth
        val bitmapHeight = options.outHeight
        "Decode:BitmapSize:$bitmapWidth,$bitmapHeight".logD()
        // calculate new require size
        var reqWidth = width
        var reqHeight = height
        if (width == 0) {
            reqWidth = bitmapWidth * (height.toFloat() / bitmapHeight.toFloat()).toInt()
        } else if (height == 0) {
            reqHeight = bitmapHeight * (width.toFloat() / bitmapWidth.toFloat()).toInt()
        }
        options.inJustDecodeBounds = false
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
        // read the resized bitmap by set sample size
        val bitmap = BitmapFactory.decodeStream(streamProvider.getInputStream(),null, options)
        return bitmap
    }

    fun calculateInSampleSize(
            options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val halfHeight = height / 2
            val halfWidth = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }
}