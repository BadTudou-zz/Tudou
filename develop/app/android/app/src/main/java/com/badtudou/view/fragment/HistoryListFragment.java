package com.badtudou.view.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.badtudou.controller.ContactsController;
import com.badtudou.model.FragmentViewClickListener;
import com.badtudou.controller.Ca3logController;
import com.badtudou.tudou.R;
import com.badtudou.util.Util;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HistoryListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HistoryListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;
    private Ca3logController ca3LogController;
    private ContactsController contactsController;
    private List<Map<String, String>> ca3list;
    private SimpleAdapter adapter;
    private ListView listView;

    private OnFragmentInteractionListener mListener;
    private FragmentViewClickListener fragmentViewClickListener;

    public HistoryListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistoryListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryListFragment newInstance(String param1, String param2) {
        HistoryListFragment fragment = new HistoryListFragment();
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the expandable_selector for this fragment
        view =  inflater.inflate(R.layout.fragment_history_list, container, false);
        initViews();
        ca3LogController = new Ca3logController(getActivity());
        contactsController = new ContactsController(getActivity());
        ca3list = ca3LogController.getCallsList();
        Log.d("Test", ca3list.toString());
        listView = (ListView)view.findViewById(R.id.call_list);
        adapter = new SimpleAdapter(view.getContext(), ca3list, R.layout.history_list_item,
                new String[]{"number", "date", "type"}, new int[]{R.id.txt_number, R.id.txt_date, R.id.img_type});
        adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                Integer id = view.getId();
                switch (id){
                    case R.id.txt_number:
                        String number = String.valueOf(data);
                        List<Map<String,String>> conList = contactsController.getContactsByNumber(number);
                        if(conList.size() == 1){
                            ((TextView) view).setText(conList.get(0).get("name"));
                        }else
                        {
                            ((TextView) view).setText(number);
                        }
                        return true;



                    case R.id.txt_date:
                        String timeString = String.valueOf(data);
                        Long timestamp = Long.valueOf(timeString);
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String date = formatter.format(timestamp);
                        ((TextView) view).setText(date);
                        return true;


                    case R.id.img_type:
                        Log.d("Test", "显示图片");
                        String typeString = String.valueOf(data);
                        Integer type = Integer.valueOf(typeString);
                        Log.d("Test", "type:"+typeString);
                        if(Ca3logController.type2Resources.get(type) == null){
                            ((ImageView) view).setBackgroundResource(R.drawable.outgoing_type);
                        }else{
                            ((ImageView) view).setBackgroundResource(Ca3logController.type2Resources.get(type));
                        }
                        return true;
                }

                return false;
            }
        });
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        return  view;
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
        if (context instanceof FragmentViewClickListener){
            Log.d("Test", "实现接口");
            fragmentViewClickListener = (FragmentViewClickListener) context;

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_history_list_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_history_group:
                fragmentViewClickListener.viewIdClick(id, null);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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

    private void initViews() {
//        ImageButton button_add = (ImageButton) view.findViewById(R.id.button_add_contact);
//        ImageButton button_switch_history_style = (ImageButton) view.findViewById(R.id.button_switch_history_style_group);
//
//        button_add.setOnClickListener((FragmentViewClickListener)getActivity());
//        button_switch_history_style.setOnClickListener((FragmentViewClickListener)getActivity());
    }

}
