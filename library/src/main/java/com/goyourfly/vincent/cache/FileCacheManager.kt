package com.goyourfly.vincent.cache

import com.goyourfly.vincent.common.logD
import java.io.File

/**
 * Created by gaoyufei on 2017/6/1.
 */

class FileCacheManager(val maxSize: Int, val path: String) : CacheManager<File> {
    override fun count():Int {
        val file = File(path)
        val childFiles = file.listFiles() ?: return 0
        return childFiles.size
    }

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
        for (file in files) {
            file.delete()
        }
    }

    fun getFile(key: String): File {
        return File(path, key)
    }

    private fun checkFile(file: File) {
        if (!file.parentFile.exists())
            file.parentFile.mkdirs()
        if (!file.exists())
            file.createNewFile()
    }

    override fun trimToSize() {
        var size = getSize()

        "TrimToSize:$size/$maxSize".logD()
        if (size < maxSize)
            return
        val file = File(path)
        val files = file.listFiles()
        var i = 0
        while (true) {
            if (size < maxSize || i > files.size - 1) {
                break
            }
            if(files != null
                    && files.isNotEmpty()){
                files[i].delete()
                "DeleteFile:$size/$maxSize,deleteFile:${files[i].name},fileTime:${files[i].lastModified()}".logD()
            }
            size = getSize()
            i++
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

    class FileComparator:Comparator<File>{
        override fun compare(o1: File?, o2: File?): Int {
            return o1!!.compareTo(o2)
        }
    }
}