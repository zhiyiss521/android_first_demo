package com.zhiyi.android_first_demo.util

import com.zhiyi.android_first_demo.BuildConfig
import timber.log.Timber

interface ILoggerAdapter {
    fun setup()
    fun d(msg: String)
    fun i(msg: String)
    // 这样可以打印异常的堆栈信息应该是
    fun e(msg: String, tr: Throwable? = null)
}

class TimberLogAdapter(private val tag: String) : ILoggerAdapter {

    override fun setup() {
        Timber.plant(Timber.DebugTree())
    }

    override fun d(msg: String) {
        Timber.tag(tag).d(msg)
    }

    override fun i(msg: String) {
        Timber.tag(tag).i(msg)
    }

    override fun e(msg: String, tr: Throwable?) {
        Timber.tag(tag).e(tr, msg)
    }
}

object LogUtil {

    private const val UNIFIED_TAG = "zhiyi"

    private val isDebug = BuildConfig.DEBUG

    private val logger: ILoggerAdapter = TimberLogAdapter(UNIFIED_TAG)

    // 因为这玩意是个单例，所以会自动调用这里的方法
    init {
        if (isDebug) {
            logger.setup()
        }
    }

    fun d(msg: String) {
        if (isDebug) logger.d(msg)
    }

    fun i(msg: String) {
        if (isDebug) logger.i(msg)
    }

    fun e(msg: String, tr: Throwable? = null) {
        if (isDebug) logger.e(msg, tr)
    }
}