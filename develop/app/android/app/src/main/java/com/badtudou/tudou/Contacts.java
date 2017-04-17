package com.badtudou.tudou;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.provider.ContactsContract.Groups;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by badtudou on 12/04/2017.
 */
public class Contacts {
    private Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
    private ContentResolver contentResolver = null;
    private Cursor cursor = null;
    private List<Map<String, String>> contactsList = null;
    private List<Map<String, String>> groupList = null;
    private Activity activity;
    Contacts(Activity activity) {
        this.activity = activity;
        this.contactsList = new ArrayList<>();
        this.groupList = new ArrayList<>();
        contentResolver =  activity.getContentResolver();
        requireReadPermission();
    }

    public List<Map<String, String>> getContactsList(){
        try {
                cursor = contentResolver.query(uri, null, null ,null, null);
                if(cursor != null){
                    while (cursor.moveToNext()){
                        String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        Map<String, String> map = new HashMap<>();
                        map.put("name", displayName);
                        map.put("number",number);
                        contactsList.add(cursor.getPosition(), map);
                    }
                }
            }catch (Exception e){
            e.printStackTrace();
            }finally {
                cursor.close();
            }
        return this.contactsList;
    }

    public List<Map<String, String>> getContactsGroupList(){
        try {
            uri = Groups.CONTENT_URI;
            cursor = contentResolver.query(uri, null, null ,null, null);
            if(cursor != null){
                while (cursor.moveToNext()){
                    String displayName = cursor.getString(cursor.getColumnIndex(Groups.TITLE));
                    String id = cursor.getString(cursor.getColumnIndex(Groups._ID));
                    Map<String, String> map = new HashMap<>();
                    map.put("name", displayName);
                    map.put("id",id);
                    groupList.add(cursor.getPosition(), map);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            cursor.close();
        }
        return this.groupList;

    }


    private  void requireReadPermission(){
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CONTACTS}, 1);
        }
    }
}
