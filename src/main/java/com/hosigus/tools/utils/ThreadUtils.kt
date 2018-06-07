package com.hosigus.tools.utils

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Created by Hosigus on 2018/6/6.
 */
object ThreadUtils {

    private var mExecutor: ExecutorService= Executors.newCachedThreadPool()
    private var mainHandler: Handler= Handler(Looper.getMainLooper())

    fun execute(command: Runnable) {
        mExecutor.execute(command)
    }

    fun <T> submit(task: Callable<T>) {
        mExecutor.submit(task)
    }

    fun <T> submit(task: Runnable, result: T) {
        mExecutor.submit(task, result)
    }

    fun post(r: Runnable) {
        mainHandler.post(r)
    }

    fun postDelay(r: Runnable, delayMillis: Long) {
        mainHandler.postDelayed(r, delayMillis)
    }

    fun execute(command: () -> Unit) {
        mExecutor.execute(command)
    }

    fun <T> submit(task: () -> Unit, result: T) {
        mExecutor.submit(task, result)
    }

    fun post(r: () -> Unit) {
        mainHandler.post(r)
    }

    fun postDelay(r: () -> Unit, delayMillis: Long) {
        mainHandler.postDelayed(r, delayMillis)
    }
}