package com.goyourfly.vincent

import android.os.Handler
import com.goyourfly.vincent.common.logD
import com.goyourfly.vincent.handle.RequestHandler
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream
import java.util.concurrent.Callable

/**
 * Created by gaoyufei on 2017/5/31.
 */

class RunFileDownloader(val handler: Handler,
                        val requestHandler: RequestHandler<Boolean>?,
                        val requestContext: RequestContext) : Callable<Boolean> {
    override fun call(): Boolean {
        if (requestHandler == null) {
            errorHandle()
            return false
        }
        val result = requestHandler.fetchSync(requestContext.keyForCache, requestContext.uri)
        val msg = handler.obtainMessage(Dispatcher.What.DOWNLOAD_FINISH)
        msg.obj = requestContext.key
        handler.sendMessage(msg)
        return result
    }

    fun errorHandle() {
        val msg = handler.obtainMessage(Dispatcher.What.DOWNLOAD_ERROR)
        msg.obj = requestContext.key
        handler.sendMessage(msg)
    }
}