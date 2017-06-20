package com.goyourfly.vincent

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.SystemClock
import com.goyourfly.vincent.decoder.Decoder
import com.goyourfly.vincent.decoder.StreamProvider
import java.util.concurrent.Callable

/**
 * Created by Administrator on 2017/6/1 0001.
 */
class RunDrawableMaker(val handler: Handler,
                       val key: String,
                       val streamProvider: StreamProvider,
                       val requestContext: RequestContext,
                       val imageDecoder: List<Decoder>) : Callable<Drawable> {
    override fun call(): Drawable? {
        val debugTime = SystemClock.elapsedRealtime()
        var drawable: Drawable? = imageDecoder
                .firstOrNull { it.canDecode(streamProvider) }
                ?.decode(requestContext.context
                        , streamProvider
                        , requestContext.scale
                        , requestContext.fit
                        , requestContext.resizeWidth
                        , requestContext.resizeHeight)
        // Decode 图片
        if (drawable == null) {
            errorMsg()
            requestContext.debugInfo.decodeTime = SystemClock.elapsedRealtime() - debugTime
            return null
        }

        // Transform 图片
        if (drawable is BitmapDrawable
                && drawable.bitmap != null) {
            var bitmap = drawable.bitmap
            for (transform in requestContext.transforms) {
                bitmap = transform.transform(bitmap)
            }
            if (bitmap != drawable.bitmap) {
                drawable = BitmapDrawable(bitmap)
            }
        }
        val msg = handler.obtainMessage(Dispatcher.What.BITMAP_DECODE_COMPLETE)
        msg.obj = key
        handler.sendMessage(msg)
        requestContext.debugInfo.decodeTime = SystemClock.elapsedRealtime() - debugTime
        return drawable
    }


    fun errorMsg() {
        val msg = handler.obtainMessage(Dispatcher.What.BITMAP_DECODE_ERROR)
        msg.obj = requestContext.key
        handler.sendMessage(msg)
    }
}
