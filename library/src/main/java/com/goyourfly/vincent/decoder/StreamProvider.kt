package com.goyourfly.vincent.decoder

import com.goyourfly.vincent.cache.CacheManager
import com.goyourfly.vincent.cache.FileLruCacheManager
import com.goyourfly.vincent.common.logD
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

/**
 * Created by Administrator on 2017/6/19 0019.
 */
class StreamProvider (val key:String,val fileLruCacheManager: CacheManager<InputStream>){
    fun getInputStream():InputStream?{
        val stream =  fileLruCacheManager.read(key)
        return stream
    }
}
