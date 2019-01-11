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
 * Created by Shameetha on 3/13/15.
 */
public class ProfessorCommentsAdapter extends BaseAdapter {
    private List<Professor> instructorComments;
    private final Context context;
    List<Comments> rowItems;

    public ProfessorCommentsAdapter(Context context, List<Comments> rowItems) {
        this.context = context;
        this.rowItems = rowItems;
    }

    public ProfessorCommentsAdapter(List<Professor> instructorComments, Context context, List<Comments> rowItems) {
        this.instructorComments = instructorComments;
        this.context = context;
        this.rowItems = rowItems;
    }

    private class ViewHolder {
        TextView date;
        TextView comment;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.comment_list_item, null);
            holder = new ViewHolder();
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.comment = (TextView) convertView.findViewById(R.id.comments);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Comments rowItem = (Comments) getItem(position);

        holder.date.setText(rowItem.getDate());
        holder.comment.setText(rowItem.getText());

        return convertView;
    }

    @Override
    public int getCount() {
        return rowItems.size();
    }

    @Override
    public Object getItem(int position) {
        return rowItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return rowItems.indexOf(getItem(position));
    }

    public void refreshList(List<Professor> professorComments) {
        this.instructorComments = professorComments;
    }


}
