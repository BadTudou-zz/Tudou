package com.badtudou.controller;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import com.badtudou.util.Util;

import java.util.ArrayList;
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
        Util.PermissionRequire(activity, Manifest.permission.WRITE_CONTACTS);
    }

    public List<Map<String, String>> getGroupsList(){
        Uri uri = ContactsContract.Groups.CONTENT_SUMMARY_URI;
        String[] projection = null;
        String selection = null;
        Map<String, String> itemList = new HashMap<>();
        String[] selectionArgs = null;
        String sortOrder = null;
        itemList.put("id", ContactsContract.Groups._ID);
        itemList.put("title", ContactsContract.Groups.TITLE);
        itemList.put("count", ContactsContract.Groups.SUMMARY_COUNT);
        return Util.ContentResolverSearch(contentResolver, uri, projection, itemList, selection, selectionArgs, sortOrder);
    }

    public List<Map<String, String>> getMembership(long id){
        Uri uri = ContactsContract.Data.CONTENT_URI;
        String[] projection = {ContactsContract.CommonDataKinds.GroupMembership.CONTACT_ID, ContactsContract.CommonDataKinds.GroupMembership.RAW_CONTACT_ID, ContactsContract.CommonDataKinds.GroupMembership.DISPLAY_NAME_PRIMARY};
       String selection = ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID + " = ? ";//AND " +
//                ContactsContract.CommonDataKinds.GroupMembership.MIMETYPE + " = ?";
        Map<String, String> itemList = new HashMap<>();
        String[] selectionArgs = {String.valueOf(id)};
        String sortOrder = null;
        itemList.put("id", ContactsContract.CommonDataKinds.GroupMembership.CONTACT_ID);
        itemList.put("rowid", ContactsContract.CommonDataKinds.GroupMembership.RAW_CONTACT_ID);
        itemList.put("name", ContactsContract.CommonDataKinds.GroupMembership.DISPLAY_NAME_PRIMARY);
        return  Util.ContentResolverSearch(contentResolver, uri, projection, itemList, selection, selectionArgs, sortOrder);
    }

    public void addMembership(long groupId, long personId){
        Uri uri = ContactsContract.Data.CONTENT_URI;
        ContentValues values = new ContentValues();
        values.put(ContactsContract.Data.RAW_CONTACT_ID, personId);
        values.put(ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID,groupId);
        values.put(ContactsContract.CommonDataKinds.GroupMembership.MIMETYPE, ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE);
        Util.ContentResolverInsert(contentResolver, uri, values);
    }

    public int removeMembership(long groupId, long personId){
        Uri uri = ContactsContract.Data.CONTENT_URI;
        String where = ContactsContract.Data.CONTACT_ID + " = ?"
                + " AND " + ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID + " = ?"
                + " AND " + ContactsContract.CommonDataKinds.GroupMembership.MIMETYPE + " = ? ;";
        String[] selectionArgs = {String.valueOf(personId), String.valueOf(groupId), ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE};

        return Util.ContentResolverDelete(contentResolver, uri, where, selectionArgs);
    }

    public Uri add(ContentValues values){
        Uri uri = ContactsContract.Groups.CONTENT_URI;
        return Util.ContentResolverInsert(contentResolver, uri, values);
    }

    public void actionNew(){
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType(ContactsContract.Groups.CONTENT_TYPE);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.getApplication().startActivity(intent);
        }
    }
}
