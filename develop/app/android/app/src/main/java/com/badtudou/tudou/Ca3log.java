package com.badtudou.tudou;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by badtudou on 16/04/2017.
 * CallLog
 */

public class Ca3log {
    private Uri uri = CallLog.Calls.CONTENT_URI;
    private ContentResolver contentResolver = null;
    private Cursor cursor = null;
    private Activity activity;
    private List<Map<String, String>> ca3list;

    Ca3log(Activity activity) {
        this.activity = activity;
        contentResolver = activity.getContentResolver();
        this.ca3list = new ArrayList<>();
        requireReadPermission();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Map<String, String>> getCallsList(){
        try {
            requireReadPermission();
            cursor = contentResolver.query(uri, null, null ,null, null);
            if(cursor != null){
                while (cursor.moveToNext()){
                    String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                    String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
                    String duration = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION));
                    String date = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE));
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    date = simpleDateFormat.format(Long.parseLong(date));
                    Map<String, String> map = new HashMap<>();
                    map.put("number", number);
                    map.put("name",name);
                    map.put("duration",duration);
                    map.put("date",date);
                    ca3list.add(cursor.getPosition(), map);
                    Log.d("Test", "number:"+ number+" name:"+name+" duration:"+duration+" date:"+date);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            cursor.close();
        }
        return this.ca3list;

    }

    public void requireReadPermission(){
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CALL_LOG}, 1);
        }
    }
}
