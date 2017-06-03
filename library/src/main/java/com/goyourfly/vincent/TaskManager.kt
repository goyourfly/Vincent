package com.goyourfly.vincent

import com.goyourfly.vincent.common.logD
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by gaoyufei on 2017/6/3.
 */
class TaskManager {
    val request_list = ConcurrentHashMap<String, RequestContext>()
    val target_key_list = ConcurrentHashMap<String, String>()


    fun put(key: String, requestContext: RequestContext) {
        remove(key)
        removeByTargetId(requestContext.target.getId())
        request_list.put(key, requestContext)
        target_key_list.put(requestContext.target.getId(), key)
    }

    fun get(key: String) = request_list.get(key)

    fun getByTargetId(targetId: String): RequestContext? {
        return request_list.get(target_key_list.get(targetId))
    }

    fun remove(key: String): RequestContext? {
        val requestInfo = request_list.remove(key) ?: return null
        target_key_list.remove(requestInfo.target.getId())
        return requestInfo
    }

    fun removeByTargetId(targetId: String):RequestContext? {
        val key = target_key_list.remove(targetId) ?: return null
        return request_list.remove(key)
    }

    fun containsKey(key: String) = request_list.containsKey(key)

    fun containsTargetId(targetId: String):Boolean{
        val contain = target_key_list.containsKey(targetId)
        return contain
    }

    fun size() = request_list.size

}