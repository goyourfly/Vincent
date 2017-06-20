package com.goyourfly.vincent

import android.app.ActivityManager
import android.content.ContentResolver
import android.content.Context
import android.content.pm.ApplicationInfo
import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.TextUtils
import android.widget.ImageView
import com.goyourfly.vincent.cache.CacheManager
import com.goyourfly.vincent.cache.FileLruCacheManager
import com.goyourfly.vincent.cache.MemoryCacheManager
import com.goyourfly.vincent.common.HashCodeGenerator
import com.goyourfly.vincent.common.getService
import com.goyourfly.vincent.common.logD
import com.goyourfly.vincent.scale.RectCenterCrop
import com.goyourfly.vincent.scale.ScaleType
import com.goyourfly.vincent.target.ImageTarget
import com.goyourfly.vincent.target.Target
import com.goyourfly.vincent.transform.Transform
import java.io.File
import java.io.InputStream

/**
 * Created by gaoyufei on 2017/5/31.
 * Vincent is a class which provide
 * full interface to user
 */
object Vincent {
    var config:Config? = null
    var dispatcher: Dispatcher? = null
    var memoryCache: CacheManager<Drawable>? = null
    var fileCache: CacheManager<InputStream>? = null
    var context: Context? = null

    @JvmStatic
    fun config(config:Config){
        this.config = config
    }


    @JvmStatic
    fun with(context: Context): Builder {
        this.context = context
        if(config == null){
            config = Config(context)
        }

        if (memoryCache == null) {
            memoryCache = MemoryCacheManager(config!!.memoryCacheSize)
        }
        if (fileCache == null) {
            fileCache = FileLruCacheManager(config!!.fileCacheDir, config!!.fileCacheSize , 1)
        }
        if (dispatcher == null) {
            dispatcher = Dispatcher(context.resources, memoryCache!!, fileCache!!)
        }
        return Builder(dispatcher!!)
    }


    /**
     * Provide all info of request
     */
    class Builder(val dispatcher: Dispatcher) {
        /**
         * img uri ,url/file_path
         */
        private var uri: Uri = Uri.EMPTY
        /**
         * Resize image size
         */
        private var resizeWidth: Int = 0
        private var resizeHeight: Int = 0
        /**
         * Scale mode, center_crop
         */
        private var scaleType: ScaleType = RectCenterCrop()
        /**
         * if fit imageview size
         */
        private var fit = true
        /**
         * the target for image
         */
        private var target: Target = Target()
        /**
         * placeholder res id
         */
        private var placeholderId = -1
        /**
         * error res id
         */
        private var errorId = -1

        private val transformList = arrayListOf<Transform>()

        fun placeholder(id: Int): Builder {
            this.placeholderId = id
            return this
        }

        fun error(id: Int): Builder {
            this.errorId = id
            return this
        }

        fun fit(): Builder {
            this.fit = true
            return this
        }

        fun notFit(): Builder {
            this.fit = false
            return this
        }

        fun resize(width: Int, height: Int): Builder {
            this.resizeWidth = width
            this.resizeHeight = height
            if (width == 0
                    && height == 0)
                throw IllegalArgumentException("The width and height must have a greater than 0")
            return this
        }

        fun transform(transform: Transform): Builder {
            transformList.add(transform)
            return this
        }

        fun load(path: String): Builder {
            if (TextUtils.isEmpty(path)) {
                "Path is empty".logD()
                return this
            }
            val file = File(path)
            if (file.exists()) {
                return load(file)
            }
            return load(Uri.parse(path))
        }

        fun load(file: File): Builder {
            return load(Uri.fromFile(file))
        }

        fun load(uri: Uri): Builder {
            this.uri = uri
            return this
        }

        fun load(id: Int): Builder {
            this.uri = Uri.fromParts(ContentResolver.SCHEME_ANDROID_RESOURCE, "", id.toString())
            return this
        }

        fun scaleType(scaleType: ScaleType): Builder {
            this.scaleType = scaleType
            return this
        }


        fun into(target: Target) {
            if (fit && (resizeWidth > 0 || resizeHeight > 0))
                throw IllegalArgumentException("fit can not resize")
            this.target = target
            val requestInfo = RequestContext(
                    context!!,
                    uri,
                    fit,
                    resizeWidth,
                    resizeHeight,
                    scaleType,
                    target,
                    placeholderId,
                    errorId,
                    HashCodeGenerator(),
                    transformList
            )
            dispatcher.dispatchSubmit(requestInfo)
        }

        fun into(imageView: ImageView) {
            into(ImageTarget(imageView))
        }
    }

    class Config(val context: Context) {
        var memoryCacheSize = calculateMemoryCacheSize(context)
        var fileCacheSize = 1024 * 1024 * 100L // bytes
        var fileCacheDir = getCacheDir()

        fun getCacheDir(): String {
            return "data/data/${context.packageName}/cache/vincent"
        }


        fun calculateMemoryCacheSize(context: Context): Long {
            val am = getService<ActivityManager>(context, Context.ACTIVITY_SERVICE)
            val largeHeap = context.applicationInfo.flags and ApplicationInfo.FLAG_LARGE_HEAP != 0
            val memoryClass = if (largeHeap) am.getLargeMemoryClass() else am.getMemoryClass()
            // Target ~15% of the available heap.
            return 1024L * 1024L * memoryClass.toLong() / 7
        }
    }
}
