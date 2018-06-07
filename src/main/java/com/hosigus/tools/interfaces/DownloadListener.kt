package com.hosigus.tools.interfaces

import java.io.File

/**
 * Created by Hosigus on 2018/6/5.
 */
interface DownloadListener {

    /**
     * on first time start Download
     */
    fun onStart() {}

    /**
     * upload progress
     */
    fun onDownloading(progress: Int, size: Int) {}

    /**
     * on download pause
     */
    fun onPaused() {}

    /**
     * on download cancel
     */
    fun onCancel() {}

    /**
     * on download success
     */
    fun onSuccess(download: File) {}

    /**
     * on download failed
     * it can cause by IO_E，URL_E ……
     */
    fun onFailed(e: Exception) {}

}