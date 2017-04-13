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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by badtudou on 12/04/2017.
 */

public class Contacts {
    private Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
    private ContentResolver contentResolver = null;
    private Cursor cursor = null;
    private List<String> contactsList = null;
    private Activity activity;
    Contacts(Activity activity) {
        this.activity = activity;
        this.contactsList = new ArrayList<>();
        requireReadPermission();
    }

    public List<String> getContactsList(){
        try {
                contentResolver =  activity.getContentResolver();
                cursor = contentResolver.query(uri, null, null ,null, null);
                if(cursor != null){
                    while (cursor.moveToNext()){
                        String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contactsList.add(displayName+":"+number);
                    }
                }
            }catch (Exception e){
            e.printStackTrace();
            }finally {
                cursor.close();
            }
        return this.contactsList;
    }

    private  void requireReadPermission(){
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CONTACTS}, 1);
        }
    }
}
