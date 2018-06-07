package com.hosigus.tools.options

import com.hosigus.tools.interfaces.DownloadListener

/**
 * Created by Hosigus on 2018/6/6.
 */
class DownloadOption(val url: String,val filePath: String,val fileName: String = url.split("/").last()) {

    var listener: DownloadListener? = null
    var threadCount: Int = 4
        private set

    fun listener(listener: DownloadListener): DownloadOption {
        this.listener = listener
        return this
    }

    fun threadCount(count: Int): DownloadOption {
        if (count < 1) {
            throw RuntimeException("no thread to download")
        }
        this.threadCount = count
        return this
    }

}