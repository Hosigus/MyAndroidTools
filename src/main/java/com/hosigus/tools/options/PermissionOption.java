package com.hosigus.tools.options;

import com.hosigus.tools.interfaces.PermissionCallback;

/**
 * Created by 某只机智 on 2018/5/8.
 */

public class PermissionOption {
    private int requestCode;
    private String[] permissions;
    private PermissionCallback callback;

    public PermissionOption(int requestCode) {
        this.requestCode = requestCode;
    }

    public PermissionOption permissions(String[] permissions) {
        this.permissions = permissions;
        return this;
    }

    public PermissionOption callback(PermissionCallback callback) {
        this.callback = callback;
        return this;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public String[] getPermissions() {
        return permissions;
    }

    public PermissionCallback getCallback() {
        return callback;
    }
}
