package com.hzz.thenewslocal.utils;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;

/**
 * 解析Android系统文档路径工具类
 * 顾东虎
 * 2020-4-6
 */
public class HandleOSImagePath {
    /**
     * 解析系统相册文件路径
     * @param context
     * @param data
     * @return
     */
    public static String ImagePath(Context context,Intent data) {
        String imagePath = null;
        Uri uri = data.getData();

        if (DocumentsContract.isDocumentUri(context, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);

            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(context,MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android,providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(context,contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            imagePath = getImagePath(context,uri, null);
        }
        return imagePath;

    }
    /**
     * 获取文件名及后缀
     */
    public static String getFileNameWithSuffix(String path) {
        if(TextUtils.isEmpty(path)){
            return "";
        }
        int start = path.lastIndexOf("/");
        if (start != -1 ) {
            return path.substring(start + 1);
        }
        return "";
    }

    private static  String getImagePath(Context context,Uri uri,String selection) {
        String path = null;
        //通过文件id查询uri,获得内容提供器
        Cursor cursor = context.getContentResolver().query(uri,null,selection,null,null);
        if (cursor!=null){
            if (cursor.moveToFirst()){
                //获取路径
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
        }
        cursor.close();
        return path;
    }
}
