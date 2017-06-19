package com.goyourfly.vincent

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Message
import com.goyourfly.vincent.common.logD
import com.goyourfly.vincent.decoder.DecodeManager
import com.goyourfly.vincent.decoder.StreamProvider
import java.io.File
import java.io.InputStream
import java.util.concurrent.Callable

/**
 * Created by Administrator on 2017/6/1 0001.
 */
class RunDrawableMaker(val handler:Handler, val key:String, val streamProvider: StreamProvider,val requestContext: RequestContext):Callable<Drawable>{
    override fun call(): Drawable? {
        var drawable = DecodeManager.decode(streamProvider,requestContext.resizeWidth,requestContext.resizeHeight)
        // 有时候可能需要Bitmap的转换
        if(drawable is BitmapDrawable
                && drawable.bitmap != null){
            var bitmap = drawable.bitmap
            for (transform in requestContext.transforms){
                bitmap = transform.transform(bitmap)
            }
            if(bitmap != drawable.bitmap){
                drawable = BitmapDrawable(bitmap)
            }
        }
        val msg:Message?
        if(drawable != null){
            msg = handler.obtainMessage(Dispatcher.What.BITMAP_DECODE_COMPLETE)
        }else{
            msg = handler.obtainMessage(Dispatcher.What.BITMAP_DECODE_ERROR)
        }
        msg.obj = key
        handler.sendMessage(msg)
        return drawable
    }
}
