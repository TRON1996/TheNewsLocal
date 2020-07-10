package com.hzz.thenewslocal.utils;

import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;


public class Permissions {
    /**
     * 申请文件读写权限
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        int REQUEST_EXTERNAL_STORAGE = 1;
        String[] PERMISSIONS_STORAGE = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"};
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void verifyInternetPermissions(Activity activity) {
        int REQUEST_EXTERNAL_STORAGE = 1;
        String[] PERMISSIONS_INTERNET = {
                "android.permission.INTERNET"};
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.INTERNET");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_INTERNET, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
