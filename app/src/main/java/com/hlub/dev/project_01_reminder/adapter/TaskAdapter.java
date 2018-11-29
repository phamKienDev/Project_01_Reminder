package com.hlub.dev.project_01_reminder.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hlub.dev.project_01_reminder.R;
import com.hlub.dev.project_01_reminder.dao.TasksDAO;
import com.hlub.dev.project_01_reminder.database.DatabaseManager;
import com.hlub.dev.project_01_reminder.model.Tasks;

import java.text.SimpleDateFormat;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> {

    private Context context;
    private List<Tasks> tasks;
    private TasksDAO tasksDAO;
    private DatabaseManager databaseManager;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss ");

    public TaskAdapter(Context context, List<Tasks> tasks) {
        this.context = context;
        this.tasks = tasks;
        databaseManager=new DatabaseManager(context);
        tasksDAO=new TasksDAO(databaseManager);
    }

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, final int position) {
        Tasks task = tasks.get(position);
        holder.tvTaskName.setText(task.getNameTask());
        holder.tvTaskTime.setText(sdf.format(task.getTime()));
        holder.relativeLayoutTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTasks(position);
            }
        });
    }

    public void deleteTasks(int position){
        Tasks t = tasks.get(position);

        //delete DB
        tasksDAO.deleteTasks(t);

        tasks.remove(position);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public class TaskHolder extends RecyclerView.ViewHolder {
        private RelativeLayout relativeLayoutTask;
        private TextView tvTaskName;
        private TextView tvTaskTime;

        public TaskHolder(View itemView) {
            super(itemView);
            relativeLayoutTask = itemView.findViewById(R.id.relativeLayoutTask);
            tvTaskName = itemView.findViewById(R.id.tvTaskName);
            tvTaskTime = itemView.findViewById(R.id.tvTaskTime);

        }
    }
}
