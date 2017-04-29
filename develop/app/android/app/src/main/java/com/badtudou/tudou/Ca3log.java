package com.badtudou.tudou;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.net.Uri;
import android.os.Build;
import android.provider.CallLog;
import android.support.annotation.RequiresApi;

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

    static final public Map<Integer, Integer> type2Resources = new HashMap<Integer , Integer>(){{
        put(CallLog.Calls.INCOMING_TYPE, R.drawable.incoming_type);
        put(CallLog.Calls.OUTGOING_TYPE, R.drawable.outgoing_type);

        // TODO
        put(CallLog.Calls.MISSED_TYPE, R.drawable.outgoing_type);
        put(CallLog.Calls.VOICEMAIL_TYPE, R.drawable.outgoing_type);
        put(CallLog.Calls.REJECTED_TYPE, R.drawable.outgoing_type);
        put(CallLog.Calls.BLOCKED_TYPE, R.drawable.outgoing_type);
        put(CallLog.Calls.ANSWERED_EXTERNALLY_TYPE, R.drawable.outgoing_type);

    }};

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Map<String, String>> getCallsList(){
        Uri uri = CallLog.Calls.CONTENT_URI;
        String[] projection = null;
        String selection = null;
        Map<String, String> itemList = new HashMap<>();
        String[] selectionArgs = null;
        String sortOrder = null;
        itemList.put("number", CallLog.Calls.NUMBER);
        itemList.put("duration", CallLog.Calls.DURATION);
        itemList.put("date", CallLog.Calls.DATE);
        itemList.put("type", CallLog.Calls.TYPE);
        return Util.ContentResolverSearch(contentResolver, uri, projection, itemList, selection, selectionArgs, sortOrder);

    }

}
