package com.goyourfly.vincent

import android.graphics.Bitmap
import android.net.Uri
import com.goyourfly.vincent.common.KeyGenerator
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
        val target:Target,
        val placeholderId:Int,
        val errorId:Int,
        val keyGenerator: KeyGenerator){

    val key:String by lazy { keyGenerator.generate(uri.toString(),target) }
    val keyForCache:String = keyGenerator.generate(uri.toString())
    var future:Future<Bitmap>? = null

}
