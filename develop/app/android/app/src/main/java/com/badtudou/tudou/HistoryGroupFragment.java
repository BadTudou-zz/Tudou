package com.badtudou.tudou;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by badtudou on 01/05/2017.
 */

public class HistoryGroupFragment extends Fragment {
    private List< List<Map<String, String>> > ca3listGroup;
    private View view;
    private HistoryListFragment.OnFragmentInteractionListener mListener;
    private ButtonClickListener buttonClickListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_history_group, container, false);
        initViews();
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HistoryListFragment.OnFragmentInteractionListener) {
            mListener = (HistoryListFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        if (context instanceof  ButtonClickListener){
            Log.d("Test", "实现接口");
            buttonClickListener = (ButtonClickListener) context;

        }
    }

    private void initViews() {
        ImageButton button_add = (ImageButton) view.findViewById(R.id.button_add_contact);
        ImageButton button_switch_history_style = (ImageButton) view.findViewById(R.id.button_switch_history_style);

        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClickListener.showMessage(R.id.button_add_contact);
            }
        });

        button_switch_history_style.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClickListener.showMessage(R.id.button_switch_history_style);
            }
        });

    }

    private void sordCa3istByDate(){
        List<Map<String, String>> ca3list = new ArrayList<>();
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
        Log.d("Test", dateFormat.format(yesterday));
        while(iter.hasNext())
        {
            iter.next();
            //System.out.println(iter.next());
        }
    }
}
