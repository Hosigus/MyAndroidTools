package com.hosigus.tools.options

import com.hosigus.tools.items.JSONBean
import kotlin.collections.HashMap

/**
 * Created by Hosigus on 2018/6/5.
 */
class NetOption(val url: String) {
    var paramSet: MutableMap<String, String>? = null
        private set
    var headSet: MutableMap<String, String>? = null
        private set
    var beanClass: Class<out JSONBean>? = null
        private set

    fun param(key: String, value: String): NetOption {
        if (paramSet == null) {
            paramSet = hashMapOf()
        }
        paramSet!![key] = value
        return this
    }

    fun head(key: String, value: String): NetOption {
        if (headSet == null) {
            headSet = hashMapOf()
        }
        headSet!![key] = value
        return this
    }

    fun beanClass(beanClass: Class<out JSONBean>): NetOption {
        this.beanClass = beanClass
        return this
    }

}