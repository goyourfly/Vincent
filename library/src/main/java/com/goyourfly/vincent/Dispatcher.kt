package com.goyourfly.vincent

import android.accounts.NetworkErrorException
import android.graphics.Bitmap
import android.net.Uri
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Message
import com.goyourfly.vincent.cache.CacheManager
import com.goyourfly.vincent.cache.CacheSeed
import com.goyourfly.vincent.common.KeyGenerator
import com.goyourfly.vincent.common.logD
import java.io.File
import java.util.concurrent.*

/**
 * Created by gaoyufei on 2017/5/31.
 *
 */
class Dispatcher(val keyGenerator: KeyGenerator,
                 val memoryCache: CacheManager<Bitmap>,
                 val fileCache: CacheManager<File>) {

    object What {
        val SUBMIT = 1
        val CANCEL = 2

        val DOWNLOAD_FINISH = 4
        val DOWNLOAD_ERROR = 5

        val MEMORY_LOAD_COMPLETE = 6

        val FILE_LOAD_COMPLETE = 7
        val FILE_LOAD_ERROR = 8
    }

    /**
     * Handler thread , receive the all action
     * of Vincent called
     */
    val handlerThread = DispatchHandlerThread("Vincent-Dispatcher")
    val executor = Executors.newFixedThreadPool(4)
    val executorBitmap = Executors.newSingleThreadExecutor()
    val executorManager = ConcurrentHashMap<String, RequestInfo>()
    val networkHandler = HttpRequestHandler(fileCache)
    var handler: Handler? = null
    var handlerMain = Handler(Looper.getMainLooper())

    init {
        handlerThread.start()
        handler = DispatcherHandler(handlerThread.looper,
                executor,
                executorBitmap,
                executorManager,
                networkHandler,
                memoryCache,
                fileCache,
                handlerMain)
    }

    class DispatchHandlerThread(name: String) : HandlerThread(name) {

    }

    /**
     * 接收消息，如果是请求加载图片
     * 则下载图片
     * 如果是取消，则取消下载
     */
    class DispatcherHandler(looper: Looper,
                            val executor: ExecutorService,
                            val executorBitmap: ExecutorService,
                            val executorManager: ConcurrentHashMap<String, RequestInfo>,
                            val networkHandler: RequestHandler<File>,
                            val memoryCache: CacheManager<Bitmap>,
                            val fileCache: CacheManager<File>,
                            val handlerMain: Handler) : Handler(looper) {
        override fun handleMessage(msg: Message?) {
            "handleMsg:${msg?.what} + ${msg?.obj}".logD()
            when (msg?.what) {
                What.SUBMIT -> {
                    val requestInfo = msg.obj as RequestInfo
                    val key = requestInfo.key
                    executorManager.put(key, requestInfo)
                    // 首先判断内存的缓存中是否存在该图片
                    if (memoryCache.contain(requestInfo.keyForCache)) {
                        sendMessage(obtainMessage(What.MEMORY_LOAD_COMPLETE, key))
                        return
                    }
                    // if local cache contain file
                    if (fileCache.contain(requestInfo.keyForCache)) {
                        sendMessage(obtainMessage(What.DOWNLOAD_FINISH, key))
                        return
                    }
                    executor.submit(RunFileDownloader(this, networkHandler, requestInfo))
                }

                What.CANCEL -> {
                    val key = msg.obj as String
                    if (executorManager.containsKey(key)) {
                        val future = executorManager.remove(key)?.future
                        future?.cancel(true)
                    }
                }


                What.DOWNLOAD_FINISH -> {
                    val key = msg.obj as String
                    if (executorManager.containsKey(key)) {
                        val requestInfo = executorManager.get(key)!!
                        //TODO 判断本地文件系统是否缓存该图
                        val file = fileCache.get(requestInfo.keyForCache)
                        requestInfo.future = executorBitmap.submit(RunBitmapMaker(this, key, file))
                    }
                }

                What.MEMORY_LOAD_COMPLETE -> {
                    val key = msg.obj as String
                    if (executorManager.containsKey(key)) {
                        val requestInfo = executorManager.remove(key)!!
                        val bitmap = memoryCache.get(requestInfo.keyForCache)
                        if (bitmap != null) {
                            handlerMain.post { requestInfo.target.onComplete(bitmap) }
                        }
                    }
                }

                What.FILE_LOAD_COMPLETE -> {
                    val key = msg.obj as String
                    if (executorManager.containsKey(key)) {
                        val requestInfo = executorManager.remove(key)
                        val bitmap = requestInfo?.future?.get()
                        if (bitmap != null) {
                            "handleMsg:7 set image".logD()
                            memoryCache.set(requestInfo.keyForCache, bitmap)
                            handlerMain.post { requestInfo.target.onComplete(bitmap) }
                        }
                    }
                }

                What.FILE_LOAD_ERROR or What.DOWNLOAD_ERROR -> {
                    val key = msg.obj as String
                    if (executorManager.containsKey(key)) {
                        val requestInfo = executorManager.remove(key)
                        handlerMain.post { requestInfo!!.target.onError(NetworkErrorException()) }
                    }
                }
            }
        }
    }


    fun dispatchSubmit(requestInfo: RequestInfo) {
        val msg = handler?.obtainMessage(What.SUBMIT)
        msg?.obj = requestInfo
        handler?.sendMessage(msg)
    }

    fun dispatchCancel(uri: Uri, target: Target) {
        val msg = handler?.obtainMessage(What.CANCEL)
        msg?.obj = keyGenerator.generate(uri.toString(), target)
        handler?.sendMessage(msg)
    }


}