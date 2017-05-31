package com.goyourfly.vincent

import android.graphics.Bitmap
import android.net.Uri
import java.util.concurrent.Future

/**
 * Created by gaoyufei on 2017/5/31.
 */
data class RequestInfo(
        var key:String?,
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
        var future: Future<Bitmap>?)
