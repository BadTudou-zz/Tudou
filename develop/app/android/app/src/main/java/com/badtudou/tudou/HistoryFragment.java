package com.badtudou.tudou;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HistoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;
    private Ca3log ca3log;
    private List<Map<String, String>> ca3list;
    private SimpleAdapter adapter;
    private ListView listView;

    private OnFragmentInteractionListener mListener;

    public HistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
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
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_history, container, false);
        ca3log = new Ca3log(getActivity());
        ca3list = ca3log.getCallsList();
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
                        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                        String[] projection = null;
                        String selection = ContactsContract.CommonDataKinds.Phone.NUMBER + " = ?";
                        Map<String, String> itemList = new HashMap<>();
                        String[] selectionArgs = {number};
                        String sortOrder = null;
                        itemList.put("name", ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                        itemList.put("number", ContactsContract.CommonDataKinds.Phone.NUMBER);
                        Map<String, String> map = new HashMap<String, String>();
                        try {
                            map = Util.ContentResolverSearch(getActivity().getContentResolver(), uri, projection, itemList, selection, selectionArgs, sortOrder).get(0);
                            Log.d("Test", map.toString());
                            ((TextView) view).setText(map.get("name"));
                            return true;
                        } catch (IndexOutOfBoundsException arExc){
                        }
                        break;

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
                        ((ImageView) view).setBackgroundResource(Ca3log.type2Resources.get(type));
                        return true;
                }

                return false;
            }
        });
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
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
}
