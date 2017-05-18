package com.badtudou.view.fragment;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SimpleExpandableListAdapter;

import com.badtudou.controller.ContactsController;
import com.badtudou.controller.GroupController;
import com.badtudou.model.FragmentViewClickListener;
import com.badtudou.tudou.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    private GroupController groupController;
    private ContactsController contactsController;
    private List<Map<String,String>> groupList;
    private List<List<Map<String, String>>> contactsList;
    private ExpandableListAdapter adapter;
    private View view;
    private ExpandableListView listView;

    private FragmentViewClickListener fragmentViewClickListener;
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

        // Inflate the expandable_selector for this fragment
        view = inflater.inflate(R.layout.fragment_contacts_group, container, false);
        listView = (ExpandableListView)view.findViewById(R.id.contents_group);
        //test
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
                Long contactsId= Long.valueOf(idString);
                contacts.add(contactsController.getContactsById(contactsId));
            }
            contactsList.add(contacts);
        }

        Log.d("Test", groupList.toString());

        adapter = new SimpleExpandableListAdapter(
                view.getContext(),
                groupList,
                R.layout.group_list_item,
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
                Long personId = Long.valueOf(contactsList.get(groupPosition).get(childPosition).get("id"));
                Uri personUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, personId);// info.id联系人ID
                Intent intent = new Intent(new Intent(Intent.ACTION_VIEW, personUri));
                //startActivity(intent);
                v.setId(R.id.contents_list);
                Map<Object , Object> map = new HashMap<Object, Object>();
                map.put("id", personId);
                fragmentViewClickListener.viewClick(v, map);

                return false;
            }
        });
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        initViews();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        fragmentViewClickListener = (FragmentViewClickListener)context;
        super.onAttach(context);
    }


    private void initViews(){
        ImageButton button_add = (ImageButton)view.findViewById(R.id.button_add_contact);
        ImageButton button_switch_contact_style = (ImageButton)view.findViewById(R.id.button_switch_contact_style_list);

        button_add.setOnClickListener((FragmentViewClickListener)getActivity());
        button_switch_contact_style.setOnClickListener((FragmentViewClickListener)getActivity());

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
