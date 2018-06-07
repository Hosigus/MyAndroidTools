package com.hosigus.tools.utils

import android.content.Context
import android.content.SharedPreferences
import com.hosigus.tools.options.DownloadOption
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.RandomAccessFile
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by Hosigus on 2018/6/6.
 * DownloadManager
 * multithreaded section downloads
 */
class DownloadManager(context: Context,val option: DownloadOption) {
    private val log = MyLog("DOWNLOAD_MANAGER", Level.NOTHING)

    private lateinit var threads: Array<DownloadThread>

    private val sp: SharedPreferences = context.getSharedPreferences("DownloadProgress", Context.MODE_PRIVATE)
    private val realFile = File(option.filePath, option.fileName)
    private val cacheFile = File(realFile.absolutePath + ".downloading")

    private var fileSize = -1
    private var totalProgress = 0

    private val notifyProgressRunnable = {
        option.listener!!.onDownloading(totalProgress, fileSize)
    }

    /**
     * stop print logs
     */
    fun closeLog() {
        log.level = Level.NOTHING
    }

    /**
     * start print logs
     * it can be used to debug
     */
    fun openLog() {
        log.level = Level.ALL
    }

    /**
     * start/continue to download.
     * it will call 'onStart' if you implemented it then init download at the first call.
     */
    fun download() {
        log.d("onDownload")

        if (fileSize in 1..totalProgress || realFile.exists()) {
            refreshProgress(0)
            return
        }
        if (fileSize > 0) {
            resume()
            return
        }

        log.v("onDownloadInit")
        option.listener?.onStart()
        initDownload()
    }

    /**
     * pause download
     */
    fun pause() {
        log.d("onPause")
        option.listener?.onPaused()
        if (fileSize > 0) {
            for (thread in threads) {
                thread.continueDownload=false
            }
        }
    }

    /**
     * cancel download and delete file cache
     */
    fun cancel() {
        log.d("onCancel")
        option.listener?.onCancel()

        if (fileSize > 0) {
            val edit = sp.edit()
            val strHead = "${option.url}${option.filePath}${option.fileName}progress"
            for (thread in threads) {
                thread.progress = 0
                thread.continueDownload = false
                edit.putInt(strHead + thread.threadId, 0)
            }
            edit.apply()
        }

        fileSize = -1
        totalProgress = 0

        if (cacheFile.exists()) {
            cacheFile.delete()
        }
    }

    /**
     * delete download file (not cacheFile)
     */
    fun delete() {
        log.d("onDelete")
        if (realFile.exists()) {
            realFile.delete()
        }
    }

    /**
     * continue download
     */
    private fun resume() {
        log.v("start download threads")
        if (fileSize > 0) {
            for (thread in threads) {
                thread.continueDownload = true
                ThreadUtils.execute(thread)
            }
        }
    }

    /**
     * init download info
     */
    private fun initDownload() {
        ThreadUtils.execute {
            var conn: HttpURLConnection? = null
            try {
                log.v("get ${option.url} size")
                conn = getConnection(option.url)
                conn.connect()

                log.d("connect responseCode: ${conn.responseCode}")
                if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                    fileSize = conn.contentLength

                    log.d("contentLength = ${fileSize}B")
                    if (fileSize <= 0) {
                        throw RuntimeException("Can't get file size")
                    }

                    log.v("init threads")
                    if (!cacheFile.exists()) {
                        val edit = sp.edit()
                        val strHead = "${option.url}${option.filePath}${option.fileName}progress"
                        for (i in 0 until option.threadCount) {
                            edit.putInt(strHead + i, 0)
                        }
                        edit.apply()
                    }
                    val blockSize = fileSize / option.threadCount
                    threads = Array(option.threadCount) { i ->
                        val progress = sp.getInt("${option.url}${option.filePath}${option.fileName}progress$i", 0)
                        totalProgress += progress
                        DownloadThread(i, blockSize * i,
                                if (i + 1 != option.threadCount) blockSize * (i + 1) - 1 else fileSize, progress)
                    }
                    val raf = RandomAccessFile(cacheFile, "rwd")
                    raf.setLength(fileSize.toLong())
                    raf.close()
                    resume()
                }
            } catch (e: Exception) {
                onFailed(e)
            } finally {
                conn?.disconnect()
            }
        }
    }

    /**
     * notify your download failed
     */
    private fun onFailed(e: Exception) {
        e.printStackTrace()
        option.listener?.onFailed(e)
    }

    /**
     * publish the total progress of all threads
     */
    @Synchronized private fun refreshProgress(increase: Int) {
        totalProgress += increase
        if (totalProgress >= fileSize) {
            ThreadUtils.post{
                cacheFile.renameTo(realFile)
                log.d("onSuccess")
                option.listener?.onSuccess(realFile)
            }
        }else if (option.listener != null) {
            ThreadUtils.post(notifyProgressRunnable)
        }
    }

    /**
     * connect to url
     */
    @Throws(IOException::class)
    private fun getConnection(downloadUrl: String): HttpURLConnection {
        val url = URL(downloadUrl)
        val conn = url.openConnection() as HttpURLConnection
        conn.connectTimeout = 5 * 1000
        conn.requestMethod = "GET"
        conn.setRequestProperty("Accept", "*/*")
        conn.setRequestProperty("Accept-Language", "zh-CN")
        conn.setRequestProperty("Referer", downloadUrl)
        conn.setRequestProperty("Charset", "UTF-8")
        conn.setRequestProperty("Connection", "Keep-Alive")
        return conn
    }

    private inner class DownloadThread(val threadId: Int, val startPosition: Int, val endPosition: Int, var progress: Int) : Runnable {
        private val threadName = "DOWNLOAD_THREAD $threadId"
        private val log = MyLog(threadName)
        var continueDownload = true

        init {
            log.v("$threadName created")
        }

        override fun run() {
            log.v("$threadName start")
            val progressLabel = "${option.url}${option.filePath}${option.fileName}progress$threadId"
            ThreadUtils.execute {
                var conn: HttpURLConnection? = null
                var inputStream: InputStream? = null
                var raf: RandomAccessFile? = null
                try {
                    conn = getConnection(option.url)
                    conn.setRequestProperty("Range", "bytes=${startPosition + progress}-$endPosition")
                    conn.connect()

                    log.v("$threadName connect responseCode: ${conn.responseCode}")
                    if (conn.responseCode == HttpURLConnection.HTTP_PARTIAL) {
                        val bytes = ByteArray(1024 * 8)
                        var offset: Int
                        inputStream = conn.inputStream
                        raf = RandomAccessFile(cacheFile, "rwd")
                        raf.seek(startPosition.toLong() + progress)

                        log.v("$threadName download from ${progress+startPosition} to $endPosition ($progress/${endPosition-startPosition})")
                        offset = inputStream.read(bytes)
                        while (continueDownload && offset != -1) {
                            raf.write(bytes,0,offset)
                            progress += offset
                            sp.edit().putInt(progressLabel, progress).apply()
                            refreshProgress(offset)
                            offset = inputStream.read(bytes)
                        }
                    } else {
                        throw RuntimeException("can't not continue")
                    }
                } catch (e: Exception) {
                    onFailed(e)
                } finally {
                    raf?.close()
                    inputStream?.close()
                    conn?.disconnect()
                    sp.edit().putInt(progressLabel, progress).apply()
                    log.v("$threadName download end $startPosition to ${progress+startPosition} ($progress/${endPosition-startPosition}))")
                }
            }
        }
    }
}