package com.goyourfly.vincent.common
import com.goyourfly.vincent.target.Target

/**
 * Created by gaoyufei on 2017/5/31.
 */
class HashCodeGenerator:KeyGenerator{

    val prefix = "Vincent"
    override fun generate(str: String,target: Target): String {
        if(str.trim().isEmpty()){
            throw NullPointerException("input str should not null")
        }
        return "$prefix-${str.hashCode()}-${target.targetId}"
    }
    override fun generate(str: String): String {
        return "$prefix-${str.hashCode()}.jpg"
    }
}
