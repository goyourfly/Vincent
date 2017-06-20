package com.goyourfly.vincent.scale

import android.graphics.*

/**
 * Created by gaoyufei on 2017/6/20.
 */

class CenterCrop : ScaleType() {

    override fun scale(bitmap: Bitmap, expectWidth: Int, expectHeight: Int): Bitmap {
        if (expectWidth <= 0
                || expectHeight <= 0)
            return bitmap
        val newBitmap = Bitmap.createBitmap(expectWidth, expectHeight, bitmap.config)
        val canvas = Canvas(newBitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.BLACK
        paint.shader = BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
        paint.shader.setLocalMatrix(getMatrix(bitmap.width, bitmap.height, expectWidth, expectHeight))
        val rectF = RectF(0F, 0F, expectWidth.toFloat(), expectHeight.toFloat())
        canvas.drawRect(rectF, paint)
        return newBitmap
    }


    fun getMatrix(bitmapWidth: Int, bitmapHeight: Int, expectWidth: Int, expecteight: Int): Matrix {
        val matrix = Matrix()
        val scale: Float
        var dx = 0f
        var dy = 0f
        //reset the matrix
        matrix.set(null)
        //计算从原始的bitmap到UI需要显示的bitmap，需要的缩放和位移，其中，如果原图宽度大于高度，则只位移X，否则位移Y
        if (bitmapWidth * expecteight > expectWidth * bitmapHeight) {
            scale = expecteight / bitmapHeight.toFloat()
            dx = (expectWidth - bitmapWidth * scale) * 0.5f
        } else {
            scale = expectWidth / bitmapWidth.toFloat()
            dy = (expecteight - bitmapHeight * scale) * 0.5f
        }

        //传入缩放设置
        matrix.setScale(scale, scale)
        //传入位移设置
        matrix.postTranslate(dx, dy)
        //将Martix设置给BitmapShader
        return matrix
    }

}
