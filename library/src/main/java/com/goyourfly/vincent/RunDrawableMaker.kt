package com.goyourfly.vincent

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Handler
import com.goyourfly.vincent.common.logD
import com.goyourfly.vincent.decoder.DecodeManager
import java.io.File
import java.util.concurrent.Callable

/**
 * Created by Administrator on 2017/6/1 0001.
 */
class RunDrawableMaker(val handler:Handler, val key:String, val file:File,val requestContext: RequestContext):Callable<Drawable>{
    override fun call(): Drawable? {
        "RunDrawableMaker--->$key".logD()
        var drawable = DecodeManager.decode(file,requestContext.resizeWidth,requestContext.resizeHeight)
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
        val msg = handler.obtainMessage(Dispatcher.What.FILE_LOAD_COMPLETE)
        msg.obj = key
        handler.sendMessage(msg)
        "RunDrawableMaker end--->$key".logD()
        return drawable
    }
}
