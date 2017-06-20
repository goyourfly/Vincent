package com.goyourfly.vincent

import android.os.Handler
import android.os.SystemClock
import com.goyourfly.vincent.handle.RequestHandler
import java.util.concurrent.Callable

/**
 * Created by gaoyufei on 2017/5/31.
 */

class RunFileDownloader(val handler: Handler,
                        val requestHandler: RequestHandler<Boolean>?,
                        val requestContext: RequestContext) : Callable<Boolean> {
    override fun call(): Boolean {
        if (requestHandler == null) {
            errorMsg()
            return false
        }
        val debugTime = SystemClock.elapsedRealtime()
        try {
            val result = requestHandler.fetchSync(requestContext.keyForCache, requestContext.uri)
            if(result) {
                val msg = handler.obtainMessage(Dispatcher.What.DOWNLOAD_FINISH)
                msg.obj = requestContext.key
                handler.sendMessage(msg)
            }else{
                errorMsg()
            }
            requestContext.debugInfo.downloadTime = SystemClock.elapsedRealtime() - debugTime
            return result
        }catch (e:Exception){
            e.printStackTrace()
            errorMsg()
            return false
        }
    }

    fun errorMsg() {
        val msg = handler.obtainMessage(Dispatcher.What.DOWNLOAD_ERROR)
        msg.obj = requestContext.key
        handler.sendMessage(msg)
    }
}