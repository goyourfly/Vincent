package com.goyourfly.vincent.decoder

import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import com.goyourfly.vincent.common.getImgType
import com.goyourfly.vincent.common.logD
import java.io.File


/**
 * Created by gaoyufei on 2017/6/1.
 */

class BitmapDecoder : Decoder {
    val TYPE_JPEG = "image/jpeg"
    val TYPE_PNG = "image/png"
    val TYPE_BMP = "image/bmp"
    val TYPE_WEBP = "image/webp"
    override fun canDecode(file: File): Boolean {
        return when (file.getImgType()) {
            TYPE_JPEG -> true
            TYPE_PNG -> true
            TYPE_WEBP -> true
            TYPE_BMP -> true
            else -> false
        }
    }

    override fun decode(file: File, width: Int, height: Int): Drawable? {
        "Decode:size:$width,$height".logD()
        if (width == 0 && height == 0) {
            return BitmapDrawable(BitmapFactory.decodeFile(file.path))
        }
        return BitmapDrawable(transformBitmap(file, width, height))
    }


    /**
     * This is my code
     */
    fun transformBitmap(file: File, width: Int, height: Int): Bitmap {
        // Read bitmap origin size
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(file.path, options)
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
        val bitmap = BitmapFactory.decodeFile(file.path, options)
        if(width == 0
                || height  == 0)
            return bitmap
        val newBitmap = Bitmap.createBitmap(width,height,bitmap.config)
        val canvas = Canvas(newBitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.BLACK
        paint.shader = BitmapShader(bitmap,Shader.TileMode.REPEAT,Shader.TileMode.REPEAT)
        paint.shader.setLocalMatrix(getMatrix(bitmap.width,bitmap.height,width,height))
        val rectF = RectF(0F,0F,width.toFloat(),height.toFloat())
        canvas.drawRect(rectF,paint)
        return newBitmap
    }


    fun getMatrix(bitmapWidth: Int, bitmapHeight: Int, needWidth: Int, needHeight: Int): Matrix {
        val matrix = Matrix()
        val scale: Float
        var dx = 0f
        var dy = 0f
        //reset the matrix
        matrix.set(null)
        //计算从原始的bitmap到UI需要显示的bitmap，需要的缩放和位移，其中，如果原图宽度大于高度，则只位移X，否则位移Y
        if (bitmapWidth * needHeight > needWidth * bitmapHeight) {
            scale = needHeight / bitmapHeight.toFloat()
            dx = (needWidth - bitmapWidth * scale) * 0.5f
        } else {
            scale = needWidth / bitmapWidth.toFloat()
            dy = (needHeight - bitmapHeight * scale) * 0.5f
        }

        //传入缩放设置
        matrix.setScale(scale, scale)
        //传入位移设置
        matrix.postTranslate(dx, dy)
        //将Martix设置给BitmapShader
        return matrix
    }

//    fun centerCrop(bitmap: Bitmap, width: Int, height: Int): Bitmap {
//        if(width == 0
//                || height == 0)
//            return bitmap
//        // if width and height neither zero, resize the bitmap
//        var bitmapNewWidth = bitmap.width
//        var bitmapNewHeight = bitmap.height
//        var offsetX = 0
//        var offsetY = 0
//
//        "width:$width,height:$height,bW:$bitmapNewWidth,bH:$bitmapNewHeight".logD()
//        // 以宽度为基准
//        if(width.toFloat()/bitmapNewWidth.toFloat() > height.toFloat()/bitmapNewHeight.toFloat()){
//            bitmapNewHeight = (bitmapNewWidth * (height.toFloat()/width.toFloat())).toInt()
//            offsetY = (bitmap.height - bitmapNewHeight)/2
//        }else{
//            bitmapNewWidth = (bitmapNewHeight * (width.toFloat()/height.toFloat())).toInt()
//            offsetX = (bitmap.width - bitmapNewWidth)/2
//        }
//        "bW:$bitmapNewWidth,bH:$bitmapNewHeight,offsetX:$offsetX,offsetY:$offsetY".logD()
//
//        return Bitmap.createBitmap(bitmap,offsetX,offsetY,bitmapNewWidth,bitmapNewHeight)
//    }

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