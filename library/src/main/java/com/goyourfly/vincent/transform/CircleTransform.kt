package com.goyourfly.vincent.transform

import android.graphics.*

/**
 * Created by Administrator on 2017/6/4 0004.
 */
class CircleTransform(): ShapeTransform() {
    override fun transform(bitmap: Bitmap): Bitmap {
        val newBitmap = Bitmap.createBitmap(bitmap.width,bitmap.height,bitmap.config)
        val canvas = Canvas(newBitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.BLACK
        paint.shader = BitmapShader(bitmap,Shader.TileMode.REPEAT,Shader.TileMode.REPEAT)
        canvas.drawCircle(newBitmap.width/2.toFloat(),newBitmap.height/2.toFloat(),newBitmap.width/2.toFloat(),paint)
        return newBitmap
    }
}