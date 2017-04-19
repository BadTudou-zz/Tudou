package com.badtudou.tudou;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by badtudou on 19/04/2017.
 */

public class Util {

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
}
