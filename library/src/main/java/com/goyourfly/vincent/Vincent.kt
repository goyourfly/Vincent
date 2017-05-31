package com.goyourfly.vincent

import android.content.Context
import android.net.Uri
import android.text.TextUtils
import android.widget.ImageView

/**
 * Created by gaoyufei on 2017/5/31.
 * Vincent is a class which provide
 * full interface to user
 */
object Vincent{
    fun with(context:Context):Builder{
        return Builder()
    }


    /**
     * Provide all info of request
     */
    class Builder{
        /**
         * img uri ,url/file_path
         */
        var uri:Uri = Uri.EMPTY
        /**
         * img smallImg url
         */
        var pre_uri:Uri = Uri.EMPTY
        /**
         * Resize image size
         */
        var resizeWidth:Int = 0
        var resizeHeight:Int = 0
        /**
         * Scale mode, center_crop, center_face
         */
        var scale: Scale = Scale.CENTER_CROP
        /**
         * the target for image
         */
        var target:Target = BitmapTarget()
        /**
         * placeholder res id
         */
        var placeholderId = -1
        /**
         * error res id
         */
        var errorId = -1

        /**
         * cache mode full,origin,fit
         */
        var cache: Cache = Cache.FULL

        var priority:Priority = Priority.NORMAL


        fun placeholder(id:Int):Builder{
            this.placeholderId = id
            return this
        }

        fun error(id: Int):Builder{
            this.errorId = id
            return this
        }

        fun resize(width:Int,height:Int):Builder{
            this.resizeWidth = width
            this.resizeHeight = height
            if(width == 0
                    && height == 0)
                throw IllegalArgumentException("The width and height must have a greater than 0")
            return this
        }

        fun load(path:String):Builder{
            if(TextUtils.isEmpty(path))
                return this
            uri = Uri.parse(path)
            return this
        }

        fun preview(path:String):Builder{
            if(TextUtils.isEmpty(path))
                return this
            pre_uri = Uri.parse(path)
            return this
        }

        fun scale(scale:Scale):Builder{
            this.scale = scale
            return this
        }

        fun cache(cache:Cache):Builder{
            this.cache = cache
            return this
        }

        fun priority(priority: Priority):Builder{
            this.priority = priority
            return this
        }

        fun into(target:Target):Builder{
            this.target = target
            return this
        }

        fun into(imageView: ImageView):Builder{
            return into(ImageTarget(imageView,placeholderId,errorId))
        }
    }
}
