package com.goyourfly.vincent.cache

import android.graphics.Bitmap
import com.goyourfly.vincent.RequestInfo
import com.goyourfly.vincent.common.logD
import com.goyourfly.vincent.common.logE
import com.goyourfly.vincent.decoder.DecodeManager
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.util.*
import kotlin.Comparator
import kotlin.coroutines.experimental.EmptyCoroutineContext.plus

/**
 * Created by gaoyufei on 2017/6/1.
 */

class FileCacheManager(val maxSize: Int, val path: String) : CacheManager<File> {
    override fun set(key: String, value: File) {
        trimToSize(maxSize)
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
        for (file in files) {
            file.delete()
        }
    }

    fun getFile(key: String): File {
        return File(path, key)
    }

    fun checkFile(file: File) {
        if (!file.parentFile.exists())
            file.parentFile.mkdirs()
        if (!file.exists())
            file.createNewFile()
    }

    fun trimToSize(maxSize: Int) {
        var size = getSize()

        "TrimToSize:$size/$maxSize".logD()
        if (size < maxSize)
            return
        val file = File(path)
        while (true) {
            if (size < maxSize) {
                break
            }
            val files = file.listFiles()
            if(files != null
                    && files.isNotEmpty()){
                files[0].delete()
                "DeleteFile:$size/$maxSize,deleteFile:${files[0].absolutePath}".logD()
            }
            size = getSize()
        }
    }

    fun getSize(): Long {
        var size = 0L
        val file = File(path)
        val childFiles = file.listFiles()
        if (childFiles == null)
            return 0
        for (f in childFiles) {
            size += f.length()
        }
        return size
    }
}