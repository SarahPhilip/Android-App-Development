package com.sdsu.cs646.shameetha818455307.datepickerdemo;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Shameetha on 2/13/15.
 */
public class DesertListAdapter extends ArrayAdapter  {
    private Context context;
    private int index;
    public static final String PREFERENCE_NAMES = "ListFragmentData";

    public DesertListAdapter(Context context, List items) {
        super(context, android.R.layout.simple_list_item_1, items);
        this.context = context;
        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCE_NAMES, context.MODE_PRIVATE);
        index = sharedPref.getInt("selected_index",-1);

    }

    private class ViewHolder{
        TextView titleText;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        DesertListItem item = (DesertListItem)getItem(position);
        View viewToUse = null;
        LayoutInflater mInflater = (LayoutInflater) context .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
                viewToUse = mInflater.inflate(R.layout.desert_list_item, null);
            holder = new ViewHolder();
            holder.titleText = (TextView)viewToUse.findViewById(R.id.titleTextView);
            viewToUse.setTag(holder);
            if(position == index)
                viewToUse.setBackgroundColor(Color.GRAY);
            else
                viewToUse.setBackgroundColor(Color.TRANSPARENT);
        } else {
            viewToUse = convertView;
            holder = (ViewHolder) viewToUse.getTag();
        }
        holder.titleText.setText(item.getItemTitle());
        return viewToUse;
    }


}