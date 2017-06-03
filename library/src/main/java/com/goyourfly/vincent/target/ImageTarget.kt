package com.goyourfly.vincent.target

import android.graphics.Bitmap
import android.widget.ImageView
import com.goyourfly.vincent.target.BitmapTarget

/**
 * Created by gaoyufei on 2017/5/31.
 */
class ImageTarget(val imageView: ImageView, val placeholderId:Int, val errorId:Int): BitmapTarget() {

    init {
        targetId = imageView.hashCode().toString()
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
