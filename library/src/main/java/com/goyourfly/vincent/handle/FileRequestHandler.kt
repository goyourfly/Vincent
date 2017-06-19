package com.goyourfly.vincent.handle

import android.content.ContentResolver.SCHEME_FILE
import android.net.Uri
import com.goyourfly.vincent.cache.CacheManager
import com.goyourfly.vincent.common.logD
import java.io.*

/**
 * Created by gaoyufei on 2017/6/2.
 */

class FileRequestHandler(val fileCacheManager: CacheManager<InputStream>) : RequestHandler<Boolean>{
    override fun canHandle(uri: Uri): Boolean {
        return SCHEME_FILE == uri.scheme
    }

    override fun fetchSync(key: String, uri: Uri): Boolean {
        "FileRequestHandler:uri:${uri.path}".logD()
        fileCacheManager.delete(key)
        val fileFrom = File(uri.path)
        return fileCacheManager.write(key, FileInputStream(fileFrom))
    }
}