package com.goyourfly.vincent.decoder

import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import com.goyourfly.vincent.common.logD

/**
 * Created by gaoyufei on 2017/6/3.
 */

class GifDrawable(val gifDecodeHelper: GifDecodeHelper) : Drawable() {

    val handler = Handler(Looper.getMainLooper())
    val paint = Paint()
    var postNum = 0

    init {
        paint.color = Color.BLACK
    }


    override fun draw(canvas: Canvas?) {
        gifDecodeHelper.advance()
        val bitmap = gifDecodeHelper.nextFrame
        if (bitmap != null && !bitmap.isRecycled) {
            canvas?.drawBitmap(bitmap, 0F, 0F, paint)
        }
        if (gifDecodeHelper.nextDelay > 0 && isVisible && postNum == 0) {
            handler.postDelayed({
                invalidateSelf()
                postNum--;
            }, gifDecodeHelper.nextDelay.toLong())
            postNum++;
        }
    }

    override fun getIntrinsicHeight(): Int {
        return gifDecodeHelper.height
    }


    override fun getIntrinsicWidth(): Int {
        return gifDecodeHelper.width
    }


    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT;
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }
}
