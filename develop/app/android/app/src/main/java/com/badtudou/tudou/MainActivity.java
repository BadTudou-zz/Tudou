package com.badtudou.tudou;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements HistoryFragment.OnFragmentInteractionListener, ContactsFragment.OnFragmentInteractionListener {

    private TextView mTextMessage;
    private FragmentManager fragmentManager;
    private HistoryFragment historyFragment;
    private ContactsFragment contactsFragment;
    private View historyLayout;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch (item.getItemId()) {
                case R.id.navigation_history:
                    mTextMessage.setText(R.string.title_history);
                    Log.d("Test", "Click history");
                    if (historyFragment == null) {
                        // 如果MessageFragment为空，则创建一个并添加到界面上
                        historyFragment = new HistoryFragment();
                        transaction.add(R.id.content, historyFragment);
                    }else {
                        // 如果MessageFragment不为空，则直接将它显示出来
                        transaction.hide(contactsFragment);
                    }
                    transaction.show(historyFragment);
                    transaction.commit();
                    return true;
                case R.id.navigation_contacts:
                    mTextMessage.setText(R.string.title_contacts);
                    Log.d("Test", "Click contacts");
                    if (contactsFragment == null) {
                        // 如果MessageFragment为空，则创建一个并添加到界面上
                        contactsFragment = new ContactsFragment();
                        transaction.add(R.id.content, contactsFragment);
                    }
                    else{
                        transaction.hide(historyFragment);
                    }
                    transaction.show(contactsFragment);
                    transaction.commit();
                    return true;
                case R.id.navigation_call:
                    mTextMessage.setText(R.string.title_call);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get fragmentManager
        fragmentManager = getSupportFragmentManager();
        historyLayout = findViewById(R.id.navigation_history);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_contacts);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
