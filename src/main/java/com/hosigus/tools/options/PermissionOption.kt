package com.hosigus.tools.options

import com.hosigus.tools.interfaces.PermissionCallback

/**
 * Created by Hosigus on 2018/6/6.
 */
class PermissionOption(val requestCode: Int) {
    var permissions: Array<String> = arrayOf()
    var callback: PermissionCallback? = null

    fun permissions(permissions: Array<String>): PermissionOption {
        this.permissions = permissions
        return this
    }

    fun callback(callback: PermissionCallback): PermissionOption {
        this.callback = callback
        return this
    }

}