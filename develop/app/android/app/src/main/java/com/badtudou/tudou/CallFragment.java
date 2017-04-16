package com.badtudou.tudou;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CallFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CallFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CallFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose  names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View view;
    private Map<String, Integer> strMapInt;
    private String phone ="";
    private EditText editTextPhone;

    private OnFragmentInteractionListener mListener;

    public CallFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CallFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CallFragment newInstance(String param1, String param2) {
        CallFragment fragment = new CallFragment();
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
        view = inflater.inflate(R.layout.fragment_call, container, false);
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
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        Log.d("Test", "Click callFragment button");
        Integer id = v.getId();
        String idName = v.getResources().getResourceEntryName(id);
        Character key = idName.charAt(idName.length()-1);

        if(strMapInt.containsKey(key.toString())){
            // 0-9, *, #
            Log.d("Test", "has"+":"+key);
            if(key.equals('r'))
                key = '*';

            if(key.equals('h'))
                key = '#';

            phone += key;
            editTextPhone.setText(phone);
        }else
        {
            switch (key.toString()){
                // call
                case "l":
                    Log.d("Test", "call button");
                    new Call((Activity)view.getContext()).callPhone(phone);
                    break;
                // delete
                case "e":
                    phone = phone.substring(0, phone.length()-1);
                    editTextPhone.setText(phone);
                    break;
            }
            // Call, Delete
            Log.d("Test", "has not"+ key);
        }
        //if(strMapInt.)
        //switch (v.)
    }

    private void initViews(){
        strMapInt = new HashMap<String, Integer>();
        strMapInt.put("0", R.id.Button0);
        strMapInt.put("1", R.id.Button1);
        strMapInt.put("2", R.id.Button2);
        strMapInt.put("3", R.id.Button3);
        strMapInt.put("4", R.id.Button4);
        strMapInt.put("5", R.id.Button5);
        strMapInt.put("6", R.id.Button6);
        strMapInt.put("7", R.id.Button7);
        strMapInt.put("8", R.id.Button8);
        strMapInt.put("9", R.id.Button9);
        strMapInt.put("r", R.id.ButtonStar);
        strMapInt.put("h", R.id.ButtonHash);
        strMapInt.put("call", R.id.ButtonCall);
        strMapInt.put("delete", R.id.ButtonDelete);

        // set buttons click listener
        for (Map.Entry<String, Integer> entry : strMapInt.entrySet()) {
            ImageButton imageButton = (ImageButton)view.findViewById(entry.getValue());
            imageButton.setOnClickListener(this);
        }

        editTextPhone = (EditText)view.findViewById(R.id.CallEditTextPhoneNumber);
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
