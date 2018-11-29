package com.hlub.dev.project_01_reminder;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hlub.dev.project_01_reminder.adapter.TaskListAdapter;
import com.hlub.dev.project_01_reminder.dao.TasksDAO;
import com.hlub.dev.project_01_reminder.dao.TasksListDAO;
import com.hlub.dev.project_01_reminder.database.DatabaseManager;
import com.hlub.dev.project_01_reminder.model.Tasks;
import com.hlub.dev.project_01_reminder.model.TasksList;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TasksListActivity extends AppCompatActivity {
    private Toolbar toolbarList;
    private RecyclerView recycleviewList;
    private EditText edtNewListName;
    private EditText edtUpdateListName;
    private TaskListAdapter tasksListAdapter;
    List<TasksList> tasksLists;
    List<Tasks> tasks;

    private TasksListDAO tasksListDAO;
    private TasksDAO tasksDAO;
    private DatabaseManager databaseManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        toolbarList = findViewById(R.id.toolbarList);
        recycleviewList = findViewById(R.id.recycleviewList);

        //toolbar
        setSupportActionBar(toolbarList);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseManager = new DatabaseManager(this);
        tasksListDAO = new TasksListDAO(databaseManager);
        tasksDAO = new TasksDAO(databaseManager);

        tasksLists = new ArrayList<>();
        tasks = new ArrayList<>();

        getAllTasksList();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ic_list_add:
                showDialogNewList();
                break;
            case R.id.ic_list_find:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //ALERT DIALOG CREATE TASKLIST
    private void showDialogNewList() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.new_listing));

        //view
        final View view = getLayoutInflater().inflate(R.layout.dialog_new_list, null);
        builder.setView(view);
        edtNewListName = view.findViewById(R.id.edtNewListName);

        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String name = edtNewListName.getText().toString();
                if (name.length() == 0) {
                    edtNewListName.setError(getString(R.string.must_not_be_empty));
                    Toast.makeText(TasksListActivity.this, getString(R.string.must_not_be_empty), Toast.LENGTH_SHORT).show();
                } else {
                    Calendar calendar = Calendar.getInstance();
                    TasksList tasksList = new TasksList(calendar.getTimeInMillis(), name);
                    createTasksList(tasksList);
                    getAllTasksList();
                }


            }
        });
        builder.show();
    }

    //ALERT DIALOG DELETE TASKLIST
    public void showDialogDeleteList(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.delete_list));


        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteTasksList(position);
            }
        });
        builder.show();
    }

    //ALERT DIALOG UPDATE TASKLIST
    public void showDialogUpdateList(final String name, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.update_list));

        //view
        final View view = getLayoutInflater().inflate(R.layout.dialog_update_list, null);
        builder.setView(view);
        edtUpdateListName = view.findViewById(R.id.edtUpdateListName);

        edtUpdateListName.setText(tasksLists.get(position).getName());

        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(getString(R.string.update), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (edtUpdateListName.getText().toString().length() == 0) {
                    Toast.makeText(TasksListActivity.this, getString(R.string.must_not_be_empty), Toast.LENGTH_SHORT).show();
                } else {
                    updateTasksList(edtUpdateListName.getText().toString(), position);
                }
            }
        });
        builder.show();
    }

    public void getAllTasksList() {
        tasksLists = tasksListDAO.getAllTasksList();
        tasksListAdapter = new TaskListAdapter(tasksLists, this);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        recycleviewList.setLayoutManager(manager);
        recycleviewList.setAdapter(tasksListAdapter);
        tasksListAdapter.notifyDataSetChanged();

    }

    public void deleteTasksList(final int position) {
        final TasksList tl = tasksLists.get(position);

        final List<Tasks> tasks = tasksDAO.getAllTasksByListId(tl.getId());

        if (tasks.size() > 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.delete_list));
            builder.setMessage("Do you want to delete " + tasks.size() + " tasks");

            builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    for (Tasks t : tasks) {
                        tasksDAO.deleteTasks(t);
                    }
                    tasksListDAO.deleteTasksList(tl);
                    Toast.makeText(TasksListActivity.this, getString(R.string.deleted_data), Toast.LENGTH_SHORT).show();
                    tasksLists.remove(position);
                    tasksListAdapter.notifyItemRemoved(position);

                }
            });
            builder.show();
        } else {
            //delete DB
            tasksListDAO.deleteTasksList(tl);
            Toast.makeText(TasksListActivity.this, getString(R.string.deleted_data), Toast.LENGTH_SHORT).show();
            tasksLists.remove(position);
            tasksListAdapter.notifyItemRemoved(position);
        }

    }

    public void updateTasksList(String name, int position) {
        TasksList tl = tasksLists.get(position);
        tl.setName(name);

        //update DB
        tasksListDAO.updateTasksList(tl);

        tasksLists.set(position, tl);
        tasksListAdapter.notifyItemChanged(position);
    }

    public void createTasksList(TasksList tasksList) {
        tasksListDAO.insertTasksList(tasksList);
    }


}
