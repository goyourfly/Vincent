package com.goyourfly.vincent.app

import android.util.Log
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

/**
 * Created by Administrator on 2017/6/18 0018.
 */
object DataProvider {

    interface ImageCallback{
        fun onSuccess(data:List<String>)
        fun onFailed(e:IOException)
    }

    val URL_SEARCH_IMG = "https://api.cognitive.microsoft.com/bing/v5.0/images/search"
    private val client = OkHttpClient()
    fun fetchImages(word:String,callback: DataProvider.ImageCallback) {
        val request = Request.Builder()
                .url("$URL_SEARCH_IMG?q=$word&mkt=en-us HTTP/1.1")
                .addHeader("Ocp-Apim-Subscription-Key", "e2618f38429e4a07a9399a0066159f5f")
                .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                callback.onFailed(e)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful()) throw IOException("Unexpected code " + response)
                val result = response.body()!!.string()
                val resultObj = Gson().fromJson(result, SearchBase::class.java)
                val list = mutableListOf<String>()
                for (data in resultObj.value) {
                    list.add(data.contentUrl)
//                    list.add(data.thumbnailUrl)
                }
                callback.onSuccess(list)
            }
        })
    }
}