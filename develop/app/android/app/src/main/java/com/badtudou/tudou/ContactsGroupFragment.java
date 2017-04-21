package com.badtudou.tudou;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

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

    private Contacts contacts;
    private List<Map<String,String>> groupList;
    private SimpleAdapter adapter;
    private View view;
    private ListView listView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private ButtonClickListener buttonClickListener;

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
        view = inflater.inflate(R.layout.fragment_contacts_group, container, false);
        listView = (ListView)view.findViewById(R.id.contents_group);
        //test
        contacts = new Contacts(getActivity());
        groupList = contacts.getGroupsList();

        adapter = new SimpleAdapter(view.getContext(), groupList, R.layout.group_list_item,
                new String[]{"name", "id"}, new int[]{R.id.txt_group, R.id.txt_group_memberSize});
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
        TextView textView_title = (TextView)view.findViewById(R.id.text_group_or_contacts);

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

        textView_title.setText(R.string.title_group);
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
