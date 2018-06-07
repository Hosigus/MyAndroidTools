package com.hosigus.tools.utils

import android.content.Context
import android.graphics.Point
import android.graphics.Rect
import android.view.Gravity
import android.view.Window
import android.view.WindowManager

/**
 * Created by Hosigus on 2018/6/6.
 */
private var screenWidth = 0
private var screenHeight = 0

fun dp2px(context: Context, dpValue: Float) = (dpValue * context.resources.displayMetrics.density + 0.5f).toInt()

fun px2dp(context: Context, pxValue: Float) = (pxValue / context.resources.displayMetrics.density + 0.5f).toInt()

fun getScreenHeight(c: Context): Int {
    if (screenHeight == 0) {
        val wm = c.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val size = Point()
        display.getSize(size)
        screenHeight = size.y
    }
    return screenHeight
}

fun getScreenWidth(c: Context): Int {
    if (screenWidth == 0) {
        val wm = c.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val size = Point()
        display.getSize(size)
        screenWidth = size.x
    }
    return screenWidth
}

fun setScreenFullWidth(context: Context, window: Window, wMargin: Int) {
    val layoutParams = window.attributes
    window.setGravity(Gravity.CENTER)
    val decorView = window.decorView
    decorView.getWindowVisibleDisplayFrame(Rect())
    layoutParams.width = getScreenWidth(context) - dp2px(context, wMargin.toFloat())
    window.setBackgroundDrawableResource(android.R.color.transparent)
    window.attributes = layoutParams
}

fun setScreenFullHeight(context: Context, window: Window, hMargin: Int) {
    val layoutParams = window.attributes
    window.setGravity(Gravity.CENTER)
    val decorView = window.decorView
    decorView.getWindowVisibleDisplayFrame(Rect())
    layoutParams.height = getScreenHeight(context) - dp2px(context, hMargin.toFloat())
    window.setBackgroundDrawableResource(android.R.color.transparent)
    window.attributes = layoutParams
}