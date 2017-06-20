package com.goyourfly.vincent.scale

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF

/**
 * Created by gaoyufei on 2017/6/20.
 */

class RoundRectCenterCrop(val radius: Int) : ShapeCenterCrop() {
    override fun draw(canvas: Canvas, paint: Paint) {
        val rect = RectF(0F, 0F, canvas.width.toFloat(), canvas.height.toFloat())
        canvas.drawRoundRect(rect, radius.toFloat(), radius.toFloat(), paint)
    }

}
