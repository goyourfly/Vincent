package com.goyourfly.vincent.handle

import android.content.ContentResolver
import android.graphics.drawable.Drawable
import android.net.Uri
import android.content.res.AssetFileDescriptor
import android.content.res.Resources
import com.goyourfly.vincent.cache.CacheManager
import com.goyourfly.vincent.cache.FileCacheManager
import java.io.*


/**
 * Created by Administrator on 2017/6/4 0004.
 */

class ResourceRequestHandler(val fileCacheManager: CacheManager<File>, val res: Resources) : RequestHandler<File>() {
    override fun canHandle(uri: Uri): Boolean {
        return uri.scheme == ContentResolver.SCHEME_ANDROID_RESOURCE
    }

    override fun fetchSync(key: String, uri: Uri): File? {
        val id = uri.fragment.toInt()

        fileCacheManager.delete(key)
        val fileTo = fileCacheManager.get(key) ?: return null
        fileCacheManager.set(key, fileTo)

        val raw = res.openRawResourceFd(id)
        var inputStream:InputStream? = null
        var outputStream:OutputStream? = null
        try {
            inputStream = raw.createInputStream()
            outputStream = FileOutputStream(fileTo)

            val data = ByteArray(1024)

            while (true) {
                val nRead = inputStream.read(data, 0, data.size)
                if (nRead == -1)
                    break
                outputStream.write(data, 0, nRead)
            }
            outputStream.flush()
            outputStream.close()
            inputStream.close()
            return fileTo
        } catch (e: IOException) {
            e.printStackTrace()
            fileCacheManager.delete(key)
            outputStream?.close()
            inputStream?.close()
            return null
        }
    }

}