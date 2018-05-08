package com.hosigus.tools.interfaces;

/**
 * Created by 某只机智 on 2018/5/8.
 */

public interface PermissionCallback {
    void onGranted();
    void onDenied(String[] DeniedPermissions);
}
