package com.goyourfly.vincent

import android.graphics.Bitmap
import android.net.Uri
import com.goyourfly.vincent.common.KeyGenerator
import com.goyourfly.vincent.target.Target
import java.util.concurrent.Future

/**
 * Created by gaoyufei on 2017/5/31.
 */
data class RequestInfo(
        val uri: Uri,
        val preUri:Uri,
        val resizeWidth:Int,
        val resizeHeight:Int,
        val scale:Scale,
        val cache:Cache,
        val priority: Priority,
        val target: Target,
        val placeholderId:Int,
        val errorId:Int,
        val keyGenerator: KeyGenerator){

    /**
     * 这个ID绑定了url和target，保证了完全的唯一
     */
    val key:String by lazy { keyGenerator.generate(uri.toString(),target) }
    /**
     * 这个ID只绑定url，没有区别target
     */
    val keyForCache:String = keyGenerator.generate(uri.toString())
    var future:Future<Bitmap>? = null

}
