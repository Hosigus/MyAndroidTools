package com.hosigus.tools.interfaces

import org.json.JSONObject

/**
 * Created by Hosigus on 2018/6/5.
 * parser json to bean
 */
interface Parserable {
    fun parser(obj: JSONObject) {}
}