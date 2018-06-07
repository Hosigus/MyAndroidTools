package com.hosigus.tools.utils

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast

/**
 * Created by Hosigus on 2018/6/6.
 */

private var mContext: Context? = null
private var mToast: Toast? = null

fun setContext(context: Context) {
    mContext = context
}

fun show(text: String) {
    show(text, Toast.LENGTH_SHORT)
}

@SuppressLint("ShowToast")
fun show(text: String, time: Int) {
    if (mContext == null) {
        return
    }

    if (time != Toast.LENGTH_SHORT && time != Toast.LENGTH_LONG) {
        return
    }

    if (mToast == null) {
        mToast = Toast.makeText(mContext, text, time)
    } else {
        mToast!!.setText(text)
    }

    mToast!!.show()
}

/**
 * 异步操作时，调用此函数更新主线程UI
 * @param text text
 */
fun asyncShow(text: String) {
    ThreadUtils.post(Runnable { show(text) })
}

/**
 * 异步操作时，调用此函数更新主线程UI
 * @param text text
 */
fun asyncShow(text: String, time: Int) {
    ThreadUtils.post(Runnable { show(text, time) })
}