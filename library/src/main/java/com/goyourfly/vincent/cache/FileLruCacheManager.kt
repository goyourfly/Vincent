package com.goyourfly.vincent.cache

import java.io.*

/**
 * Created by Administrator on 2017/6/19 0019.
 */
class FileLruCacheManager(val path: String, val maxSize: Long, val appVersion: Int):CacheManager<InputStream> {

    val lruCache: DiskLruCache = DiskLruCache.open(File(path), appVersion, 1, maxSize)


    override fun write(key: String, value: InputStream):Boolean {
        val editor = lruCache.edit(key) ?: return false
        var outputStream:OutputStream? = null
        var inputStream:InputStream? = null
        try {
            inputStream = value
            outputStream = BufferedOutputStream(editor.newOutputStream(0))
            val bytes = ByteArray(1024)
            while (true) {
                val len = inputStream.read(bytes)
                if (len == -1)
                    break
                outputStream.write(bytes,0,len)
            }
            editor.commit()
            return true
        } catch (e: IOException) {
            e.printStackTrace()
            editor.abort()
            return false
        }finally {
            try {
                inputStream?.close()
                outputStream?.close()
            }catch (e:Exception){
                e.printStackTrace()
            }
            lruCache.flush()
        }
    }

    override fun read(key:String): InputStream? {
        val snapshot = lruCache.get(key) ?: return null
        return BufferedInputStream(snapshot.getInputStream(0))
    }

    override fun delete(key:String):Boolean{
        try {
            return lruCache.remove(key)
        }catch (e:IOException){
            e.printStackTrace()
            return false
        }
    }

    override fun size():Long{
        return lruCache.size()
    }

    override fun clear(){
        lruCache.delete()
    }

    override fun exist(key:String):Boolean{
        return lruCache.get(key) != null
    }
}
