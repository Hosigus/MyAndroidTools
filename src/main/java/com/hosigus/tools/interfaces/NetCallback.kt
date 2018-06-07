package com.hosigus.tools.interfaces

import com.hosigus.tools.items.JSONBean

/**
 * Created by Hosigus on 2018/6/5.
 */
interface NetCallback {
    fun onSuccess(data: JSONBean) {}
    fun onFail(e: Exception) {}
}