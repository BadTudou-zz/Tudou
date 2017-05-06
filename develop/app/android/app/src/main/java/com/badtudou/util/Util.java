package com.badtudou.util;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by badtudou on 19/04/2017.
 */

public class Util {

    /**
     *
     * @param contentResolver   内容提供者
     * @param uri               Uri
     * @param projection        字段列表
     * @param itemList          字段：获取与请求的key
     * @param selection         条件
     * @param selectionArgs     条件值
     * @param sortOrder         排序字段
     * @return                  List键值对
     */
    public static List<Map<String, String>> ContentResolverSearch(ContentResolver contentResolver, Uri uri, String[] projection, Map<String, String> itemList, String selection, String[] selectionArgs, String sortOrder){
        List<Map<String, String>> list = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = contentResolver.query(uri, projection, selection ,selectionArgs, sortOrder);
            if(cursor != null){
                while (cursor.moveToNext()){
                    Map<String, String> map = new HashMap<>();
                    for (Map.Entry<String, String> entry : itemList.entrySet()) {
                        String key = entry.getKey();
                        String key_uri = entry.getValue();
                        String value = cursor.getString(cursor.getColumnIndex(key_uri));
                        map.put(key, value);
                    }
                    list.add(cursor.getPosition(), map);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor != null)
                cursor.close();
        }
        return list;
    }

    public static Uri ContentResolverInsert(ContentResolver contentResolver, Uri uri, ContentValues values){
        return contentResolver.insert(uri, values);
    }

    /**
     * 请求权限
     * @param activity   activity
     * @param permission 权限名
     */
    public static void PermissionRequire(Activity activity, String permission){
        if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity, new String[]{permission}, 1);
        }
    }
}
