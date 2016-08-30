/**
 * File Name: 		BiteAdapter.java
 * Description：
 * Author:			Luke Huang
 * Create Time: 	2015-7-22 下午5:21:35
 * <p>
 * For the SLCD Project
 * Copyright © 2015 Donica.cn All rights reserved
 */
package cn.donica.slcd.dmanager.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cn.donica.slcd.dmanager.R;

/**
 * Class:		BiteAdapter
 * Description:
 */
public class BiteAdapter extends BaseAdapter {
    public static final int INVALID_POSITION = -1;

    private LayoutInflater mInflater;
    private ArrayList<String> mItemList;
    // private Bitmap mIcon;
    private int mPosition = INVALID_POSITION;

    public BiteAdapter(Context context, ArrayList<String> list) {
        mInflater = LayoutInflater.from(context);
        mItemList = list;
    }

    /**
     * (non-Javadoc)
     *
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public int getCount() {
        return mItemList.size();
    }

    /**
     * (non-Javadoc)
     *
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public Object getItem(int position) {
        return mItemList.get(position);
    }

    /**
     * (non-Javadoc)
     *
     * @see android.widget.Adapter#getItemId(int)
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * (non-Javadoc)
     *
     * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            // create a view item
            convertView = mInflater.inflate(R.layout.list_item, null);

            // save this item
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(R.id.item_name);

            // use a tag to indicate it
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        convertView.setSelected(mPosition == position);
        convertView.setPressed(mPosition == position);

        viewHolder.textView.setText(mItemList.get(position));
        if (mPosition == position) {
            viewHolder.textView.setBackgroundColor(Color.YELLOW);
            viewHolder.textView.setTextColor(Color.WHITE);
        } else {
            viewHolder.textView.setBackgroundColor(Color.TRANSPARENT);
            viewHolder.textView.setTextColor(Color.BLACK);
        }
        return convertView;
    }

    public void setSelectedPosition(int position) {
        this.mPosition = position;
        notifyDataSetInvalidated();
    }

    class ViewHolder {
        TextView textView;
    }
}
