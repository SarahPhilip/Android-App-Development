package com.sdsu.cs646.shameetha.assignment3new;

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
public class InstructorListAdapter extends BaseAdapter {
    private List<Instructor> instructorList;
    private final Context context;

    public InstructorListAdapter(List<Instructor> instructorList, Context context) {
        this.instructorList = instructorList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return instructorList.size();
    }

    @Override
    public Object getItem(int position) {
        return instructorList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return instructorList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.instructor_list_item, null);
            holder.instructorNameText = (TextView) convertView
                    .findViewById(R.id.name);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Instructor instructor = (Instructor) getItem(position);
        holder.instructorNameText.setText(instructor.getFirstName() + " "
                + instructor.getLastName());
        return convertView;
    }

    public void refreshList(List<Instructor> professorList) {
        this.instructorList = professorList;
    }

    private static class ViewHolder {
        TextView instructorNameText;
    }
}
