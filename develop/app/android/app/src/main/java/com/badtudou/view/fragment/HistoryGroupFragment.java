package com.badtudou.view.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringDef;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import android.widget.LinearLayout;
import android.widget.SimpleExpandableListAdapter;

import com.badtudou.model.FragmentViewClickListener;
import com.badtudou.controller.Ca3logController;
import com.badtudou.tudou.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by badtudou on 01/05/2017.
 */

public class HistoryGroupFragment extends Fragment {
    private Ca3logController ca3LogController;
    private List<Map<String, String>> ca3list;
    private List<Map<String, String>> group;
    private Map<String, List<Map<String, String>> > ca3listGroup;
    private List<List<Map<String, String>>> groupList;
    private ExpandableListAdapter adapter;
    private View view;
    private ExpandableListView listView;
    private FragmentViewClickListener fragmentViewClickListener;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_history_group, container, false);
        listView = (ExpandableListView)view.findViewById(R.id.history_group);
        //listView.setGroupIndicator(null);
        ca3LogController = new Ca3logController(getActivity());
        ca3list = new ArrayList<>();
        groupList = new ArrayList<>();
        group = new ArrayList<>();
        ca3listGroup = new HashMap<>();
        sordCa3istByDate();
        Log.d("Test", groupList.toString());
        adapter = new SimpleExpandableListAdapter(view.getContext(),
                group,
                R.layout.history_group_item,
                new String[]{"date", "count"}, new int[]{R.id.txt_date, R.id.txt_count},
                groupList,
                R.layout.history_list_item,
                new String[]{"number", "date"},
                new int[]{R.id.txt_number, R.id.txt_date});

        // expand
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

        listView.setAdapter(adapter);

        initViews();
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        return view;
    }

    private void initViews() {
        ImageButton button_add = (ImageButton) view.findViewById(R.id.button_add_contact);
        ImageButton button_switch_history_style = (ImageButton) view.findViewById(R.id.button_switch_history_style);

        button_add.setOnClickListener((FragmentViewClickListener)getActivity());
        button_switch_history_style.setOnClickListener((FragmentViewClickListener)getActivity());
        button_switch_history_style.setBackgroundResource(R.drawable.vector_drawable_group);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sordCa3istByDate(){
        List<Map<String, String>> ca3list = ca3LogController.getCallsList();
        Iterator<Map<String, String>> iter = ca3list.iterator();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date today = new Date(); //今天
        Date yesterday = new Date(); //昨天
        Date twoDaysBefore = new Date(); //前天
        Date aWeekBefore = new Date(); //一周前
        Date longtimeBefore  = new Date(); //更久前

        calendar.setTime(today);

        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
        yesterday = calendar.getTime();

        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 2);
        twoDaysBefore = calendar.getTime();

        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 7);
        aWeekBefore = calendar.getTime();

        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 15);
        longtimeBefore = calendar.getTime();

        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");

        String todayString =  dateFormat.format(today);//今天
        String yesterdayString = dateFormat.format(yesterday); //昨天
        String twoDaysBeforeString = dateFormat.format(twoDaysBefore); //前天
        String aWeekBeforeString = dateFormat.format(aWeekBefore); //一周前
        String longtimeBeforeString  = dateFormat.format(longtimeBefore); //更久前
        Map<String, String> mapGroup = new HashMap<>();
        Log.d("Test", dateFormat.format(yesterday));
        for(Map<String, String>  map : ca3list)    {
            String timeString = map.get("date");
            Long timestamp = Long.valueOf(timeString);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String date = formatter.format(timestamp);
            Log.d("Test", date);

            if (ca3listGroup.get(date) == null){
                ca3listGroup.put(date, new ArrayList<Map<String, String>>());
                ca3listGroup.get(date).add(map);

                mapGroup.put(date, String.valueOf(1));
            }else{
                ca3listGroup.get(date).add(map);
                Long count = Long.valueOf(mapGroup.get(date))+1;
                mapGroup.put(date, String.valueOf(count));
            }
        }

        for(String key: ca3listGroup.keySet()){
            String count = String.valueOf(ca3listGroup.get(key).size());
            Map<String, String> map = new HashMap<>();
            map.put("date", key);
            map.put("count", count);
            group.add(map);
            groupList.add(ca3listGroup.get(key));
        }
        Log.d("Test", ca3listGroup.toString());
        Log.d("Test", mapGroup.toString());
    }

}
