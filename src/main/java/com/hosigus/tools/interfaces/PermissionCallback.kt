package com.hosigus.tools.interfaces

/**
 * Created by Hosigus on 2018/6/6.
 */
interface PermissionCallback {
    fun onGranted(){}
    fun onDenied(DeniedPermissions: Array<String>){}
}