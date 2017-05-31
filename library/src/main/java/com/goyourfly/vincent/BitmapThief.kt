package com.goyourfly.vincent

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import java.util.concurrent.Callable

/**
 * Created by gaoyufei on 2017/5/31.
 */

class BitmapThief(val handler:Handler,val requestHandler: RequestHandler<Bitmap>,val requestInfo: RequestInfo):Callable<Bitmap>{
    override fun call(): Bitmap {
        return requestHandler.fetchSync(requestInfo.uri)
    }
}