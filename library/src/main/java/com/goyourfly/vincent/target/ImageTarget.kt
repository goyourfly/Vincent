package com.goyourfly.vincent.target

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.goyourfly.vincent.common.logD
import com.goyourfly.vincent.decoder.GifDrawable

/**
 * Created by gaoyufei on 2017/5/31.
 */
class ImageTarget(val imageView: ImageView): Target() {

    init {
        targetId = imageView.hashCode().toString()
    }

    override fun onInit(drawable: Drawable) {
        imageView.setImageDrawable(drawable)
    }

    override fun onComplete(drawable: Drawable) {
        imageView.setImageDrawable(drawable)
    }

    override fun onError(drawable: Drawable,e: Exception) {
        imageView.setImageDrawable(drawable)
    }
}
