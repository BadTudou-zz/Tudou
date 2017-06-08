package com.badtudou.view.fragment;

import com.badtudou.model.ContactsComparator;
import com.badtudou.util.Util;
import com.pinyinsearch.model.*;
import com.pinyinsearch.util.*;
import android.app.AlertDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringDef;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.badtudou.controller.CallController;
import com.badtudou.controller.ContactsController;
import com.badtudou.controller.SmsController;
import com.badtudou.model.FragmentViewClickListener;
import com.badtudou.tudou.R;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import net.sourceforge.pinyin4j.PinyinHelper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.brightyoyo.IndexBar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ContactsListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ContactsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactsListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ContactsController contactsController;
    private CallController callController;
    private SmsController smsController;
    private List<Map<String,String>> contactsList;
    private SimpleAdapter adapter;
    private View view, dialogView;
    private SwipeMenuListView listView;
    private IndexBar indexBar;
    private Map<String, Integer> mSections = new HashMap<String, Integer>();
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;

    private FragmentViewClickListener fragmentViewClickListener;
    private OnFragmentInteractionListener mlistener;
    private View.OnClickListener onClickListener;
    private List<Integer> floatingActionButtonIds;
    private FloatingActionMenu floatingActionMenu;
    private Map<String, List<Map<Integer,List<String>>>> hanyu2pinyinMap;
    private int activiteContactsIndex = -1;

    private static final int SWIP_MENU_ITEM_CALL = 1;
    private static final int SWIP_MENU_ITEM_SMS = 2;
    private static final int SWIP_MENU_ITEM_STAR = 3;
    private static final int SWIP_MENU_ITEM_EDIT = 4;
    private static final int SWIP_MENU_ITEM_DELETE = 5;
    final Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
    final Map<String, String> projectMap = new HashMap<String, String>() {{
        put("id",ContactsContract.CommonDataKinds.Phone.CONTACT_ID);
        put("name", ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        put("number", ContactsContract.CommonDataKinds.Phone.NUMBER);
    }};

    public ContactsListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContactsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactsListFragment newInstance(String param1, String param2) {
        ContactsListFragment fragment = new ContactsListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(0, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the expandable_selector for this fragment
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_contacts_list, container, false);
        dialogView = inflater.inflate(R.layout.dialog_alert, container, false);
        builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView);
        alertDialog = builder.create();
        initDates();
        initViews();
        return view;


    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_contacts_list_menu, menu);
        initSearchView(menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_contacts_group:
                fragmentViewClickListener.viewIdClick(id, null);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Context context) {
        fragmentViewClickListener = (FragmentViewClickListener)context;
        mlistener = (OnFragmentInteractionListener)context;
        super.onAttach(context);
    }

    private void initIndexBar(){
        indexBar = (IndexBar)view.findViewById(R.id.index_bar);
        indexBar.setSections(alphabets());
        indexBar.setIndexBarFilter(new IndexBar.IIndexBarFilter() {
            /**
             /* @param sideIndexY  滑动IndexBar的Y轴坐标
             * @param position    字母的索引位置
             * @param previewText 手指触摸的字母
             */
            @Override
            public void filterList(float sideIndex, int position, 	String previewText) {
                Integer selection = mSections.get(previewText);
                if(previewText == null){
                    return;
                }
                for (Map.Entry<String, List<Map<Integer,List<String>>>> entry: hanyu2pinyinMap.entrySet()) {
                    String zimu = entry.getKey().substring(0,1).substring(0,1);
                    if(zimu.equals(previewText.toLowerCase())){
                        for (Map<Integer, List<String>> keyName : entry.getValue()) {
                            Object[] indexs = keyName.keySet().toArray();
                            listView.setSelection((Integer)indexs[0]);
                            return;

                        }
                        break;
                    }
                }
                // Toast.makeText(getActivity(), String.valueOf(sideIndex)+previewText, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initSwipeRefreshLayout(){
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeRefreshLayout);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                Toast.makeText(getActivity(), "Refresh success", Toast.LENGTH_LONG).show();
//                swipeRefreshLayout.setRefreshing(false);
//            }
//        });
    }

    private void initActionBar(){
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.logo_du_big);
    }

    private void initSearchView(Menu menu){
        final  SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                contactsList.clear();
                contactsList.addAll(contactsController.getContactsByName(newText));
                adapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    private void initListView(){
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                List<Map<String, Integer>> icon2id = new ArrayList<>();
                icon2id.add(new HashMap<String, Integer>(){{put("icon", R.drawable.ic_call_black_24dp); put("id", SWIP_MENU_ITEM_CALL);}});
                icon2id.add(new HashMap<String, Integer>(){{put("icon", R.drawable.ic_sms_black_24dp); put("id",SWIP_MENU_ITEM_SMS);}});
                icon2id.add(new HashMap<String, Integer>(){{put("icon", R.drawable.ic_star_black_24dp); put("id", SWIP_MENU_ITEM_STAR);}});
                icon2id.add(new HashMap<String, Integer>(){{put("icon", R.drawable.ic_edit_black_24dp); put("id", SWIP_MENU_ITEM_EDIT);}});
                icon2id.add(new HashMap<String, Integer>(){{put("icon", R.drawable.ic_delete_forever_black_24dp); put("id", SWIP_MENU_ITEM_DELETE);}});

                int swipeMenuItem_width = 180;
                int swipeMenuItem_color =  R.color.colorPrimary;
                for (Map<String, Integer> swipeMenuItemMap: icon2id) {
                    SwipeMenuItem swipeMenuItem = new SwipeMenuItem(getContext());
                    swipeMenuItem.setBackground(swipeMenuItem_color);
                    swipeMenuItem.setWidth(swipeMenuItem_width);
                    swipeMenuItem.setIcon(swipeMenuItemMap.get("icon"));
                    swipeMenuItem.setId(swipeMenuItemMap.get("id"));
                    menu.addMenuItem(swipeMenuItem);
                }
            }
        };

        listView = (SwipeMenuListView) view.findViewById(R.id.contents_list);

        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        listView.setMenuCreator(creator);
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                activiteContactsIndex = position;
                Map<String,String> map = new HashMap<>();
                map = contactsList.get(position);
                switch (menu.getMenuItem(index).getId()){
                    case SWIP_MENU_ITEM_CALL:
                        callController.callPhone(map.get("number"));
                        break;
                    case SWIP_MENU_ITEM_SMS:
                        smsController.sendSms(map.get("number"), "");
                        break;
                    case SWIP_MENU_ITEM_STAR:
                        break;
                    case SWIP_MENU_ITEM_EDIT:
                        contactsController.actionEdit(Long.valueOf(map.get("id")));
                        break;
                    case SWIP_MENU_ITEM_DELETE:
                        alertDialog.show();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        listView.setSwipeDirection(SwipeMenuListView.DIRECTION_RIGHT);
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (activiteContactsIndex != -1){
                    View view = getViewByPosition(activiteContactsIndex, listView);
                    int color = ContextCompat.getColor(getContext(), R.color.colorActiviBarOff);
                    ((TextView)view.findViewById(R.id.txt_name)).setTextColor(Color.GRAY);
                    ((TextView)view.findViewById(R.id.txt_phone)).setTextColor(Color.GRAY);
                    CircleImageView circleImageView = (CircleImageView)view.findViewById(R.id.img_head);
                    circleImageView.setBorderColor(color);
                }

                activiteContactsIndex = -1;
                if(!listView.isSelected())
                    floatingActionMenu.close(true);
                else
                    floatingActionMenu.open(true);
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                activiteContactsIndex = position;
                int color = ContextCompat.getColor(getContext(), R.color.colorActiviBarOn);
                ((TextView)view.findViewById(R.id.txt_name)).setTextColor(color);
                ((TextView)view.findViewById(R.id.txt_phone)).setTextColor(color);
                CircleImageView circleImageView = (CircleImageView)view.findViewById(R.id.img_head);
                circleImageView.setBorderColor(color);
                floatingActionMenu.open(true);

            }
        });
    }

    private void initFloatingActionMenu(){
        floatingActionMenu = (FloatingActionMenu) view.findViewById(R.id.material_design_android_floating_action_menu);
        floatingActionMenu.setClosedOnTouchOutside(true);
        floatingActionMenu.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((listView.isSelected() || floatingActionMenu.isOpened())){
                    floatingActionMenu.close(true);
                }else{
                    contactsController.actionNew();
                }

            }
        });
        floatingActionButtonIds = new ArrayList<>();
        floatingActionButtonIds.add(R.id.material_design_floating_action_menu_call);
        floatingActionButtonIds.add(R.id.material_design_floating_action_menu_sms);
        floatingActionButtonIds.add(R.id.material_design_floating_action_menu_share);
        floatingActionButtonIds.add(R.id.material_design_floating_action_menu_details);

        for(Integer floatingActionButtonId : floatingActionButtonIds){
            FloatingActionButton fb = (FloatingActionButton)view.findViewById(floatingActionButtonId);
            fb.setOnClickListener(onClickListener);
        }
    }

    private void initViews(){

        Button button_contacts_cancel = (Button)dialogView.findViewById(R.id.button_contacts_cancel);
        button_contacts_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.hide();
            }
        });
        Button button_contacts_delete = (Button)dialogView.findViewById(R.id.button_contacts_delete);
        button_contacts_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,String> map = new HashMap<>();
                map = contactsList.get(activiteContactsIndex);
                int result = contactsController.deleteContacts(Long.valueOf(map.get("id")));
                if (result ==1) {
                    contactsList.remove(activiteContactsIndex);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
                }
                alertDialog.hide();
            }
        });
        initActionBar();
        initListView();
        initFloatingActionMenu();
        initIndexBar();

    }

    private void initDates(){
        contactsController = new ContactsController(getActivity());
        callController = new CallController(getActivity());
        smsController = new SmsController(getActivity());
        //contactsList = contactsController.getContactsList();
        contactsList = new ArrayList<>();


        adapter = new SimpleAdapter(view.getContext(), contactsList, R.layout.contacts_list_item,
                new String[]{"id", "name", "number"}, new int[]{R.id.img_head, R.id.txt_name, R.id.txt_phone});
        adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                if(view instanceof CircleImageView){
                    String idstring = String.valueOf(data);
                    Long id = Long.valueOf(idstring);
                    InputStream inputStream = contactsController.openPhoto(Long.valueOf(id));
                    Bitmap bmp;
                    if(inputStream != null){
                        bmp = BitmapFactory.decodeStream(inputStream);
                    }else{
                        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_face_black_24dp);
                    }
                    ((CircleImageView) view).setImageBitmap(bmp);

                    return  true;
                }
                Integer id = view.getId();
                return false;
            }
        });

        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                if(activiteContactsIndex == -1){
                    return;
                }
                Map<String,String> map = new HashMap<>();
                map = contactsList.get(activiteContactsIndex);
                switch (id){
                    case R.id.material_design_floating_action_menu_call:
                        callController.callPhone(map.get("number"));
                        break;
                    case R.id.material_design_floating_action_menu_sms:
                        smsController.sendSms(map.get("number"), "");
                        break;
                    case R.id.material_design_floating_action_menu_share:
                        break;
                    case R.id.material_design_floating_action_menu_details:
                        contactsController.actionView(Long.parseLong(map.get("id")));
                        break;
                    default:
                        break;
                }
                activiteContactsIndex = -1;
                floatingActionMenu.close(true);

            }
        };

    }

    public void initPinyin(){
        hanyu2pinyinMap = new HashMap<>();
        for (int i =0; i < contactsList.size(); i++){
            // 获取所有拼音
            Map<String, String> contactsMap = contactsList.get(i);
            String name = contactsMap.get("name");
            List<String> pin = new ArrayList<>();
            for(int j =0; j < name.length(); j++){
                String[] pinyin = PinyinHelper.toHanyuPinyinStringArray(name.charAt(j));
                if(pinyin != null){
                    pin.add(pinyin.toString());
                }else {
                    pin.add("");
                }
            }
            String[] p = PinyinHelper.toHanyuPinyinStringArray(name.charAt(0));
            if(p != null){
                Map<Integer, List<String>> con = new HashMap<>();
                con.put(i, pin);
                if(hanyu2pinyinMap.get(p[0]) == null){
                    List< Map<Integer, List<String>>> l = new ArrayList<>();
                    l.add(con);
                    hanyu2pinyinMap.put(p[0], l);
                }else{
                    Map<Integer, List<String>> con2 = new HashMap<>();
                    con2.put(i, pin);
                    hanyu2pinyinMap.get(p[0]).add(con2);
                }

            }
        }

    }

    @Override
    public android.support.v4.content.Loader onCreateLoader(int id, Bundle args) {
    String[] project = new String[projectMap.size()];
        projectMap.values().toArray(project);
        String sortOrder = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
    return Util.CursorLoaderCreate(getContext(), uri, project, null, null, sortOrder);
}

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        while (data.moveToNext())
        {
            Map<String, String> contactsItemMap = new HashMap<>();
            for (Map.Entry<String, String> entry : projectMap.entrySet()) {
                String key = entry.getKey();
                String key_uri = entry.getValue();
                String value = data.getString(data.getColumnIndex(key_uri));
                contactsItemMap.put(key, value);
            }
            Log.d("Test", contactsItemMap.toString());
            contactsList.add(contactsItemMap);
            adapter.notifyDataSetChanged();
        }
        data.close();
        ContactsComparator comp = new ContactsComparator();
        Collections.sort(contactsList, comp);
        initPinyin();
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader loader) {
        Log.d("Test", "onLoaderReset ");
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    private String[] alphabets() {
        final int length = 27;
        final String[] alphabets = new String[length];
        alphabets[0] = "#";
        char c = 'A';
        for (int i = 1; i < length; i++) {
            alphabets[i] = String.valueOf(c++);
        }
        return alphabets;
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            return listView.getChildAt(pos - firstListItemPosition);
        }
    }

}


