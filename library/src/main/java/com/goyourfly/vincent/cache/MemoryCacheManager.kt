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
class MemoryCacheManager(val cacheSize:Long):CacheManager<Drawable>{

    val lruCache:LruCache<String,Drawable> = MyLruCache(cacheSize.toInt())

    override fun exist(key: String): Boolean {
        return lruCache.get(key) != null
    }
    override fun write(key: String, value: Drawable): Boolean {
        lruCache.put(key,value)
        return true
    }

    override fun read(key: String):Drawable? {
        return lruCache.get(key)
    }

    override fun delete(key: String):Boolean {
        lruCache.remove(key)
        return true
    }

    override fun clear() {
        lruCache.evictAll()
    }

    override fun size(): Long {
        return lruCache.size().toLong()
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
