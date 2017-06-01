package com.goyourfly.vincent

import android.content.Context
import android.net.Uri
import android.text.TextUtils
import android.widget.ImageView
import com.goyourfly.vincent.cache.CacheManager
import com.goyourfly.vincent.cache.CacheSeed
import com.goyourfly.vincent.cache.FileCacheManager
import com.goyourfly.vincent.cache.MemoryCacheManager
import com.goyourfly.vincent.common.HashCodeGenerator

/**
 * Created by gaoyufei on 2017/5/31.
 * Vincent is a class which provide
 * full interface to user
 */
object Vincent{
    var dispatcher:Dispatcher? = null
    val keyGenerator = HashCodeGenerator()
    val memoryCache = MemoryCacheManager(1024 * 10)
    var fileCache:CacheManager<CacheSeed>? = null

    fun with(context:Context):Builder{
        if(fileCache == null){
            fileCache = FileCacheManager(1024 * 20,"data/data/${context.packageName}/vincent/cache/")
        }
        if(dispatcher == null){
            dispatcher = Dispatcher(keyGenerator, memoryCache, fileCache!!)
        }
        return Builder(dispatcher!!)
    }


    /**
     * Provide all info of request
     */
    class Builder(val dispatcher: Dispatcher){
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

        fun into(target:Target){
            this.target = target
            val requestInfo = RequestInfo(
                    uri,
                    pre_uri,
                    resizeWidth,
                    resizeHeight,
                    scale,
                    cache,
                    priority,
                    target,
                    placeholderId,
                    errorId,
                    HashCodeGenerator()
            )
            dispatcher.dispatchSubmit(requestInfo)
        }

        fun into(imageView: ImageView){
            into(ImageTarget(imageView,placeholderId,errorId))
        }
    }
}
