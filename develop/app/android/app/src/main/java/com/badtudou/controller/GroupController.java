package com.badtudou.controller;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.net.Uri;
import android.provider.ContactsContract;

import com.badtudou.util.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by badtudou on 04/05/2017.
 */

public class GroupController {
    private ContentResolver contentResolver = null;
    private Activity activity;

    public GroupController(Activity activity) {
        this.activity = activity;
        this.contentResolver =  activity.getContentResolver();
        Util.PermissionRequire(activity, Manifest.permission.READ_CONTACTS);
    }

    public List<Map<String, String>> getGroupsList(){
        Uri uri = ContactsContract.Groups.CONTENT_URI;
        String[] projection = null;
        String selection = null;
        Map<String, String> itemList = new HashMap<>();
        String[] selectionArgs = null;
        String sortOrder = null;
        itemList.put("id", ContactsContract.Groups._ID);
        itemList.put("title", ContactsContract.Groups.TITLE);
        return Util.ContentResolverSearch(contentResolver, uri, projection, itemList, selection, selectionArgs, sortOrder);
    }
}
