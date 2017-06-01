package com.goyourfly.vincent

import android.accounts.NetworkErrorException
import android.graphics.Bitmap
import android.net.Uri
import android.nfc.Tag
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Message
import com.goyourfly.vincent.cache.CacheManager
import com.goyourfly.vincent.cache.CacheSeed
import com.goyourfly.vincent.common.KeyGenerator
import com.goyourfly.vincent.common.logD
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Created by gaoyufei on 2017/5/31.
 *
 */
class Dispatcher(val keyGenerator: KeyGenerator,
                 val memoryCache:CacheManager<CacheSeed>,
                 val fileCache:CacheManager<CacheSeed>){

    object What{
        val SUBMIT = 1
        val CANCEL = 2

        val THIEF_COMPLETE = 3
        val THIEF_CANCEL = 4
        val THIEF_ERROR = 5
    }

    /**
     * Handler thread , receive the all action
     * of Vincent called
     */
    val handlerThread = DispatchHandlerThread("Vincent-Dispatcher")
    val executor = Executors.newFixedThreadPool(4)
    val executorManager = ConcurrentHashMap<String,RequestInfo>()
    val networkHandler = OkHttpRequestHandler()
    var handler:Handler? =  null
    var handlerMain = Handler(Looper.getMainLooper())

    init {
        handlerThread.start()
        handler = DispatcherHandler(handlerThread.looper,executor,executorManager,networkHandler,handlerMain)
    }

    class DispatchHandlerThread(name:String) : HandlerThread(name) {

    }

    /**
     * 接收消息，如果是请求加载图片
     * 则下载图片
     * 如果是取消，则取消下载
     */
    class DispatcherHandler(looper: Looper,
                            val executor: ExecutorService,
                            val executorManager: ConcurrentHashMap<String,RequestInfo>,
                            val networkHandler:RequestHandler<Bitmap>,
                            val handlerMain:Handler) : Handler(looper) {
        override fun handleMessage(msg: Message?) {
            "handleMsg:${msg?.what}".logD()
            when(msg?.what){
                What.SUBMIT ->{
                    val requestInfo = msg.obj as RequestInfo
                    val future = executor.submit(BitmapThief(this,networkHandler,requestInfo))
                    requestInfo.future = future
                    executorManager.put(requestInfo.key!!,requestInfo)
                }

                What.CANCEL ->{
                    val key = msg.obj as String
                    if(executorManager.contains(key)){
                        val future = executorManager.remove(key)?.future
                        future?.cancel(true)
                    }
                }

                What.THIEF_COMPLETE ->{
                    val key = msg.obj as String
                    "thiefComplete:Key$key,${executorManager.contains(key)}".logD()
                    if(executorManager.containsKey(key)){
                        val requestInfo = executorManager.remove(key)
                        val bitmap = requestInfo?.future?.get()
                        handlerMain.post { requestInfo!!.target.onComplete(bitmap!!) }
                    }
                }

                What.THIEF_ERROR -> {
                    val key = msg.obj as String
                    "thiefError:Key$key,${executorManager.contains(key)}".logD()
                    if(executorManager.containsKey(key)){
                        val requestInfo = executorManager.remove(key)
                        handlerMain.post { requestInfo!!.target.onError(NetworkErrorException()) }
                    }
                }
            }
        }
    }


    fun dispatchSubmit(requestInfo: RequestInfo){
        requestInfo.key = keyGenerator.generate(requestInfo.uri.toString(),requestInfo.target)
        val msg = handler?.obtainMessage(What.SUBMIT)
        msg?.obj = requestInfo
        handler?.sendMessage(msg)
    }

    fun dispatchCancel(uri: Uri,target: Target){
        val msg = handler?.obtainMessage(What.CANCEL)
        msg?.obj = keyGenerator.generate(uri.toString(),target)
        handler?.sendMessage(msg)
    }



}