package com.hosigus.tools.utils

import android.util.Log

/**
 * Created by Hosigus on 2018/6/6.
 * LogUtil.
 */
var LOG_LEVEL: Level =Level.NOTHING

enum class Level {
    NOTHING,VERBOSE,DEBUG,INFO,WARN,ERROR,ALL
}

/**
 * you can ignore the second param , it would be your global setting
 */
class MyLog(private val tag: String, var level: Level = LOG_LEVEL) {

    fun v(msg: String) {
        if (level >= Level.VERBOSE) {
            Log.v(tag, msg)
        }
    }

    fun d(msg: String) {
        if (level >= Level.DEBUG) {
            Log.d(tag, msg)
        }
    }

    fun i(msg: String) {
        if (level >= Level.INFO) {
            Log.i(tag, msg)
        }
    }

    fun w(msg: String) {
        if (level >= Level.WARN) {
            Log.w(tag, msg)
        }
    }

    fun e(msg: String) {
        if (level >= Level.ERROR) {
            Log.e(tag, msg)
        }
    }

}