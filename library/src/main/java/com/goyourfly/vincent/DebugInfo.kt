package com.goyourfly.vincent

/**
 * Created by gaoyufei on 2017/6/20.
 */
data class DebugInfo(var from: LoadFrom ?,
                     var mesureViewTime: Long = -1,
                     var downloadTime: Long = -1,
                     var decodeTime: Long = -1)
