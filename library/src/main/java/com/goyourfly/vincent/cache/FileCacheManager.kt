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

class FileCacheManager(val size:Int,val path:String):CacheManager<File>{

    override fun set(key: String, value: File) {
        val file = getFile(key)
        checkFile(file)
    }

    override fun get(key: String) = getFile(key)

    override fun contain(key: String): Boolean {
        return getFile(key).exists()
    }

    override fun delete(key: String): File {
        val file = getFile(key)
        file.delete()
        return file
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