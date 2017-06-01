package com.badtudou.view.activity;

import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.badtudou.controller.ContactsController;
import com.badtudou.model.FragmentViewClickListener;
import com.badtudou.view.fragment.CallFragment;
import com.badtudou.view.fragment.ContactsGroupFragment;
import com.badtudou.view.fragment.ContactsListFragment;
import com.badtudou.view.fragment.HistoryGroupFragment;
import com.badtudou.view.fragment.HistoryListFragment;
import com.badtudou.tudou.R;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements
        HistoryListFragment.OnFragmentInteractionListener,
        ContactsListFragment.OnFragmentInteractionListener,
        ContactsGroupFragment.OnFragmentInteractionListener,
        CallFragment.OnFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener,
        FragmentViewClickListener {

    private FragmentManager fragmentManager;
    private List<Fragment> fragmentsList;
    private Map<String,Fragment> fragmentsMap;
    private android.support.v4.app.FragmentTransaction transaction;
    private String FRAGMENT_HISTORY_LIST = "FRAGMENT_HISTORY_LIST";
    private String FRAGMENT_HISTORY_GROUP = "FRAGMENT_HISTORY_GROUP";
    private String FRAGMENT_CONTACTS_LIST = "FRAGMENT_CONTACTS_LIST";
    private String FRAGMENT_CONTACTS_GROUP = "FRAGMENT_CONTACTS_GROUP";
    private String FRAGMENT_CALL = "FRAGMENT_CALL";


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            transaction = fragmentManager.beginTransaction();
            switch (item.getItemId()){
                case R.id.navigation_history:
                    showFragment(fragmentsMap.get(FRAGMENT_HISTORY_LIST));
                    break;
                case R.id.navigation_contacts:
                    showFragment(fragmentsMap.get(FRAGMENT_CONTACTS_LIST));
                    break;
                case R.id.navigation_call:
                    // TODO 根据用户设置切换显示风格
                    showFragment(fragmentsMap.get(FRAGMENT_CALL));
                    break;
            }
            setActiviteNavigationItemBar(item.getItemId());
            return true;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        initFragments();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_contacts);

    }


    // 初始化所有fragment
    private void initFragments(){
        HistoryListFragment historyListFragment = new HistoryListFragment();
        HistoryGroupFragment historyGroupFragment = new HistoryGroupFragment();
        ContactsListFragment contactsListFragment = new ContactsListFragment();
        ContactsGroupFragment contactsGroupFragment = new ContactsGroupFragment();
        CallFragment callFragment = new CallFragment();

        fragmentsMap = new HashMap<>();
        fragmentsMap.put(FRAGMENT_HISTORY_LIST, historyListFragment);
        fragmentsMap.put(FRAGMENT_HISTORY_GROUP, historyGroupFragment);
        fragmentsMap.put(FRAGMENT_CONTACTS_LIST, contactsListFragment);
        fragmentsMap.put(FRAGMENT_CONTACTS_GROUP, contactsGroupFragment);
        fragmentsMap.put(FRAGMENT_CALL, callFragment);


        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        for(Fragment fragment: fragmentsMap.values()){
            transaction.add(R.id.content, fragment);
        }

    }

    // 隐藏所有framgnet
    private void hideAllFragments(){
        for(Fragment fragment: fragmentsMap.values()){
            transaction.hide(fragment);
        }
    }

    // 显示特定的fragment
    private void showFragment(Fragment fragment){
        if(fragment == null){
            initFragments();
        }
        hideAllFragments();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content, fragment);
        transaction.show(fragment);
        transaction.commit();
    }


    // 设置活动导航栏的bar
    private void setActiviteNavigationItemBar(int itemId){
        Map<Integer, Integer> item2bar = new HashMap<>();
        item2bar.put(R.id.navigation_history, R.id.navigationItemBar_History);
        item2bar.put(R.id.navigation_contacts, R.id.navigationItemBar_Contacts);
        item2bar.put(R.id.navigation_call, R.id.navigationItemBar_Call);

        for(Integer itemBarId: item2bar.values()){
            ((TextView) findViewById(itemBarId)).setBackgroundResource(R.color.colorActiviBarOff);
        }

        ((TextView) findViewById(item2bar.get(itemId))).setBackgroundResource(R.color.colorActiviBarOn);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    // 侧边栏 导航栏 项
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_notice) {
            // Handle the camera action
            Log.d("Test", "Click camera");
        }else if (id == R.id.nav_help) {

        } else if (id == R.id.nav_exit) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    /**
     * 隶属于自定义的FragmentViewClickListener，响应Fragment中View发起的Click请求
     *【注意】Fragment主动将特定View的Click事件分发给此函数处理
     * @param v 视图
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        viewIdClick(id, null);

    }

    @Override
    public void viewIdClick(int id, Map map) {
        switch (id){
            case R.id.button_switch_history_style_list:
                showFragment(fragmentsMap.get(FRAGMENT_HISTORY_LIST));
                break;

            case R.id.button_switch_history_style_group:
                showFragment(fragmentsMap.get(FRAGMENT_HISTORY_GROUP));
                break;
            
            case R.id.action_contacts_group:
                showFragment(fragmentsMap.get(FRAGMENT_CONTACTS_GROUP));
                break;

            case R.id.action_contacts_list:
                showFragment(fragmentsMap.get(FRAGMENT_CONTACTS_LIST));
                break;

            default:
                break;
        }
    }

//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == ContactsController.REQUEST_SELECT_CONTACT && resultCode == RESULT_OK) {
//            Uri contactUri = data.getData();
//            final long id = ContentUris.parseId(data.getData());
//            Log.d("Test", "onActivityResult"+String.valueOf(id));
//            // Do something with the selected contact at contactUri
//        }
//    }

}
