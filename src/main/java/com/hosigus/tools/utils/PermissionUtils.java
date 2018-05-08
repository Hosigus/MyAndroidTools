package com.hosigus.tools.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.SparseArray;

import com.hosigus.tools.interfaces.PermissionCallback;
import com.hosigus.tools.options.PermissionOption;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 某只机智 on 2018/5/8.
 */

public class PermissionUtils {
    private PermissionUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    private static SparseArray<PermissionOption> options;

    /**
     * 申请需要申请的权限
     * @param activity activity
     * @param permissions permissions
     * @param requestCode requestCode
     * @return true only if all permission granted
     */
    @TargetApi(23)
    public static boolean requestPermission(Activity activity, String[] permissions, int requestCode) {
        String need[] = needRequestPermissions(activity, permissions);
        if (need == null) {
            return true;
        }
        ActivityCompat.requestPermissions(activity, need, requestCode);
        return false;
    }

    /**
     * 直接执行添加过的Option,无权限自动申请,有权限自动执行
     * @param activity activity
     * @param requestCode requestCode
     */
    @TargetApi(23)
    public static void executeOption(Activity activity, int requestCode) {
        if (requestPermission(activity, options.get(requestCode).getPermissions(), requestCode)) {
            options.get(requestCode).getCallback().onGranted();
        }
    }

    /**
     * 添加一些Option,方便直接调用
     * @param option
     */
    public static void addOption(PermissionOption option) {
        if (options == null) {
            options = new SparseArray<>();
        }
        options.put(option.getRequestCode(), option);
    }

    /**
     * 检查权限申请结果,根据requestCode调用相应option中的callback
     * @param requestCode requestCode
     * @param permissions permissions
     * @param grantResults grantResults
     */
    public static void onPermissionRequest(int requestCode, String[] permissions, int[] grantResults) {
        onPermissionRequest(options.get(requestCode).getCallback(), permissions, grantResults);
    }

    public static void onPermissionRequest(PermissionCallback callback, String[] permissions, int[] grantResults) {
        List<String> deniedList = new ArrayList<>();
        for (int i = 0, size = grantResults.length; i < size; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                deniedList.add(permissions[i]);
            }
        }
        if (deniedList.isEmpty()) {
            callback.onGranted();
        }else {
            callback.onDenied(deniedList.toArray(new String[deniedList.size()]));
        }
    }

    public static void startAppSetting(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + activity.getPackageName()));
        activity.startActivity(intent);
    }

    private static String[] needRequestPermissions(Activity activity, String[] permissions) {
        List<String> needList = new ArrayList<>();
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(activity,
                    permission) != PackageManager.PERMISSION_GRANTED) {
                needList.add(permission);
            }
        }
        if (needList.isEmpty()) {
            return null;
        }
        return needList.toArray(new String[needList.size()]);
    }
}
