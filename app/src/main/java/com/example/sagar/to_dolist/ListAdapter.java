package com.example.sagar.to_dolist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;


public class ListAdapter extends ArrayAdapter {

    private List<TaskEntry> mTaskList;


    ListAdapter(Context context, List<TaskEntry> taskEntries) {
        super(context, R.layout.task_layout);
        this.mTaskList = taskEntries;
    }


    @Override
    public int getCount() {
        return mTaskList.size();
    }


    @Override
    public Object getItem(int position) {
        return position;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.task_layout, parent, false);

            viewHolder.tvDescription = convertView.findViewById(R.id.taskDescription);
            viewHolder.tvUpdatedAt = convertView.findViewById(R.id.taskUpdatedAt);
            viewHolder.tvPriority = convertView.findViewById(R.id.priorityTextView);


            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        TaskEntry taskEntry = mTaskList.get(position);


        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd/MM/yyyy hh:mm:ss", Locale.getDefault());


        viewHolder.tvDescription.setText(taskEntry.getDescription());
        viewHolder.tvUpdatedAt.setText(dateFormat.format(taskEntry.getUpdatedAt()));
        viewHolder.tvPriority.setText(taskEntry.getPriority());

        return convertView;
    }


    private class ViewHolder {
        TextView tvDescription;
        TextView tvUpdatedAt;
        TextView tvPriority;
    }


    // END
}
