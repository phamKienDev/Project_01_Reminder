package com.hlub.dev.project_01_reminder.fragment;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.hlub.dev.project_01_reminder.TasksListActivity;
import com.hlub.dev.project_01_reminder.R;
import com.hlub.dev.project_01_reminder.adapter.TaskAdapter;
import com.hlub.dev.project_01_reminder.dao.TasksDAO;
import com.hlub.dev.project_01_reminder.dao.TasksListDAO;
import com.hlub.dev.project_01_reminder.database.DatabaseManager;
import com.hlub.dev.project_01_reminder.model.Tasks;
import com.hlub.dev.project_01_reminder.model.TasksList;
import com.hlub.dev.project_01_reminder.broadcast_receiver.AlarmReceiver;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class TasksFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private Toolbar toobarTasks;
    private ExpandableListView expandableTasks;
    private FloatingActionButton flButtonTask;
    private EditText edtIWanTo;
    private ImageView imgCalendar;
    private Spinner spinnerList;
    String ngay, thang, nam, gio, phut;
    private TextView tvQuantityToday;
    private TextView tvQuantityTomorrow;
    private TextView tvQuantityUpcoming;


    private RecyclerView recycleviewToday;
    private RecyclerView recycleviewTomorrow;

    List<TasksList> tasksListList;
    List<Tasks> tasksToday;
    List<Tasks> tasksTomorrow;
    List<Tasks> tasksUpcoming;

    TaskAdapter taskAdapter;

    PendingIntent pendingIntent;
    AlarmManager alarmManager;
    Calendar calendar;

    TasksList tasksList;

    private DatabaseManager databaseManager;
    private TasksDAO tasksDAO;
    private TasksListDAO tasksListDAO;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks, null);

        anhxa(view);

        //list
        tasksListList = new ArrayList<>();
        tasksToday = new ArrayList<>();
        tasksTomorrow = new ArrayList<>();
        tasksUpcoming = new ArrayList<>();

        databaseManager = new DatabaseManager(getActivity());
        tasksDAO = new TasksDAO(databaseManager);
        tasksListDAO = new TasksListDAO(databaseManager);

        calendar = Calendar.getInstance();

        //ALARMMANAGER
        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        //toolbar
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toobarTasks);
        setHasOptionsMenu(true);
        toobarTasks.setTitle("ALL TASKS");

        getTasksToday();
        getTasksTomorrow();

        //floatingbutton
        flButtonTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogNewTask();
            }
        });
        return view;
    }

    private void showDialogNewTask() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //view
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_new_task, null);
        edtIWanTo = view.findViewById(R.id.edtIWanTo);
        imgCalendar = view.findViewById(R.id.imgCalendar);
        spinnerList = view.findViewById(R.id.spinnerList);
        builder.setView(view);

        //load spinner
        ArrayAdapter<TasksList> adapter = loadSpinner();
        spinnerList.setAdapter(adapter);
        spinnerList.setOnItemSelectedListener(this);

        imgCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        builder.setPositiveButton(getString(R.string.put), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nameTask = edtIWanTo.getText().toString();//tên nhiệm vụ
                Calendar now = Calendar.getInstance();
                long timeNow = now.getTimeInMillis();//thời gian hiện tại
                long timer = calendar.getTimeInMillis();//thời điểm đặt báo thức
                // k đc phép đặt thời điểm nhiệm vụ đã kết thúc
                if (timer < timeNow) {
                    Toast.makeText(getActivity(), getString(R.string.The_time_you_selected_has_ended), Toast.LENGTH_SHORT).show();
                } else {
                    Tasks tasks = new Tasks(nameTask, timer, tasksList.getId(), "", "", timeNow, 0);
                    tasksDAO.insertTasks(tasks);
                    getTasksToday();
                    getTasksTomorrow();

                }

            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }

    public void timer(List<Tasks> tasklist) {
        for (Tasks task : tasklist) {
            Intent intent = new Intent(getActivity(), AlarmReceiver.class);
            //HẸN GIỜ
            intent.putExtra("extra", "on");
            //vẫn tồn tại khi thoát ứng dụng
            pendingIntent = PendingIntent.getBroadcast(
                    getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            alarmManager.set(AlarmManager.RTC_WAKEUP, task.getTime(), pendingIntent);
        }

    }


    public void showDatePickerDialog() {

        //SET THỜI GIAN CALENDAR

        final int day = calendar.get(Calendar.DATE);
        final int month = calendar.get(Calendar.MONTH);
        final int year = calendar.get(Calendar.YEAR);
        final DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(final DatePicker datePicker, int i, int i1, int i2) {
                final SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                final TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        //set calendar
                        calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                        calendar.set(Calendar.MONTH, datePicker.getMonth());
                        calendar.set(Calendar.YEAR, datePicker.getYear());
                        calendar.set(Calendar.HOUR_OF_DAY, view.getCurrentHour());
                        calendar.set(Calendar.MINUTE, view.getCurrentMinute());

                        int hh = view.getCurrentHour();
                        int mm = view.getCurrentMinute();

                        gio = String.valueOf(hh);
                        phut = String.valueOf(mm);

                        if (hh > 12) {
                            gio = String.valueOf(hh - 12);
                        }
                        if (mm < 10) {
                            phut = "0" + phut;
                        }

                        Toast.makeText(getActivity(), "Đặt " + gio + " : " + phut, Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(), sdf.format(calendar.getTime()).toString() + "...", Toast.LENGTH_SHORT).show();

                    }
                }, hour, minute, true);

                timePickerDialog.show();
            }

        }, year, month, day);

        datePickerDialog.show();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_task, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.ic_task_sort:

                return false;
            case R.id.ic_task_list:
                Intent intent = new Intent(getActivity(), TasksListActivity.class);
                getActivity().startActivity(intent);
                return true;
            case R.id.ic_task_plan:

                return true;
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        tasksList = (TasksList) parent.getItemAtPosition(position);
        Toast.makeText(getActivity(), tasksList.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public ArrayAdapter<TasksList> loadSpinner() {
        tasksListList = tasksListDAO.getAllTasksList();
        ArrayAdapter<TasksList> adapter = new ArrayAdapter(getActivity(),
                android.R.layout.simple_spinner_item, tasksListList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    //TODAY
    public void getTasksToday() {
        int dayToday = calendar.get(Calendar.DATE);
        int monthToday = calendar.get(Calendar.MONTH) + 1;
        int yearToday = calendar.get(Calendar.YEAR);

        tasksToday = tasksDAO.getTasksToDayTomorrow(dayToday, monthToday, yearToday);
        Log.d("today", tasksToday.size() + "");
        tvQuantityToday.setText(String.valueOf(tasksToday.size()));
        Toast.makeText(getActivity(), dayToday + "/ " + monthToday + "/" + yearToday, Toast.LENGTH_LONG).show();

        //recycleview
        taskAdapter = new TaskAdapter(getActivity(), tasksToday);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        recycleviewToday.setLayoutManager(manager);
        recycleviewToday.setAdapter(taskAdapter);
        taskAdapter.notifyDataSetChanged();
    }

    //TOMORROW
    public void getTasksTomorrow() {
        //TOMORROW
        calendar.add(Calendar.DATE, 1);
        int dayTomorrow = calendar.get(Calendar.DATE);
        int monthTomorrow = calendar.get(Calendar.MONTH) + 1;
        int yearTomorrow = calendar.get(Calendar.YEAR);
        Toast.makeText(getActivity(), dayTomorrow + "/ " + monthTomorrow + "/" + yearTomorrow, Toast.LENGTH_LONG).show();

        tasksTomorrow = tasksDAO.getTasksToDayTomorrow(dayTomorrow, monthTomorrow, yearTomorrow);
        tvQuantityTomorrow.setText(String.valueOf(tasksTomorrow.size()));
        Log.d("today", tasksTomorrow.size() + "");

        //recycleview
        taskAdapter = new TaskAdapter(getActivity(), tasksTomorrow);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        recycleviewTomorrow.setLayoutManager(manager);
        recycleviewTomorrow.setAdapter(taskAdapter);
        taskAdapter.notifyDataSetChanged();
    }


    public void anhxa(View view) {
        toobarTasks = view.findViewById(R.id.toobarTasks);
        flButtonTask = view.findViewById(R.id.flButtonTask);
        recycleviewToday = view.findViewById(R.id.recycleviewToday);
        recycleviewTomorrow = view.findViewById(R.id.recycleviewTomorrow);
        tvQuantityToday = view.findViewById(R.id.tvQuantityToday);
        tvQuantityTomorrow = view.findViewById(R.id.tvQuantityTomorrow);
        tvQuantityUpcoming = view.findViewById(R.id.tvQuantityUpcoming);

    }


}
