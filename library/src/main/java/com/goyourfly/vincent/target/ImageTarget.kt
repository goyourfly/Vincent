package com.goyourfly.vincent.target

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.goyourfly.vincent.decoder.GifDrawable

/**
 * Created by gaoyufei on 2017/5/31.
 */
class ImageTarget(val imageView: ImageView, val placeholderId:Int, val errorId:Int): Target() {

    init {
        targetId = imageView.hashCode().toString()
        imageView.setImageResource(placeholderId)
    }

    override fun onComplete(drawable: Drawable) {
        imageView.setImageDrawable(drawable)
    }

    override fun onError(e: Exception) {
        super.onError(e)
        imageView.setImageResource(errorId)
    }
}
