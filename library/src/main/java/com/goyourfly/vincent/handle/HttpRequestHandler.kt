package com.goyourfly.vincent.handle

import android.accounts.NetworkErrorException
import android.net.Uri
import com.goyourfly.vincent.cache.CacheManager
import com.goyourfly.vincent.common.logD
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by gaoyufei on 2017/5/31.
 */
class HttpRequestHandler(val fileCacheManager: CacheManager<InputStream>) : RequestHandler<Boolean> {
    val TIMEOUT = 60 * 1000
    val SCHEME_HTTP = "http"
    val SCHEME_HTTPS = "https"

    override fun canHandle(uri: Uri) = SCHEME_HTTP == uri.scheme || SCHEME_HTTPS == uri.scheme

    @Throws(IOException::class, NetworkErrorException::class)
    override fun fetchSync(key: String, uri: Uri): Boolean {
        "HttpRequestHandlerStart:${uri.toString()}".logD()

        val url = URL(uri.toString())
        val con: HttpURLConnection = url.openConnection() as HttpURLConnection
//        con.connectTimeout = TIMEOUT
        con.requestMethod = "GET"
        if (con.responseCode != HttpURLConnection.HTTP_OK) {
            return false
        }
        "HttpRequestHandlerResponse:${uri.toString()}".logD()
        val result = fileCacheManager.write(key, con.inputStream)
        con.disconnect()
        return result
    }
}
