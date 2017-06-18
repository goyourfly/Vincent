package com.goyourfly.vincent

import android.accounts.NetworkErrorException
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Message
import com.goyourfly.vincent.cache.CacheManager
import com.goyourfly.vincent.common.logD
import com.goyourfly.vincent.handle.FileRequestHandler
import com.goyourfly.vincent.handle.HttpRequestHandler
import com.goyourfly.vincent.handle.RequestHandler
import com.goyourfly.vincent.handle.ResourceRequestHandler
import com.goyourfly.vincent.target.ImageTarget
import java.io.File
import java.util.concurrent.*

/**
 * Created by gaoyufei on 2017/5/31.
 *
 */
class Dispatcher(
        val res: Resources,
        val memoryCache: CacheManager<Drawable>,
        val fileCache: CacheManager<File>) {

    object What {
        val SUBMIT = 1
        val CANCEL = 2

        val DOWNLOAD_FINISH = 4
        val DOWNLOAD_ERROR = 5

        val FILE_LOAD_COMPLETE = 7
        val FILE_LOAD_ERROR = 8
        val TRIM_FILE_TO_SIZE = 9

        fun getName(i: Int): String {
            return when (i) {
                SUBMIT -> "1:SUBMIT"
                CANCEL -> "2:CANCEL"
                DOWNLOAD_FINISH -> "4:DOWNLOAD_FINISH"
                DOWNLOAD_ERROR -> "5:DOWNLOAD_ERROR"
                FILE_LOAD_COMPLETE -> "7:FILE_LOAD_COMPLETE"
                FILE_LOAD_ERROR -> "8:FILE_LOAD_ERROR"
                TRIM_FILE_TO_SIZE -> "9:TRIM_FILE_TO_SIZE"
                else -> {
                    "$i:UNKNOWN"
                }
            }
        }
    }

    /**
     * Handler thread , receive the all action
     * of Vincent called
     */
    val handlerThread = DispatchHandlerThread("Vincent-Dispatcher")
    val executor = Executors.newFixedThreadPool(8)
    val executorBitmap = Executors.newSingleThreadExecutor()
    val executorManager = TaskManager()
    val networkHandler = arrayListOf<RequestHandler<File>>(HttpRequestHandler(fileCache), FileRequestHandler(fileCache), ResourceRequestHandler(fileCache, res))
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
                            val taskManager: TaskManager,
                            val networkHandler: ArrayList<RequestHandler<File>>,
                            val memoryCache: CacheManager<Drawable>,
                            val fileCache: CacheManager<File>,
                            val handlerMain: Handler) : Handler(looper) {

        fun getRequestHandler(uri: Uri): RequestHandler<File>? {
            for (requestHandler in networkHandler) {
                if (requestHandler.canHandle(uri))
                    return requestHandler
            }
            return null
        }

        override fun handleMessage(msg: Message) {
            "Biu Biu, i receive a msg:${What.getName(msg.what)}".logD()
            when (msg.what) {
                What.SUBMIT -> {
                    val requestInfo = msg.obj as RequestContext
                    val key = requestInfo.key

                    taskManager.put(key, requestInfo)
                    // 判断本地文件系统是否有该图片
                    if (fileCache.contain(requestInfo.keyForFileCache)) {
                        requestInfo.setFrom(LoadFrom.FROM_SD_CARD)
                        sendMessage(obtainMessage(What.DOWNLOAD_FINISH, key))
                        return
                    }
                    requestInfo.setFrom(LoadFrom.FROM_NET)
                    // 触发清空缓存
                    removeMessages(What.TRIM_FILE_TO_SIZE)
                    sendMessageDelayed(obtainMessage(What.TRIM_FILE_TO_SIZE), 1000 * 5)
                    // 如果都没有，去网络下载
                    requestInfo.futureDownload = executor.submit(RunFileDownloader(this, getRequestHandler(requestInfo.uri), requestInfo))
                }

                What.CANCEL -> {
                    val targetId = msg.obj as String
                    if (taskManager.containsTargetId(targetId)) {
                        val requestInfo = taskManager.getByTargetId(targetId)
                        requestInfo?.cancel()
                    }
                }


                What.DOWNLOAD_FINISH -> {
                    val key = msg.obj as String
                    if (taskManager.containsKey(key)) {
                        val requestInfo = taskManager.get(key)!!
                        measureViewSize(requestInfo)
                        val file = fileCache.get(requestInfo.keyForFileCache)!!
                        requestInfo.future = executorBitmap.submit(RunDrawableMaker(this, key, file, requestInfo))
                    }
                }

                What.FILE_LOAD_COMPLETE -> {
                    val key = msg.obj as String
                    if (taskManager.containsKey(key)) {
                        val requestInfo = taskManager.remove(key)
                        val bitmap = requestInfo?.future?.get()
                        if (bitmap != null) {
                            memoryCache.set(requestInfo.keyForMemoryCache, bitmap)
                            handlerMain.post { requestInfo.target.onComplete(bitmap) }
                        }
                    }
                }

            // 每10秒清空一次缓存
                What.TRIM_FILE_TO_SIZE -> {
                    // 如果当前有任务，不清空
                    if (taskManager.size() == 0) {
                        fileCache.trimToSize()
                    } else {
                        sendMessageDelayed(obtainMessage(What.TRIM_FILE_TO_SIZE), 1000 * 5)
                    }
                }

                else -> {
                    val key = msg.obj as String
                    if (taskManager.containsKey(key)) {
                        val requestInfo = taskManager.remove(key)

                        handlerMain.post { requestInfo!!.target.onError(requestInfo.loadDrawable(requestInfo.errorId), NetworkErrorException()) }
                    }
                }
            }
        }

        fun measureViewSize(requestInfo: RequestContext?) {
            if (requestInfo == null)
                return
            // 获取图片宽高
            if (requestInfo.fit && requestInfo.target is ImageTarget) {
                val time = System.currentTimeMillis()
                var retry = 0
                while (retry < 40) {
                    if (requestInfo.resizeWidth != 0
                            || requestInfo.resizeHeight != 0)
                        break
                    requestInfo.resizeWidth = requestInfo.target.imageView.measuredWidth
                    requestInfo.resizeHeight = requestInfo.target.imageView.measuredHeight
                    retry++
                    Thread.sleep(50)
                }
                "InitViewSizeCostTime:${System.currentTimeMillis() - time}".logD()
            }
        }
    }


    fun dispatchSubmit(request: RequestContext) {
        val targetId = request.target.getId()
        "Target id:$targetId".logD()
        if (executorManager.containsTargetId(targetId)) {
            // 判断当前target是否有任务
            val requestInfoOld = executorManager.getByTargetId(targetId)
            if (requestInfoOld != null) {
                "Target[${targetId}] already have a task".logD()
                // 如果当前有任务，但是任务和之前的一致，则返回，让老任务继续
                if (requestInfoOld == request) {
                    return
                }
                "Target[$targetId] old task not equals new task, so cancel before".logD()
                // 否则，取消老任务，执行新的任务
                requestInfoOld.cancel()
                executorManager.removeByTargetId(targetId)
            }
        }
        // 首先判断内存的缓存中是否存在该图片
        if (memoryCache.contain(request.keyForMemoryCache)) {
            executorManager.remove(request.key)
            val bitmap = memoryCache.get(request.keyForMemoryCache)
            if (bitmap != null) {
                request.setFrom(LoadFrom.FROM_MEMORY)
                request.target.onComplete(bitmap)
            }
            return
        } else {
            request.target.onInit(request.loadDrawable(request.placeholderId))
        }


        val msg = handler?.obtainMessage(What.SUBMIT)
        msg?.obj = request
        handler?.sendMessage(msg)
    }
}