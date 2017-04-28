package com.badtudou.tudou;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by badtudou on 27/04/2017.
 */

public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
//        ImageView imageView;
//        if (convertView == null) {
//            // if it's not recycled, initialize some attributes
//            imageView = new ImageView(mContext);
//            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            imageView.setPadding(8, 8, 8, 8);
//        } else {
//            imageView = (ImageView) convertView;
//        }
//
//        imageView.setImageResource(mThumbIds[position]);
//        return imageView;
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        convertView = layoutInflater.inflate(R.layout.gridview_item, null);
        TextView tvCity = (TextView)convertView.findViewById(R.id.tvCity);
        ImageView imageView = (ImageView)convertView.findViewById(R.id.iiLogo);

        tvCity.setText("TestCity");
        imageView.setImageResource(mThumbIds[position]);
        return convertView;
    }

    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.vector_drawable_qq, R.drawable.vector_drawable_wechat,
            R.drawable.vector_drawable_weibo, R.drawable.vector_drawable_twitter,
            R.drawable.vector_drawable_qq, R.drawable.vector_drawable_qq,
            R.drawable.vector_drawable_qq, R.drawable.vector_drawable_qq,
            R.drawable.vector_drawable_qq, R.drawable.vector_drawable_qq,
            R.drawable.vector_drawable_qq, R.drawable.vector_drawable_qq
    };
}
