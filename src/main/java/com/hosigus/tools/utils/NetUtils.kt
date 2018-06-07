package com.hosigus.tools.utils

import com.hosigus.tools.interfaces.NetCallback
import com.hosigus.tools.items.JSONBean
import com.hosigus.tools.options.NetOption
import org.json.JSONException
import org.json.JSONObject
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import javax.net.ssl.HttpsURLConnection

/**
 * Created by Hosigus on 2018/6/5.
 * <p> it only use to get bean , try to use them:
 * @see com.hosigus.tools.options.NetOption
 * @see com.hosigus.tools.interfaces.NetCallback
 * @see com.hosigus.tools.items.JSONBean
 */

fun get(options: NetOption, callback: NetCallback) {
    ThreadUtils.execute{
        val conn: HttpURLConnection
        try {
            val url = URL(options.url)
            conn = if (options.url.contains("https"))
                url.openConnection() as HttpsURLConnection
            else
                url.openConnection() as HttpURLConnection

            conn.connectTimeout = 5000
            conn.readTimeout = 5000
            conn.requestMethod = "GET"

            if (options.headSet != null) {
                for (key in options.headSet!!.keys) {
                    conn.setRequestProperty(key, options.headSet!![key])
                }
            }

            val resCode = conn.responseCode
            if (resCode != HttpsURLConnection.HTTP_OK) {
                ThreadUtils.post { callback.onFail(Exception(resCode.toString())) }
                return@execute
            }

            dealResult(conn.inputStream, options.beanClass, callback)

        } catch (e: Exception) {
            ThreadUtils.post { callback.onFail(e) }
        }

    }
}

fun post(options: NetOption, callback: NetCallback) {
    ThreadUtils.execute{
        val conn: HttpURLConnection
        try {
            val url = URL(options.url)
            conn = if (options.url.contains("https"))
                url.openConnection() as HttpsURLConnection
            else
                url.openConnection() as HttpURLConnection

            conn.connectTimeout = 5000
            conn.readTimeout = 5000
            conn.requestMethod = "POST"

            if (options.paramSet != null) {
                conn.doOutput = true
                val os = conn.outputStream
                os.write(formParamString(options.paramSet!!).toByteArray())
                os.flush()
                os.close()
            }

            if (options.headSet != null) {
                for (key in options.headSet!!.keys) {
                    conn.setRequestProperty(key, options.headSet!![key])
                }
            }

            val resCode = conn.responseCode
            if (resCode != HttpsURLConnection.HTTP_OK) {
                ThreadUtils.post { callback.onFail(Exception(resCode.toString())) }
                return@execute
            }

            dealResult(conn.inputStream, options.beanClass, callback)

        } catch (e: Exception) {
            ThreadUtils.post { callback.onFail(e) }
        }
    }
}

private fun dealResult(inputStream: InputStream, beanClass: Class<out JSONBean>?, callback: NetCallback) {
    val `in` = Scanner(inputStream)
    val builder = StringBuilder()
    while (`in`.hasNextLine()) {
        builder.append(`in`.nextLine()).append("\n")
    }
    val jsonStr = builder.toString()
    ThreadUtils.post{
        try {
            val bean: JSONBean = if (beanClass == null) object : JSONBean(){} else beanClass.newInstance()
            bean.json = jsonStr
            bean.parser(JSONObject(jsonStr))
            callback.onSuccess(bean)
        } catch (e: InstantiationException) {
            callback.onFail(e)
        } catch (e: IllegalAccessException) {
            callback.onFail(e)
        } catch (e: JSONException) {
            callback.onFail(e)
        }
    }
}

fun formParamString(paramSet: Map<String, String>): String {
    val builder = StringBuilder()
    for ((key, value) in paramSet) {
        builder.append(key).append("=").append(value).append("&")
    }
    builder.deleteCharAt(builder.length - 1)
    return builder.toString()
}
