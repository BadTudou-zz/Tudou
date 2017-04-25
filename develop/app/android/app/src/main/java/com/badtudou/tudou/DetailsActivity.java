package com.badtudou.tudou;

import android.Manifest;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.HashMap;
import java.util.Map;
public class DetailsActivity extends AppCompatActivity {

    private Map<String, String> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        this.contacts = new HashMap<>();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (! Intent.ACTION_VIEW.equals(getIntent().getAction())){
            return;
        }

        final long id = ContentUris.parseId(getIntent().getData());
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = null;
        String selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " =? ";
        Map<String, String> itemList = new HashMap<>();
        String[] selectionArgs = {String.valueOf(id)};
        String sortOrder = null;
        itemList.put("name", ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        itemList.put("number", ContactsContract.CommonDataKinds.Phone.NUMBER);
        this.contacts = Util.ContentResolverSearch(getContentResolver(), uri, projection, itemList, selection, selectionArgs, sortOrder).get(0);
    }
}
