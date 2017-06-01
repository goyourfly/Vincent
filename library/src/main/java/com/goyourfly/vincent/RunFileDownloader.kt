package com.goyourfly.vincent

import android.os.Handler
import com.goyourfly.vincent.common.logD
import com.goyourfly.vincent.common.logE
import java.io.File
import java.util.concurrent.Callable

/**
 * Created by gaoyufei on 2017/5/31.
 */

class RunFileDownloader(val handler:Handler,
                        val requestHandler: RequestHandler<File>,
                        val requestInfo: RequestInfo):Callable<File>{
    override fun call(): File? {
        "start download bitmap".logD()
        try {
            val bitmap = requestHandler.fetchSync(requestInfo.keyForCache,requestInfo.uri)
            val msg = handler.obtainMessage(Dispatcher.What.DOWNLOAD_FINISH)
            msg.obj = requestInfo.key
            handler.sendMessage(msg)
            "end download bitmap".logD()
            return bitmap
        }catch (e: Exception){
            e.message?.logE()
            val msg = handler.obtainMessage(Dispatcher.What.DOWNLOAD_ERROR)
            msg.obj = requestInfo.key
            handler.sendMessage(msg)
            return null
        }
    }
}