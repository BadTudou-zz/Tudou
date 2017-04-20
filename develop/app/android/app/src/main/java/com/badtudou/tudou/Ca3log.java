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
    private Activity activity;

    Ca3log(Activity activity) {
        this.activity = activity;
        contentResolver = activity.getContentResolver();
        Util.PermissionRequire(activity, Manifest.permission.READ_CALL_LOG);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Map<String, String>> getCallsList(){
        Uri uri = CallLog.Calls.CONTENT_URI;
        String[] projection = null;
        String selection = null;
        Map<String, String> itemList = new HashMap<>();
        String[] selectionArgs = null;
        String sortOrder = null;
        itemList.put("number", CallLog.Calls.NUMBER);
        itemList.put("name", CallLog.Calls.CACHED_NAME);
        itemList.put("duration", CallLog.Calls.DURATION);
        itemList.put("date", CallLog.Calls.DATE);
        return Util.ContentResolverSearch(contentResolver, uri, projection, itemList, selection, selectionArgs, sortOrder);

    }

}
