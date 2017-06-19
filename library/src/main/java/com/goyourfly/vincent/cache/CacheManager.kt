package com.goyourfly.vincent.cache

/**
 * Created by gaoyufei on 2017/5/31.
 */
interface CacheManager<T> {

    fun write(key: String, value: T):Boolean

    fun read(key: String): T?

    fun exist(key: String): Boolean

    fun delete(key: String): Boolean

    fun clear();

    fun size(): Long
}
