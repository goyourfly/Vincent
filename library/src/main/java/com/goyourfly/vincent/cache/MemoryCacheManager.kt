package com.goyourfly.vincent.cache

import android.util.LruCache
import com.goyourfly.vincent.common.logD

/**
 * Created by gaoyufei on 2017/5/31.
 */
class MemoryCacheManager(cacheSize:Int):CacheManager<CacheSeed>{

    val TAG = "MemoryCacheManager"
    val lruCache:LruCache<String,CacheSeed> = LruCache(cacheSize)

    override fun contain(key: String): Boolean {
        return lruCache.get(key) != null
    }
    override fun set(key: String, value: CacheSeed) {
        lruCache.put(key,value)
        "set:$key=$value".logD(TAG)
    }

    override fun get(key: String):CacheSeed {
        "get:key=$key".logD(TAG)
        return lruCache.get(key)
    }

    override fun delete(key: String):CacheSeed {
        "delete:key=$key".logD(TAG)
        return lruCache.remove(key)
    }

    override fun update(key: String, value: CacheSeed) {
        "update:$key=$value".logD(TAG)
        lruCache.put(key,value)
    }

    override fun clear() {
        "clear------------>>>".logD(TAG)
        lruCache.evictAll()
    }

}
