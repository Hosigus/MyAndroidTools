package com.hosigus.tools.utils

import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.util.SparseArray
import com.hosigus.tools.interfaces.PermissionCallback
import com.hosigus.tools.options.PermissionOption

/**
 * Created by Hosigus on 2018/6/6.
 */
private val options: SparseArray<PermissionOption> by lazy { SparseArray<PermissionOption>() }

/**
 * 申请需要申请的权限
 * @param activity activity
 * @param permissions permissions
 * @param requestCode requestCode
 * @return true only if all permission granted
 */
@TargetApi(23)
fun requestPermission(activity: Activity, permissions: Array<String>, requestCode: Int): Boolean {
    val need = needRequestPermissions(activity, permissions) ?: return true
    ActivityCompat.requestPermissions(activity, need, requestCode)
    return false
}

/**
 * 直接执行添加过的Option,无权限自动申请,有权限自动执行
 * @param activity activity
 * @param requestCode requestCode
 */
@TargetApi(23)
fun executeOption(activity: Activity, requestCode: Int) {
    if (requestPermission(activity, options.get(requestCode).permissions, requestCode)) {
        options.get(requestCode).callback?.onGranted()
    }
}

/**
 * 添加一些Option,方便直接调用
 * @param option
 */
fun addOption(option: PermissionOption) {
    options.put(option.requestCode, option)
}

/**
 * 检查权限申请结果,根据requestCode调用相应option中的callback
 * @param requestCode requestCode
 * @param permissions permissions
 * @param grantResults grantResults
 */
fun onPermissionRequest(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
    onPermissionRequest(options.get(requestCode).callback, permissions, grantResults)
}

fun onPermissionRequest(callback: PermissionCallback?, permissions: Array<String>, grantResults: IntArray) {
    val deniedList = ArrayList<String>()
    var i = 0
    val size = grantResults.size
    while (i < size) {
        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
            deniedList.add(permissions[i])
        }
        i++
    }
    if (deniedList.isEmpty()) {
        callback?.onGranted()
    } else {
        callback?.onDenied(deniedList.toTypedArray())
    }
}

fun startAppSetting(activity: Activity) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    intent.data = Uri.parse("package:" + activity.packageName)
    activity.startActivity(intent)
}

private fun needRequestPermissions(activity: Activity, permissions: Array<String>): Array<String>? {
    val needList = ArrayList<String>()
    for (permission in permissions) {
        if (ActivityCompat.checkSelfPermission(activity,
                        permission) != PackageManager.PERMISSION_GRANTED) {
            needList.add(permission)
        }
    }
    return if (needList.isEmpty()) {
        null
    } else needList.toTypedArray()
}