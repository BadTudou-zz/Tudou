package com.badtudou.tudou;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements
        HistoryFragment.OnFragmentInteractionListener,
        ContactsListFragment.OnFragmentInteractionListener,
        ContactsGroupFragment.OnFragmentInteractionListener,
        CallFragment.OnFragmentInteractionListener {

    private TextView mTextMessage;
    private FragmentManager fragmentManager;
    private HistoryFragment historyFragment;
    private ContactsListFragment contactsListFragment;
    private ContactsGroupFragment contactsGroupFragment;
    private CallFragment callFragment;
    private Call call;
    private android.support.v4.app.FragmentTransaction transaction;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            transaction = fragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_history:
                    if (historyFragment == null) {
                        initFragments();
                    }
                    hideFragments();
                    showFrame(historyFragment);
                    setNavigationBar("history");
                    return true;

                case R.id.navigation_contacts:
                    if ((contactsListFragment == null) || (contactsGroupFragment == null)) {
                        initFragments();
                    }
                    hideFragments();
                    showFrame(contactsListFragment);
                    setNavigationBar("contacts");
                    return true;

                case R.id.navigation_call:
                    if(callFragment == null){
                        initFragments();
                    }
                    hideFragments();
                    showFrame(callFragment);
                    setNavigationBar("call");
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        call = new Call(this);
        initFragments();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_contacts);

    }

    private void initFragments(){
        // get fragmentManager
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        historyFragment = new HistoryFragment();
        contactsListFragment = new ContactsListFragment();
        contactsGroupFragment = new ContactsGroupFragment();
        callFragment = new CallFragment();
        transaction.add(R.id.content, historyFragment);
        transaction.add(R.id.content, contactsListFragment);
        transaction.add(R.id.content, contactsGroupFragment);
        transaction.add(R.id.content, callFragment);

    }

    private void hideFragments(){
        transaction.hide(contactsListFragment);
        transaction.hide(contactsGroupFragment);
        transaction.hide(historyFragment);
        transaction.hide(callFragment);
    }

    private void showFrame(Fragment fragmentame){
        transaction.replace(R.id.content, fragmentame);
        transaction.show(fragmentame);
        transaction.commit();
    }

    private void setNavigationBar(String bar){
        int id_color_off = R.color.colorActiviBarOff;
        int id_color_on = R.color.colorActiviBarOn;
        TextView textViewHistory = (TextView)findViewById(R.id.activibarHistory);
        TextView textViewContacts = (TextView)findViewById(R.id.activibarContacts);
        TextView textViewCall = (TextView)findViewById(R.id.activibarCall);
        textViewHistory.setBackgroundResource(id_color_off);
        textViewContacts.setBackgroundResource(id_color_off);
        textViewCall.setBackgroundResource(id_color_off);
        switch (bar){
            case "call":
                textViewCall.setBackgroundResource(id_color_on);
                break;
            case "history":
                textViewHistory.setBackgroundResource(id_color_on);
                break;
            case "contacts":
                textViewContacts.setBackgroundResource(id_color_on);
                break;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
