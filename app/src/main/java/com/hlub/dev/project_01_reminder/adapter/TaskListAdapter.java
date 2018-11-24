package com.hlub.dev.project_01_reminder.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hlub.dev.project_01_reminder.TasksListActivity;
import com.hlub.dev.project_01_reminder.R;
import com.hlub.dev.project_01_reminder.model.TasksList;

import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ListHolder> {
    List<TasksList> list;
    TasksListActivity listActivity;

    public TaskListAdapter(List<TasksList> list, TasksListActivity listActivity) {
        this.list = list;
        this.listActivity = listActivity;
    }


    @NonNull
    @Override
    public ListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListHolder holder, final int position) {
        final TasksList taskList=list.get(position);
        holder.tvListName.setText(taskList.getName());
        holder.imgItemEditList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listActivity.showDialogUpdateList(taskList.getName().toString(),position);
            }
        });
        holder.imgItemDeleteList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listActivity.showDialogDeleteList(position);
            }
        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ListHolder extends RecyclerView.ViewHolder {
        private TextView tvListName;
        private TextView tvQuantityInList;
        private ImageView imgItemEditList;
        private ImageView imgItemDeleteList;

        public ListHolder(View itemView) {
            super(itemView);
            tvListName = itemView.findViewById(R.id.tvListName);
            tvQuantityInList = itemView.findViewById(R.id.tvQuantityInList);
            imgItemEditList = itemView.findViewById(R.id.imgItemEditList);
            imgItemDeleteList = itemView.findViewById(R.id.imgItemDeleteList);


        }
    }
}
