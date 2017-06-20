package com.goyourfly.vincent.scale

import android.graphics.Canvas
import android.graphics.Paint

/**
 * Created by gaoyufei on 2017/6/20.
 */
class CircleCenterCrop() : ShapeCenterCrop() {
    override fun draw(canvas: Canvas, paint: Paint) {
        val min = Math.min(canvas.width, canvas.height)
        canvas.drawCircle(canvas.width / 2.toFloat(), canvas.height / 2.toFloat(), min/2.toFloat(), paint)
    }
}