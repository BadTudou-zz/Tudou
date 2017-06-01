package com.badtudou.model;

import android.view.View;

import java.util.Map;

/**
 * Created by badtudou on 21/04/2017.
 */

public interface FragmentViewClickListener extends View.OnClickListener{
    public void viewIdClick(int id, Map map);
}
