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


public class MainActivity extends AppCompatActivity implements
        HistoryFragment.OnFragmentInteractionListener,
        ContactsFragment.OnFragmentInteractionListener,
        CallFragment.OnFragmentInteractionListener {

    private TextView mTextMessage;
    private FragmentManager fragmentManager;
    private HistoryFragment historyFragment;
    private ContactsFragment contactsFragment;
    private CallFragment callFragment;
    private android.support.v4.app.FragmentTransaction transaction;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            transaction = fragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_history:
                    mTextMessage.setText(R.string.title_history);
                    if (historyFragment == null) {
                        initFragments();
                    }
                    hideFragments();
                    showFrame(historyFragment);
                    return true;

                case R.id.navigation_contacts:
                    mTextMessage.setText(R.string.title_contacts);
                    if (contactsFragment == null) {
                        initFragments();
                    }
                    hideFragments();
                    showFrame(contactsFragment);
                    return true;

                case R.id.navigation_call:
                    mTextMessage.setText(R.string.title_call);
                    if(callFragment == null){
                        initFragments();
                    }
                    hideFragments();
                    showFrame(callFragment);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFragments();
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_contacts);
    }

    private void initFragments(){
        // get fragmentManager
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        historyFragment = new HistoryFragment();
        contactsFragment = new ContactsFragment();
        callFragment = new CallFragment();
        transaction.add(R.id.content, historyFragment);
        transaction.add(R.id.content, contactsFragment);
        transaction.add(R.id.content, callFragment);

    }

    private void hideFragments(){
        transaction.hide(contactsFragment);
        transaction.hide(historyFragment);
        transaction.hide(callFragment);
    }

    private void showFrame(Fragment fragmentame){
        transaction.replace(R.id.content, fragmentame);
        transaction.show(fragmentame);
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
