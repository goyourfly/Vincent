package com.goyourfly.vincent

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import com.goyourfly.vincent.common.KeyGenerator
import com.goyourfly.vincent.common.centerCrop
import com.goyourfly.vincent.target.Target
import com.goyourfly.vincent.transform.Transform
import java.io.File
import java.util.concurrent.Future

/**
 * Created by gaoyufei on 2017/5/31.
 */
data class RequestContext(
        val context: Context,
        val uri: Uri,
        val fit:Boolean,
        var resizeWidth:Int,
        var resizeHeight:Int,
        val scale:Scale,
        val cache:Cache,
        val priority: Priority,
        val target: Target,
        val placeholderId:Int,
        val errorId:Int,
        val keyGenerator: KeyGenerator,
        val transforms:ArrayList<Transform>){

    /**
     * 这个ID绑定了url和target，保证了完全的唯一
     */
    val key:String by lazy { keyGenerator.generate(uri.toString(),target.getId()) }
    /**
     * 这个ID只绑定url，没有区别target
     */
    val keyForCache:String = keyGenerator.generate(uri.toString(),target.getId())

    var futureDownload:Future<File>? = null
    var future:Future<Drawable>? = null

    fun cancel(){
        futureDownload?.cancel(true)
        future?.cancel(true)
    }

    override fun equals(other: Any?): Boolean {
        if(other == null)
            return false
        if(other is RequestContext) {
            return uri == other.uri && target == other.target
        }
        return false
    }

    fun loadDrawable(id:Int):Drawable{
        val drawable = context.resources.getDrawable(id)
        return drawable
    }
}
