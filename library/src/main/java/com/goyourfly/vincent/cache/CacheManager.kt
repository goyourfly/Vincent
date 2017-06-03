package com.goyourfly.vincent.cache

/**
 * Created by gaoyufei on 2017/5/31.
 */
interface CacheManager<T>{

    fun set(key:String,value: T)

    fun get(key:String):T?

    fun contain(key:String):Boolean

    fun delete(key:String):T?

    fun clear();

    fun trimToSize();
}
