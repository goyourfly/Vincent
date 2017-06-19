package com.goyourfly.vincent.handle

import android.content.ContentResolver
import android.net.Uri
import android.content.res.Resources
import com.goyourfly.vincent.cache.CacheManager
import java.io.*


/**
 * Created by Administrator on 2017/6/4 0004.
 */

class ResourceRequestHandler(val fileCacheManager: CacheManager<InputStream>, val res: Resources) : RequestHandler<Boolean> {
    override fun canHandle(uri: Uri): Boolean {
        return uri.scheme == ContentResolver.SCHEME_ANDROID_RESOURCE
    }

    override fun fetchSync(key: String, uri: Uri): Boolean {
        val id = uri.fragment.toInt()
        val raw = res.openRawResourceFd(id)
        return fileCacheManager.write(key, raw.createInputStream())
    }

}