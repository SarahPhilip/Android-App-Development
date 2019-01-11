package com.sdsu.cs646.shameetha.assignment3new;

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
public class InstructorCommentsAdapter extends BaseAdapter {
    private List<Instructor> instructorComments;
    private final Context context;


    public InstructorCommentsAdapter(List<Instructor> instructorComments,
                                    Context context) {
        this.instructorComments = instructorComments;
        this.context = context;

    }

    @Override
    public int getCount() {
        return instructorComments.size();
    }

    @Override
    public Object getItem(int position) {
        return instructorComments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return instructorComments.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.comment_list_item, null);
            holder.comments = (TextView) convertView
                    .findViewById(R.id.comments);
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.commentNumber = (TextView) convertView.findViewById(R.id.commentNumber);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Instructor instructor = (Instructor) getItem(position);
        holder.comments.setText(instructor.getComment());
        holder.date.setText(instructor.getDate());




        return convertView;
    }

    public void refreshList(List<Instructor> professorComments) {
        this.instructorComments = professorComments;
    }

    private static class ViewHolder {
        TextView comments;
        TextView date;
        TextView commentNumber;
    }

}
