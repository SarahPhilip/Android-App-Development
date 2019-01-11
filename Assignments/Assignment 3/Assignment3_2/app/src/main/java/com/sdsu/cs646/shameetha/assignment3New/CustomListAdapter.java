package com.sdsu.cs646.shameetha.assignment3New;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Shameetha on 3/12/15.
 */
public class CustomListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Professor> mProfessorList;

    public CustomListAdapter(Activity activity, List<Professor> mProfessorList) {
        this.activity = activity;
        this.mProfessorList = mProfessorList;
    }

    @Override
    public int getCount() {
        return mProfessorList.size();
    }

    @Override
    public Object getItem(int location) {
        return mProfessorList.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row, null);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        Professor professor = mProfessorList.get(position);
        name.setText(professor.getFirstName() + " " + professor.getLastName());
        return convertView;
    }
}
