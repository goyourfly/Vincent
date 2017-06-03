package com.goyourfly.vincent.cache

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.LruCache
import com.goyourfly.vincent.common.byteSize
import com.goyourfly.vincent.common.logD

/**
 * Created by gaoyufei on 2017/5/31.
 */
class MemoryCacheManager(val cacheSize:Int):CacheManager<Drawable>{

    val lruCache:LruCache<String,Drawable> = MyLruCache(cacheSize)

    override fun contain(key: String): Boolean {
        return lruCache.get(key) != null
    }
    override fun set(key: String, value: Drawable) {
        lruCache.put(key,value)
        "set:$key=$value".logD()
    }

    override fun get(key: String):Drawable? {
        "get:key=$key,size:${lruCache.size()}/${lruCache.maxSize()}".logD()
        return lruCache.get(key)
    }

    override fun delete(key: String):Drawable? {
        "delete:key=$key".logD()
        val drawable = lruCache.remove(key)
        return drawable
    }

    override fun clear() {
        "clear------------>>>".logD()
        lruCache.evictAll()
    }

    override fun trimToSize() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            lruCache.trimToSize(cacheSize)
        }
    }

    override fun count(): Int {
        return 0
    }

    class MyLruCache(maxSize:Int):LruCache<String,Drawable>(maxSize){
        override fun sizeOf(key: String?, value: Drawable?): Int {
            if(value == null)
                return 0
            if(value is BitmapDrawable && value.bitmap != null){
                return value.bitmap.byteSize()
            }
            return 0;
        }
    }
}
