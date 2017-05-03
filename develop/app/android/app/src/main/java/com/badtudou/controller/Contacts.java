package com.badtudou.controller;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Groups;

import com.badtudou.util.Util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by badtudou on 12/04/2017.
 */
public class Contacts {
    public static final String NAME = "name";
    public static final String NUMBER = "number";
    private Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
    private ContentResolver contentResolver = null;
    private Cursor cursor = null;
    private Activity activity;
    public Contacts(Activity activity) {
        this.activity = activity;
        contentResolver =  activity.getContentResolver();
        Util.PermissionRequire(activity, Manifest.permission.READ_CONTACTS);
    }


    public List<Map<String, String>> getContactsList(){
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = null;
        String selection = null;
        Map<String, String> itemList = new HashMap<>();
        String[] selectionArgs = null;
        String sortOrder = null;
        itemList.put("id", ContactsContract.CommonDataKinds.Phone.CONTACT_ID);
        itemList.put("name", ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        itemList.put("number", ContactsContract.CommonDataKinds.Phone.NUMBER);
        return Util.ContentResolverSearch(contentResolver, uri, projection, itemList, selection, selectionArgs, sortOrder);
    }

    public List<Map<String, String>> getGroupsList(){
        Uri uri = Groups.CONTENT_URI;
        String[] projection = null;
        String selection = null;
        Map<String, String> itemList = new HashMap<>();
        String[] selectionArgs = null;
        String sortOrder = null;
        itemList.put("id", Groups._ID);
        itemList.put("title", Groups.TITLE);
        return Util.ContentResolverSearch(contentResolver, uri, projection, itemList, selection, selectionArgs, sortOrder);
    }

    public List<Map<String, String>> getContactsByName(String name){
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = null;
        String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " LIKE ?";
        Map<String, String> itemList = new HashMap<>();
        String[] selectionArgs = {"%"+name+"%"};;
        String sortOrder = null;
        itemList.put("name", ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        itemList.put("number", ContactsContract.CommonDataKinds.Phone.NUMBER);
       return Util.ContentResolverSearch(contentResolver, uri, projection, itemList, selection, selectionArgs, sortOrder);
    }

    public List<Map<String, String>> getContactsByNumber(String number){
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = null;
        String selection = ContactsContract.CommonDataKinds.Phone.NUMBER + " LIKE ?";
        Map<String, String> itemList = new HashMap<>();
        String[] selectionArgs = {"%"+number+"%"};
        String sortOrder = null;
        itemList.put("name", ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        itemList.put("number", ContactsContract.CommonDataKinds.Phone.NUMBER);
        return Util.ContentResolverSearch(contentResolver, uri, projection, itemList, selection, selectionArgs, sortOrder);
    }

    public InputStream openPhoto(long contactId) {
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
        Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
        Cursor cursor = contentResolver.query(photoUri,
                new String[] {ContactsContract.CommonDataKinds.Photo.PHOTO}, null, null, null);
        if (cursor == null) {
            return null;
        }
        try {
            if (cursor.moveToFirst()) {
                byte[] data = cursor.getBlob(0);
                if (data != null) {
                    return new ByteArrayInputStream(data);
                }
            }
        } finally {
            cursor.close();
        }
        return null;
    }

}
