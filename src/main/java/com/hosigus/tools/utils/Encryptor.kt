package com.hosigus.tools.utils

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.experimental.and

/**
 * Created by Hosigus on 2018/6/6.
 */
fun MD5Encode(code: String): String {
    try {
        val digest = MessageDigest.getInstance("md5")
        val bys = digest.digest(code.toByteArray())
        val builder = StringBuilder()
        for (b in bys) {
            val number: Int = (b and 0xff.toByte()).toInt()
            val str = Integer.toHexString(number)
            if (str.length == 1) {
                builder.append("0")
            }
            builder.append(str)
        }
        return builder.toString()
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
        return ""
    }

}