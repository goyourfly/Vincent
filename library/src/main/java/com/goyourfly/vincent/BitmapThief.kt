package com.goyourfly.vincent

import android.graphics.Bitmap
import android.os.Handler
import com.goyourfly.vincent.common.logD
import com.goyourfly.vincent.common.logE
import java.util.concurrent.Callable

/**
 * Created by gaoyufei on 2017/5/31.
 */

class BitmapThief(val handler:Handler,
                  val requestHandler: RequestHandler<Bitmap>,
                  val requestInfo: RequestInfo):Callable<Bitmap>{
    override fun call(): Bitmap? {
        "start get bitmap".logD()
        try {
            val bitmap = requestHandler.fetchSync(requestInfo.uri)
            val msg = handler.obtainMessage(Dispatcher.What.THIEF_COMPLETE)
            msg.obj = requestInfo.key
            handler.sendMessage(msg)
            "end get bitmap".logD()
            return bitmap
        }catch (e: Exception){
            e.message?.logE()
            val msg = handler.obtainMessage(Dispatcher.What.THIEF_ERROR)
            msg.obj = requestInfo.key
            handler.sendMessage(msg)
            return null
        }
    }
}