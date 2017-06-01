package com.goyourfly.vincent.cache

import android.graphics.Bitmap
import com.goyourfly.vincent.RequestInfo
import com.goyourfly.vincent.decoder.DecodeManager
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream

/**
 * Created by gaoyufei on 2017/6/1.
 */

class FileCacheManager(val size:Int,val path:String):CacheManager<CacheSeed>{

    override fun set(key: String, value: CacheSeed) {
        val file = getFile(key)
        checkFile(file)
        var fileOutputStream:FileOutputStream? = null
        try {
            fileOutputStream = FileOutputStream(file)
            value.value?.compress(Bitmap.CompressFormat.JPEG,70,fileOutputStream)
        }catch (e:FileNotFoundException){
            e.printStackTrace()
        }finally {
            fileOutputStream?.close()
        }
    }

    override fun get(key: String): CacheSeed {
        return CacheSeed(key,DecodeManager.decode(getFile(key)))
    }

    override fun contain(key: String): Boolean {
        return getFile(key).exists()
    }

    override fun delete(key: String): CacheSeed {
        val file = getFile(key)
        file.delete()
        return CacheSeed(key,null)
    }

    override fun update(key: String, value: CacheSeed) {
        delete(key)
        set(key,value)
    }

    override fun clear() {
        val files = File(path).listFiles()
        for (file in files){
            file.delete()
        }
    }

    fun getFile(key:String):File{
        return File(path,key)
    }

    fun checkFile(file:File){
        if(!file.parentFile.exists())
            file.parentFile.mkdirs()
        if(!file.exists())
            file.createNewFile()
    }
}