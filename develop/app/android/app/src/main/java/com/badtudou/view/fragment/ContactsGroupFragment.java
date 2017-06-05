package com.badtudou.view.fragment;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.SimpleExpandableListAdapter;

import com.badtudou.controller.ContactsController;
import com.badtudou.controller.GroupController;
import com.badtudou.model.FragmentViewClickListener;
import com.badtudou.tudou.R;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ContactsGroupFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ContactsGroupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactsGroupFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    static public final int REQUEST_OK = -1;

    private GroupController groupController;
    private ContactsController contactsController;
    private List<Map<String,String>> groupList;
    private List<List<Map<String, String>>> contactsList;
    private ExpandableListAdapter adapter;
    private View view;
    private FloatingActionMenu floatingActionMenu;
    private List<Integer> floatingActionButtonIds;
    private View.OnClickListener onClickListener;
    private SearchView searchView;
    private ExpandableListView listView;

    private FragmentViewClickListener fragmentViewClickListener;
    private int activiteGroupIndex = -1;
    private int activiteItemIndex = -1;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ContactsGroupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContactsGroupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactsGroupFragment newInstance(String param1, String param2) {
        ContactsGroupFragment fragment = new ContactsGroupFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        // Inflate the expandable_selector for this fragment
        view = inflater.inflate(R.layout.fragment_contacts_group, container, false);
        listView = (ExpandableListView)view.findViewById(R.id.contents_group);
        //test

        initDate();

        adapter = new SimpleExpandableListAdapter(
                view.getContext(),
                groupList,
                R.layout.contacts_group_item,
                new String[]{"title", "count"},
                new int[]{R.id.txt_group, R.id.txt_group_memberSize},
                contactsList,
                R.layout.contacts_list_item,
                new String[]{"name", "number"}, new int[]{R.id.txt_name, R.id.txt_phone});
        listView.setAdapter(adapter);
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                ImageView imageview = (ImageView)v.findViewById(R.id.button_history_expand_or_fold);
                activiteItemIndex = -1;
                if (parent.isGroupExpanded(groupPosition)) {
                    imageview.setImageResource(R.drawable.vector_drawable_down);
                } else{
                    imageview.setImageResource(R.drawable.vector_drawable_up);
                }
                return false;
            }
        });

        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                activiteGroupIndex = groupPosition;
                activiteItemIndex = childPosition;
                Long personId = Long.valueOf(contactsList.get(groupPosition).get(childPosition).get("id"));
                Uri personUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, personId);// info.id联系人ID
                Intent intent = new Intent(new Intent(Intent.ACTION_VIEW, personUri));
                //startActivity(intent);
                Map<Object , Object> map = new HashMap<Object, Object>();
                map.put("id", personId);
                floatingActionMenu.open(true);
                return false;
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                activiteGroupIndex = position;
                Log.d("Test", "item long click");
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(intent, ContactsController.REQUEST_SELECT_CONTACT);
                }
                //contactsController.selectContact();
                return false;
            }
        });

//
        initViews();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        fragmentViewClickListener = (FragmentViewClickListener)context;
        super.onAttach(context);
    }


    private void initViews(){

    }

    private void initDate(){
        groupController = new GroupController(getActivity());
        contactsController = new ContactsController(getActivity());
        groupList = groupController.getGroupsList();
        contactsList = new ArrayList<>();
        for(Map<String, String>  map : groupList){
            List<Map<String, String>> idList;
            List<Map<String, String>> contacts = new ArrayList<>();
            String idString = map.get("id");
            Long id = Long.valueOf(idString);
            idList = groupController.getMembership(id);
            for(Map<String, String> mapId : idList){
                String contactsIdString = mapId.get("id");
                Long contactsId= Long.valueOf(contactsIdString);
                mapId.put("number", contactsController.getContactsById(contactsId).get("number"));
            }
            contactsList.add(idList);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        floatingActionMenu = (FloatingActionMenu) view.findViewById(R.id.material_design_android_floating_action_menu);
        floatingActionMenu.setClosedOnTouchOutside(true);
        floatingActionMenu.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((listView.isSelected() || floatingActionMenu.isOpened())){
                    floatingActionMenu.close(true);
                }else{
                    groupController.actionNew();
                }

            }
        });

        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                if(activiteItemIndex == -1){
                    return;
                }
                Map<String,String> map = new HashMap<>();
                map = contactsList.get(activiteGroupIndex).get(activiteItemIndex);
                Log.d("Test", map.toString());
                switch (id){
                    case R.id.material_design_floating_action_menu_call:
                        //callController.callPhone(map.get("number"));
                        break;
                    case R.id.material_design_floating_action_menu_sms:
                        //smsController.sendSms(map.get("number"), "");
                        break;
                    case R.id.material_design_floating_action_menu_share:
                        break;
                    case R.id.material_design_floating_action_menu_details:
                        contactsController.actionView(Long.parseLong(map.get("id")));
                        break;
                    default:
                        break;
                }
                activiteItemIndex = -1;
                floatingActionMenu.close(true);

            }
        };

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_contacts_group_menu, menu);
//        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
////                contactsList.clear();
////                contactsList.addAll(contactsController.getContactsByName(newText));
////                adapter.notifyDataSetChanged();
//                return false;
//            }
//        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_contacts_list:
                fragmentViewClickListener.viewIdClick(id, null);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ContactsController.REQUEST_SELECT_CONTACT && resultCode == RESULT_OK) {
            Uri contactUri = data.getData();
            Log.d("Test", contactUri.toString());
            long id = ContentUris.parseId(contactUri);
            long groupId = Long.parseLong(groupList.get(activiteGroupIndex).get("id"));
            Log.d("Test", "Group:"+String.valueOf(groupId)+",Contacts:"+String.valueOf(id));
            groupController.addMembership(groupId, id);
            // Do something with the selected contact at contactUri
        }
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
}
