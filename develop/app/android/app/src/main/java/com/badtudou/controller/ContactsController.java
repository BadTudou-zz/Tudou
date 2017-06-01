package com.badtudou.controller;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
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
public class ContactsController {

    static public final int REQUEST_SELECT_CONTACT = 1;
    private ContentResolver contentResolver = null;
    private Activity activity;
    public ContactsController(Activity activity) {
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

    public Map<String, String> getContactsById(Long id){
        List<Map<String, String>> resultList = null;
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = null;
        String selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?";
        Map<String, String> itemList = new HashMap<>();
        String[] selectionArgs = {String.valueOf(id)};;
        String sortOrder = null;
        itemList.put("id", ContactsContract.CommonDataKinds.Phone.CONTACT_ID);
        itemList.put("name", ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        itemList.put("number", ContactsContract.CommonDataKinds.Phone.NUMBER);
        resultList = Util.ContentResolverSearch(contentResolver, uri, projection, itemList, selection, selectionArgs, sortOrder);
        return resultList.size()==1?resultList.get(0):null;
    }

    public List<Map<String, String>> getContactsByName(String name){
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = null;
        String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " LIKE ?";
        Map<String, String> itemList = new HashMap<>();
        String[] selectionArgs = {"%"+name+"%"};;
        String sortOrder = null;
        itemList.put("id", ContactsContract.CommonDataKinds.Phone.CONTACT_ID);
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
        itemList.put("id", ContactsContract.CommonDataKinds.Phone.CONTACT_ID);
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

    public int deleteContacts(long contactId){
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
        return Util.ContentResolverDelete(contentResolver, contactUri, null, null);

    }

    public void actionView(long contactId){
        Uri personUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);// info.id联系人ID
        Intent intent = new Intent(new Intent(Intent.ACTION_VIEW, personUri));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.getApplication().startActivity(intent);
        }
    }

    public void actionNew(){
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.getApplication().startActivity(intent);
        }
    }

    public void actionEdit(long contactId){
        Uri personUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);// info.id联系人ID
        Intent intent = new Intent(Intent.ACTION_EDIT, personUri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.getApplication().startActivity(intent);
        }
    }

    public void selectContact() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivityForResult(intent, REQUEST_SELECT_CONTACT);
        }
    }


}
