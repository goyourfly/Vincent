package com.goyourfly.vincent

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView

/**
 * Created by gaoyufei on 2017/5/31.
 */
class ImageTarget(val imageView: ImageView,val placeholderId:Int,val errorId:Int): BitmapTarget() {

    init {
        imageView.setImageResource(placeholderId)
    }

    override fun onComplete(bitmap: Bitmap) {
        imageView.setImageBitmap(bitmap)
    }

    override fun onError(e: Exception) {
        super.onError(e)
        imageView.setImageResource(errorId)
    }
}
