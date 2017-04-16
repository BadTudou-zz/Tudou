package com.badtudou.tudou;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

/**
 * Created by badtudou on 16/04/2017.
 * CallLog
 */

public class Ca3log {
    private Uri uri = CallLog.Calls.CONTENT_URI;
    private ContentResolver contentResolver = null;
    private Cursor cursor = null;
    private Activity activity;

    Ca3log(Activity activity) {
        this.activity = activity;
        contentResolver = activity.getContentResolver();
        requireReadPermission();
    }

    public void getCallsList(){
        try {
            cursor = contentResolver.query(uri, null, null ,null, null);
            if(cursor != null){
                while (cursor.moveToNext()){
                    String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                    String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
                    String duration = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION));
                    Log.d("Test", "number:"+ number+" name:"+name+" duration:"+duration);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            cursor.close();
        }

    }

    public void requireReadPermission(){
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CALL_LOG}, 1);
        }
    }
}
