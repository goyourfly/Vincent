package com.goyourfly.vincent

import android.os.Handler
import com.goyourfly.vincent.common.logD
import com.goyourfly.vincent.common.logE
import java.io.File
import java.io.FileNotFoundException
import java.util.concurrent.Callable

/**
 * Created by gaoyufei on 2017/5/31.
 */

class RunFileDownloader(val handler:Handler,
                        val requestHandler: RequestHandler<File>,
                        val requestInfo: RequestInfo):Callable<File>{
    override fun call(): File? {
        "start download file".logD()
        try {
            val file = requestHandler.fetchSync(requestInfo.keyForCache,requestInfo.uri)
            if(file == null){
                throw FileNotFoundException("File not found")
            }else if(file.length() == 0L){
                throw FileNotFoundException("File length is 0")
            }
            val msg = handler.obtainMessage(Dispatcher.What.DOWNLOAD_FINISH)
            msg.obj = requestInfo.key
            handler.sendMessage(msg)
            "end download file".logD()
            return file
        }catch (e: Exception){
            e.printStackTrace()
            val msg = handler.obtainMessage(Dispatcher.What.DOWNLOAD_ERROR)
            msg.obj = requestInfo.key
            handler.sendMessage(msg)
            return null
        }
    }
}