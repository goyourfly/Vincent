package com.goyourfly.vincent.handle

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import com.goyourfly.vincent.cache.CacheManager
import java.io.InputStream

/**
 * Created by gaoyufei on 2017/6/24.
 */

class ContentHandler(val context: Context, val fileCacheManager: CacheManager<InputStream>) : RequestHandler<Boolean> {
    override fun canHandle(uri: Uri): Boolean {
        return ContentResolver.SCHEME_CONTENT == uri.scheme
    }

    override fun fetchSync(key: String, uri: Uri): Boolean {
        val inputStream = context.contentResolver.openInputStream(uri)
        fileCacheManager.delete(key)
        return fileCacheManager.write(key, inputStream)
    }

}
