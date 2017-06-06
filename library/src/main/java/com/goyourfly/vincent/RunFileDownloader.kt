package com.goyourfly.vincent

import android.os.Handler
import com.goyourfly.vincent.common.logD
import com.goyourfly.vincent.handle.RequestHandler
import java.io.File
import java.io.FileNotFoundException
import java.util.concurrent.Callable

/**
 * Created by gaoyufei on 2017/5/31.
 */

class RunFileDownloader(val handler:Handler,
                        val requestHandler: RequestHandler<File>?,
                        val requestContext: RequestContext):Callable<File>{
    override fun call(): File? {
        "start download file".logD()
        if(requestHandler == null) {
            errorHandle()
            return null
        }
        try {
            val file = requestHandler.fetchSync(requestContext.keyForFileCache, requestContext.uri)
            if(file == null){
                throw FileNotFoundException("File not found:${requestContext.uri.toString()},${file}")
            }else if(file.length() == 0L){
                throw FileNotFoundException("File length is 0")
            }
            val msg = handler.obtainMessage(Dispatcher.What.DOWNLOAD_FINISH)
            msg.obj = requestContext.key
            handler.sendMessage(msg)
            "end download file".logD()
            return file
        }catch (e: Exception){
            e.printStackTrace()
            errorHandle()
            return null
        }
    }

    fun errorHandle(){
        val msg = handler.obtainMessage(Dispatcher.What.DOWNLOAD_ERROR)
        msg.obj = requestContext.key
        handler.sendMessage(msg)
    }
}