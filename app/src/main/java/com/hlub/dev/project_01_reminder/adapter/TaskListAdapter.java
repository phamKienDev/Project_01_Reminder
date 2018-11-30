package com.hlub.dev.project_01_reminder.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.hlub.dev.project_01_reminder.TasksListActivity;
import com.hlub.dev.project_01_reminder.R;
import com.hlub.dev.project_01_reminder.dao.TasksDAO;
import com.hlub.dev.project_01_reminder.database.DatabaseManager;
import com.hlub.dev.project_01_reminder.model.Tasks;
import com.hlub.dev.project_01_reminder.model.TasksList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ListHolder> implements AdapterView.OnItemSelectedListener, Filterable {
    List<TasksList> list;
    TasksListActivity listActivity;
    DatabaseManager databaseManager;
    TasksDAO tasksDAO;

    String listItem;
    List<TasksList> tlSearch;

    public TaskListAdapter(List<TasksList> list, TasksListActivity listActivity) {
        this.list = list;
        this.listActivity = listActivity;
        databaseManager = new DatabaseManager(listActivity);
        tasksDAO = new TasksDAO(databaseManager);
        tlSearch = new ArrayList<>(list);
    }


    @NonNull
    @Override
    public ListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListHolder holder, final int position) {
        final TasksList taskList = tlSearch.get(position);
        List<Tasks> tasks = tasksDAO.getAllTasksByListId(taskList.getId());

        holder.tvListName.setText(taskList.getName());
        holder.imgItemEditList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listActivity.showDialogUpdateList(taskList.getName().toString(), position);
            }
        });
        holder.tvQuantityInList.setText(String.valueOf(tasks.size()) + " tasks");
        holder.imgItemDeleteList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listActivity.showDialogDeleteList(position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return tlSearch.size();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        listItem = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<TasksList> tl = new ArrayList<>();
                Log.d("size", list.size() + "");
                if (constraint == null || constraint.length() == 0) {
                    tl.addAll(list);
                    Log.d("size1", list.size() + "");
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for (TasksList tasksList : list) {
                        if (tasksList.getName().toLowerCase().contains(filterPattern)) {
                            tl.add(tasksList);
                            Log.e("Id", tasksList.getName().toLowerCase());
                            Log.e("filter", filterPattern);
                        }
                    }
                }
                Log.e("search list", String.valueOf(tl.size()));
                FilterResults results = new FilterResults();
                results.values = tl;

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                tlSearch.clear();
                tlSearch.addAll((List) results.values);
                notifyDataSetChanged();
            }
        };
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
