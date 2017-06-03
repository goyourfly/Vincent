package com.goyourfly.vincent.cache

import android.graphics.Bitmap
import android.os.Build
import android.util.LruCache
import com.goyourfly.vincent.common.byteSize
import com.goyourfly.vincent.common.logD

/**
 * Created by gaoyufei on 2017/5/31.
 */
class MemoryCacheManager(val cacheSize:Int):CacheManager<Bitmap>{

    val lruCache:LruCache<String,Bitmap> = MyLruCache(cacheSize)

    override fun contain(key: String): Boolean {
        return lruCache.get(key) != null
    }
    override fun set(key: String, value: Bitmap) {
        lruCache.put(key,value)
        "set:$key=$value".logD()
    }

    override fun get(key: String):Bitmap? {
        "get:key=$key,size:${lruCache.size()}/${lruCache.maxSize()}".logD()
        return lruCache.get(key)
    }

    override fun delete(key: String):Bitmap? {
        "delete:key=$key".logD()
        return lruCache.remove(key)
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

    class MyLruCache(maxSize:Int):LruCache<String,Bitmap>(maxSize){
        override fun sizeOf(key: String?, value: Bitmap?): Int {
            if(value == null)
                return 0
            return value.byteSize()
        }
    }
}
