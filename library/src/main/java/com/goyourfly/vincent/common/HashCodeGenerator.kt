package com.goyourfly.vincent.common

/**
 * Created by gaoyufei on 2017/5/31.
 */
class HashCodeGenerator:KeyGenerator{
    val prefix = "Vincent-"
    override fun generate(str: String): String {
        if(str.trim().isEmpty()){
            throw NullPointerException("input str should not null")
        }
        return prefix + str.hashCode()
    }
}
