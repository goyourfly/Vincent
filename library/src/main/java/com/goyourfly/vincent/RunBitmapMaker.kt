package com.goyourfly.vincent

import android.graphics.Bitmap
import android.os.Handler
import com.goyourfly.vincent.common.logD
import com.goyourfly.vincent.decoder.DecodeManager
import java.io.File
import java.util.concurrent.Callable

/**
 * Created by Administrator on 2017/6/1 0001.
 */
class RunBitmapMaker(val handler:Handler,val key:String, val file:File):Callable<Bitmap>{
    override fun call(): Bitmap? {
        "RunBitmapMaker--->$key".logD()
        val bitmap = DecodeManager.decode(file)
        val msg = handler.obtainMessage(Dispatcher.What.FILE_LOAD_COMPLETE)
        msg.obj = key
        handler.sendMessage(msg)
        "RunBitmapMaker end--->$key".logD()
        return bitmap
    }
}
