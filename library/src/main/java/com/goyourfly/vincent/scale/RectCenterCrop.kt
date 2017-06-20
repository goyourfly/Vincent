package com.goyourfly.vincent.scale

import android.graphics.*

/**
 * Created by gaoyufei on 2017/6/20.
 */

class RectCenterCrop : ShapeCenterCrop() {
    override fun draw(canvas: Canvas, paint: Paint) {
        val rectF = RectF(0F, 0F, canvas.width.toFloat(), canvas.height.toFloat())
        canvas.drawRect(rectF, paint)
    }
}
