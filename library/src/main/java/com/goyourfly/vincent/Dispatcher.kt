package com.goyourfly.vincent

import android.accounts.NetworkErrorException
import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.*
import com.goyourfly.vincent.cache.CacheManager
import com.goyourfly.vincent.cache.FileLruCacheManager
import com.goyourfly.vincent.common.logD
import com.goyourfly.vincent.decoder.BitmapDecoder
import com.goyourfly.vincent.decoder.Decoder
import com.goyourfly.vincent.decoder.GifDecoder
import com.goyourfly.vincent.decoder.StreamProvider
import com.goyourfly.vincent.handle.*
import com.goyourfly.vincent.target.ImageTarget
import java.io.InputStream
import java.util.concurrent.*

/**
 * Created by gaoyufei on 2017/5/31.
 *
 */
class Dispatcher(
        val context: Context,
        val res: Resources,
        val memoryCache: CacheManager<Drawable>,
        val fileCache: CacheManager<InputStream>) {

    object What {
        val SUBMIT = 1
        val CANCEL = 2

        val DOWNLOAD_FINISH = 4
        val DOWNLOAD_ERROR = 5

        val BITMAP_DECODE_COMPLETE = 7
        val BITMAP_DECODE_ERROR = 8

        fun getName(i: Int): String {
            return when (i) {
                SUBMIT -> "1:SUBMIT"
                CANCEL -> "2:CANCEL"
                DOWNLOAD_FINISH -> "4:DOWNLOAD_FINISH"
                DOWNLOAD_ERROR -> "5:DOWNLOAD_ERROR"
                BITMAP_DECODE_COMPLETE -> "7:BITMAP_DECODE_COMPLETE"
                BITMAP_DECODE_ERROR -> "8:BITMAP_DECODE_ERROR"
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
    val executorDownloader = Executors.newFixedThreadPool(4)
    val executorDecoder = Executors.newSingleThreadExecutor()
    val executorManager = TaskManager()
    val networkHandler = arrayListOf(HttpRequestHandler(fileCache),
            FileRequestHandler(fileCache),
            ContentHandler(context,fileCache),
            ResourceRequestHandler(fileCache, res))
    val imageDecoder = arrayListOf(BitmapDecoder(), GifDecoder())
    var handler: Handler? = null
    var handlerMain = Handler(Looper.getMainLooper())

    init {
        handlerThread.start()
        handler = DispatcherHandler(handlerThread.looper,
                executorDownloader,
                executorDecoder,
                executorManager,
                networkHandler,
                memoryCache,
                fileCache,
                imageDecoder,
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
                            val networkHandler: ArrayList<RequestHandler<Boolean>>,
                            val memoryCache: CacheManager<Drawable>,
                            val fileCache: CacheManager<InputStream>,
                            val imageDecoder: List<Decoder>,
                            val handlerMain: Handler) : Handler(looper) {

        fun getRequestHandler(uri: Uri): RequestHandler<Boolean>? {
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
                    if (fileCache.exist(requestInfo.keyForCache)) {
                        requestInfo.debugInfo.from = LoadFrom.FROM_SD_CARD
                        sendMessage(obtainMessage(What.DOWNLOAD_FINISH, key))
                        return
                    }
                    requestInfo.debugInfo.from = LoadFrom.FROM_NET
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
                        // 计算View尺寸
                        measureViewSize(requestInfo)
                        requestInfo.futureDecode = executorBitmap.submit(RunDrawableMaker(this,
                                key,
                                StreamProvider(requestInfo.keyForCache, fileCache as FileLruCacheManager),
                                requestInfo,
                                imageDecoder))
                    } else {
                        val errMsg = obtainMessage(What.BITMAP_DECODE_ERROR)
                        errMsg.obj = key
                        sendMessage(errMsg)
                    }
                }

                What.BITMAP_DECODE_COMPLETE -> {
                    val key = msg.obj as String
                    if (taskManager.containsKey(key)) {
                        val requestInfo = taskManager.remove(key)
                        val drawable = requestInfo?.futureDecode?.get()
                        if (drawable != null) {
                            memoryCache.write(requestInfo.keyForCache, drawable)
                            handlerMain.post { requestInfo.target.onComplete(drawable) }
                        }
                        "DebugInfo->${requestInfo?.debugInfo}".logD()
                    } else {
                        val errMsg = obtainMessage(What.BITMAP_DECODE_ERROR)
                        errMsg.obj = key
                        sendMessage(errMsg)
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
            val debugTime = SystemClock.elapsedRealtime()
            // 获取图片宽高
            if ((requestInfo.fit ||
                    (requestInfo.resizeWidth == 0 && requestInfo.resizeHeight == 0))
                    && requestInfo.target is ImageTarget) {
                var retry = 0
                while (retry < 40) {
                    requestInfo.resizeWidth = requestInfo.target.imageView.measuredWidth
                    requestInfo.resizeHeight = requestInfo.target.imageView.measuredHeight
                    retry++
                    if (requestInfo.resizeWidth != 0
                            || requestInfo.resizeHeight != 0)
                        break
                    Thread.sleep(50)
                }
            }
            requestInfo.debugInfo.mesureViewTime = SystemClock.elapsedRealtime() - debugTime
        }
    }


    fun dispatchSubmit(request: RequestContext) {
        val targetId = request.target.getId()
        "Target id:$targetId".logD()
        if (executorManager.containsTargetId(targetId)) {
            // 判断当前target是否有任务
            val requestInfoOld = executorManager.getByTargetId(targetId)
            if (requestInfoOld != null) {
                // 如果当前有任务，但是任务和之前的一致，则返回，让老任务继续
                if (requestInfoOld == request) {
                    return
                }
                // 否则，取消老任务，执行新的任务
                requestInfoOld.cancel()
                executorManager.removeByTargetId(targetId)
            }
        }

        // 首先判断内存的缓存中是否存在该图片
        if (memoryCache.exist(request.keyForCache)) {
            executorManager.remove(request.key)
            val bitmap = memoryCache.read(request.keyForCache)
            if (bitmap != null) {
                request.debugInfo.from = LoadFrom.FROM_MEMORY
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