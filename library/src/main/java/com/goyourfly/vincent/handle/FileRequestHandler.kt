package com.goyourfly.vincent.handle

import android.content.ContentResolver.SCHEME_FILE
import android.net.Uri
import com.goyourfly.vincent.cache.CacheManager
import com.goyourfly.vincent.common.logD
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

/**
 * Created by gaoyufei on 2017/6/2.
 */

class FileRequestHandler(val fileCacheManager: CacheManager<File>) : RequestHandler<File>() {
    override fun canHandle(uri: Uri): Boolean {
        return SCHEME_FILE == uri.scheme
    }

    override fun fetchSync(key: String, uri: Uri): File? {
        "FileRequestHandler:uri:${uri.path}".logD()
        fileCacheManager.delete(key)
        val fileTo = fileCacheManager.get(key) ?: return null
        fileCacheManager.set(key, fileTo)
        val fileFrom = File(uri.path)
        var input:FileInputStream? = null
        var output:FileOutputStream? = null
        try {
            input = FileInputStream(fileFrom)
            output = FileOutputStream(fileTo)

            val buf = ByteArray(1024)
            while (true) {
                val len = input.read(buf)
                if (len <= 0)
                    break
                output.write(buf, 0, len)
            }
            output.close()
            input.close()
            return fileTo
        } catch (e: IOException) {
            fileCacheManager.delete(key)
            output?.close()
            input?.close()
            return null
        }
    }
}