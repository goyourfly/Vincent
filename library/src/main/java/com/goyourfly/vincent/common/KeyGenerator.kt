package com.goyourfly.vincent.common

import com.goyourfly.vincent.Target

/**
 * Created by gaoyufei on 2017/5/31.
 */

interface KeyGenerator{
    fun generate(str:String,target:Target):String
}
