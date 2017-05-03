package com.badtudou.view.activity;

import android.content.ContentUris;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.badtudou.model.Contacts;
import com.badtudou.model.ImageAdapter;
import com.badtudou.tudou.R;
import com.badtudou.util.Util;
import com.badtudou.tudou.databinding.ActivityDetailsBinding;

import java.util.HashMap;
import java.util.Map;
public class DetailsActivity extends AppCompatActivity {

    private Contacts contacts;
    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        LayoutInflater layoutInflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);

        ActivityDetailsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_details);
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
        initViews();

        // get name and number
        final long id = ContentUris.parseId(getIntent().getData());
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = null;
        String selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " =? ";
        Map<String, String> itemList = new HashMap<>();
        String[] selectionArgs = {String.valueOf(id)};
        String sortOrder = null;
        itemList.put("id", ContactsContract.CommonDataKinds.Phone.CONTACT_ID);
        itemList.put("name", ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        itemList.put("number", ContactsContract.CommonDataKinds.Phone.NUMBER);
        Map<String, String> map = new HashMap<>();
        map =  Util.ContentResolverSearch(getContentResolver(), uri, projection, itemList, selection, selectionArgs, sortOrder).get(0);

        // get email
        uri = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        projection = null;
        selection = ContactsContract.CommonDataKinds.Email.CONTACT_ID + " =? ";
        itemList.clear();
        itemList.put("email", ContactsContract.CommonDataKinds.Email.DATA);
        Map<String, String> mapEmail = new HashMap<>();
        try {
            mapEmail = Util.ContentResolverSearch(getContentResolver(), uri, projection, itemList, selection, selectionArgs, sortOrder).get(0);
        }catch (IndexOutOfBoundsException indexExp){

        }
        map.putAll(mapEmail);
        contacts = new Contacts();
        map2Obj(map, contacts);
        binding.setContacts(contacts);
    }

    private void initViews(){

        TextView text_phone = (TextView)findViewById(R.id.text_phone);
        TextView text_email = (TextView)findViewById(R.id.text_email);
        ImageButton button_back = (ImageButton)findViewById(R.id.button_back_details);
        setGridView();

        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void map2Obj(Map<String, String> map, Contacts contacts){
        long id = Long.valueOf(map.get("id"));
        String dispalyname = map.get("name");
        String phone = map.get("number");
        String email = map.get("email");
        contacts.set(id, dispalyname, phone, email);

    }

    private void setGridView(){
        gridView = (GridView) findViewById(R.id.gridView_im);
        int size = 100;
        int length = 100;
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        float density = dm.density;
        int gridviewWidth = (int)(size * (length + 10) * density);
        int itemWidth = (int)(length * density);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(gridviewWidth, LinearLayoutCompat.LayoutParams.MATCH_PARENT);
        gridView.setLayoutParams(params);
        gridView.setColumnWidth(itemWidth);
        gridView.setHorizontalSpacing(5);
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setNumColumns(size);

        gridView.setAdapter(new ImageAdapter(this));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }
}

