package com.badtudou.tudou;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.InputStream;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ContactsListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ContactsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactsListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Contacts contacts;
    private List<Map<String,String>> contactsList;
    private SimpleAdapter adapter;
    private View view;
    private ListView listView;

    private OnFragmentInteractionListener mListener;
    private ButtonClickListener buttonClickListener;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_contacts_list, container, false);
        listView = (ListView)view.findViewById(R.id.contents_list);
        //test
        contacts = new Contacts(getActivity());
        contactsList = contacts.getContactsList();

        adapter = new SimpleAdapter(view.getContext(), contactsList, R.layout.contacts_list_item,
                new String[]{"id", "name", "number"}, new int[]{R.id.img_head, R.id.txt_name, R.id.txt_phone});
        adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                if(view instanceof ImageView){
                    String idstring = String.valueOf(data);
                    Long id = Long.valueOf(idstring);
                    InputStream inputStream = contacts.openPhoto(Long.valueOf(id));
                    Bitmap bmp;
                    if(inputStream != null){
                        bmp = BitmapFactory.decodeStream(inputStream);
                    }else{
                        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.vector_drawable_earth);
                    }
                    ((ImageView) view).setImageBitmap(bmp);

                    Log.d("Test", "show photo"+String.valueOf(data));
                    return  true;
                }
                return false;
            }
        });
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        initViews();
        return view;


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        if (context instanceof  ButtonClickListener){
            Log.d("Test", "实现接口");
            buttonClickListener = (ButtonClickListener) context;

        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void initViews(){
        ImageButton button_add = (ImageButton)view.findViewById(R.id.button_add_contact);
        ImageButton button_switch_contact_style = (ImageButton)view.findViewById(R.id.button_switch_contact_style);

        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClickListener.showMessage(R.id.button_add_contact);
            }
        });

        button_switch_contact_style.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClickListener.showMessage(R.id.button_switch_contact_style);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Test", contactsList.get(position).toString());
                Long personId = Long.parseLong(contactsList.get(position).get("id"));
                if (contacts.openPhoto(personId) != null){
                    Log.d("Test", "has photo");
                }else{
                    Log.d("Test", "has no photo");
                }

                Uri personUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, personId);// info.id联系人ID
                Intent intent = new Intent(new Intent(Intent.ACTION_VIEW, personUri));
                startActivity(intent);
            }
        });

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
