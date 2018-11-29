package com.hlub.dev.project_01_reminder.fragment;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

import com.hlub.dev.project_01_reminder.PlanMyDayActivity;
import com.hlub.dev.project_01_reminder.TasksListActivity;
import com.hlub.dev.project_01_reminder.R;
import com.hlub.dev.project_01_reminder.UpdateTasksActivity;
import com.hlub.dev.project_01_reminder.adapter.ExpandableListViewTasks;
import com.hlub.dev.project_01_reminder.dao.TasksDAO;
import com.hlub.dev.project_01_reminder.dao.TasksListDAO;
import com.hlub.dev.project_01_reminder.database.DatabaseManager;
import com.hlub.dev.project_01_reminder.model.Tasks;
import com.hlub.dev.project_01_reminder.model.TasksList;
import com.hlub.dev.project_01_reminder.broadcast_receiver.AlarmReceiver;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;

public class TasksFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private Toolbar toobarTasks;
    private ExpandableListView expandableTasks;
    private FloatingActionButton flButtonTask;
    private EditText edtIWanTo;
    private ImageView imgCalendar;
    private Spinner spinnerList;
    private ImageView imgEditTasks;
    String ngay, thang, nam, gio, phut;
    private TextView tvNameTasks;
    private TextView tvDayTasks;
    private TextView tvTimeTasks;
    private TextView tvListTasks;
    private TextView tvNoteTasks;
    private TextView tvAdressTasks;
    public static final String TASKS_ID = "TasksId";

    ExpandableListViewTasks expandableListViewTasks;
    List<String> listTasksHeader;
    HashMap<String, List<Tasks>> listDataChild;


    List<TasksList> tasksListList;
    List<Tasks> tasksToday;
    List<Tasks> tasksTomorrow;
    List<Tasks> tasksUpcoming;
    List<Tasks> tasksAll;
    List<Tasks> tasks;

    PendingIntent pendingIntent;
    AlarmManager alarmManager;
    Calendar calendar;

    TasksList tasksList;

    private DatabaseManager databaseManager;
    private TasksDAO tasksDAO;
    private TasksListDAO tasksListDAO;
    long timeNow;

    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm aa");
    private String ACTION_NOTIFICATION = "ACTION_NOTIFICATION";
    private String CHANNEL_ID = "CHANNEL_ID";
    public static final String KEY = "KEY";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toobarTasks);
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
        tasksAll = new ArrayList<>();
        listTasksHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        //DB
        databaseManager = new DatabaseManager(getActivity());
        tasksDAO = new TasksDAO(databaseManager);
        tasksListDAO = new TasksListDAO(databaseManager);

        calendar = Calendar.getInstance();
        timeNow = calendar.getTimeInMillis();//time hiện tại

        //ALARMMANAGER
        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        //toolbar
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toobarTasks);

        setHasOptionsMenu(true);
        toobarTasks.inflateMenu(R.menu.menu_task);

        //expandable listview
        addControl();



        //bat su  kien
        click_group();
        click_childer();
        close_group();
        open_group();

        //timer
        //timer();

        //floatingbutton
        flButtonTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogNewTask();
            }
        });
        return view;
    }

    private void addControl() {

//        listDataChild.clear();
//        listTasksHeader.clear();
//        tasksToday.clear();
//        tasksTomorrow.clear();
//        tasksUpcoming.clear();

        listTasksHeader.add(getString(R.string.today));
        listTasksHeader.add(getString(R.string.tomorrow));
        listTasksHeader.add(getString(R.string.upcoming));

        //TO DAY
        tasksToday = tasksDAO.getTasksToDayTomorrow(calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.YEAR));

        //TOMORROW ( TODAY+1)
        calendar.add(Calendar.DATE, 1);
        tasksTomorrow = tasksDAO.getTasksToDayTomorrow(calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.YEAR));

        //UPCOMING //cộng thêm 2 ngày ->lấy những tasks sắp tới(trừ today & tomorrow)
        Calendar upcoming = Calendar.getInstance();
        upcoming.add(Calendar.DAY_OF_MONTH, 2);

        upcoming.set(Calendar.HOUR_OF_DAY, 0);
        upcoming.set(Calendar.MINUTE, 0);
        upcoming.set(Calendar.SECOND, 0);
        tasksUpcoming = tasksDAO.getTasksUpcoming(upcoming.getTimeInMillis());

        listDataChild.put(listTasksHeader.get(0), tasksToday);
        listDataChild.put(listTasksHeader.get(1), tasksTomorrow);
        listDataChild.put(listTasksHeader.get(2), tasksUpcoming);

        expandableListViewTasks = new ExpandableListViewTasks(getActivity(), listTasksHeader, listDataChild);
        expandableTasks.setAdapter(expandableListViewTasks);
        expandableListViewTasks.notifyDataSetChanged();


    }

    private void open_group() {
        expandableTasks.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int i) {
                // Toast.makeText(getActivity(), "open " + listDataHeader.get(i), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void close_group() {
        expandableTasks.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                // Toast.makeText(MainActivity.this, "close " + listDataHeader.get(groupPosition), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void click_childer() {
        expandableTasks.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childerPosition, long l) {
                long id = listDataChild.get(listTasksHeader.get(groupPosition)).get(childerPosition).getId();
                showDialogInfoTask(id);
                return false;
            }
        });
    }

    private void click_group() {
        expandableTasks.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long l) {
                //Toast.makeText(MainActivity.this, listDataHeader.get(groupPosition), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    //SHOW DIALOG ->CREATE NEW TASKS
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
                long timer = calendar.getTimeInMillis();//thời điểm đặt báo thức
                // k đc phép đặt thời điểm nhiệm vụ đã kết thúc
                if (timer <= timeNow) {
                    Toast.makeText(getActivity(), getString(R.string.The_time_you_selected_has_ended), Toast.LENGTH_SHORT).show();
                } else {
                    Tasks tasks = new Tasks(nameTask, timer, tasksList.getId(), "", "", timeNow, 0);
                    tasksDAO.insertTasks(tasks);
                    addControl();
                    expandableTasks.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                        @Override
                        public void onGroupExpand(int i) {
                            // Toast.makeText(getActivity(), "open " + listDataHeader.get(i), Toast.LENGTH_SHORT).show();
                        }
                    });

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

    //SHOW DIALOG INFO TASKS
    private void showDialogInfoTask(final long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //view
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_info_tasks, null);
        imgEditTasks = view.findViewById(R.id.imgEditTasks);
        tvNameTasks = view.findViewById(R.id.tvNameTasks);
        tvDayTasks = view.findViewById(R.id.tvDayTasks);
        tvTimeTasks = view.findViewById(R.id.tvTimeTasks);
        tvListTasks = view.findViewById(R.id.tvListTasks);
        tvNoteTasks = view.findViewById(R.id.tvNoteTasks);
        tvAdressTasks = view.findViewById(R.id.tvAdressTasks);
        builder.setView(view);


        SimpleDateFormat day = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat time = new SimpleDateFormat("HH:mm aa");
        //Sét text
        final Tasks tasks = tasksDAO.getTasksByID(id);
        tvNameTasks.setText(tasks.getNameTask());
        tvDayTasks.setText(day.format(tasks.getTime()));
        tvTimeTasks.setText(time.format(tasks.getTime()));

        //list
        TasksList tl = tasksListDAO.getTasksListByID(tasks.getTaskListId());
        tvListTasks.setText(tl.getName());
        //address
        if (tasks.getAddress().length() > 0) {
            setTextForEdt(tvAdressTasks, tasks.getAddress());
        } else {
            setTextForEdtNull(tvAdressTasks, getString(R.string.no_address));
        }

        //note
        if (tasks.getNote().length() > 0) {
            setTextForEdt(tvNoteTasks, tasks.getNote());
        } else {
            setTextForEdtNull(tvNoteTasks, getString(R.string.no_note));
        }
        Toast.makeText(getActivity(), "" + tasks.getId(), Toast.LENGTH_SHORT).show();

        //Chuyển màn hình EDIT TASKS
        imgEditTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UpdateTasksActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("TasksId", String.valueOf(tasks.getId()));
                intent.putExtra("bundle", bundle);
                startActivity(intent);
            }
        });

        builder.show();
    }

    public void setTextForEdt(TextView tv, String text) {
        tv.setText(text);
        tv.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack));
    }

    public void setTextForEdtNull(TextView tv, String text) {
        tv.setText(text);
        tv.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGray));
    }


    public void timer() {
        tasksAll = tasksDAO.getAllTasks();
        for (Tasks task : tasksAll) {
            if (task.getTime() > timeNow) {
                Intent intent = new Intent(getActivity(), AlarmReceiver.class);
                //HẸN GIỜ
                intent.putExtra(KEY, "On");
                //vẫn tồn tại khi thoát ứng dụng
                pendingIntent = PendingIntent.getBroadcast(
                        getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                notification(pendingIntent);
                alarmManager.set(AlarmManager.RTC_WAKEUP, task.getTime(), pendingIntent);
            }

        }

    }

    //NOTIFICATION
    public void notification(PendingIntent pendingIntent) {
        Intent snoozeIntent = new Intent(getActivity(), AlarmReceiver.class);
        snoozeIntent.setAction(ACTION_NOTIFICATION);
        snoozeIntent.putExtra(KEY, "Off");

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getActivity(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("My notification")
                .setContentText("Hello World!")
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(""))
                .addAction(R.drawable.ic_launcher_background, getString(R.string.start),
                        pendingIntent);
        createNotificationChannel();
        //hiển thị thông báo
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getActivity());

        // hide the notification after its selected
        mBuilder.build().flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(0, mBuilder.build());


    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    //SHOW DATE & TIME DIALOGPICKER
    public void showDatePickerDialog() {
        final int day = calendar.get(Calendar.DAY_OF_MONTH) - 1;
        final int month = calendar.get(Calendar.MONTH);
        final int year = calendar.get(Calendar.YEAR);

        final DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(final DatePicker datePicker, int i, int i1, int i2) {

                int hour = calendar.get(Calendar.HOUR_OF_DAY) + 1;
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

                    }
                }, hour, minute, true);

                timePickerDialog.show();
            }

        }, year, month, day);

        datePickerDialog.show();

    }


    //LOAD TASKSLIST CHO SPINNER
    public ArrayAdapter<TasksList> loadSpinner() {
        tasksListList = tasksListDAO.getAllTasksList();
        ArrayAdapter<TasksList> adapter = new ArrayAdapter(getActivity(),
                android.R.layout.simple_spinner_item, tasksListList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    //CLICK SPINNER
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        tasksList = (TasksList) parent.getItemAtPosition(position);
        Toast.makeText(getActivity(), tasksList.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    //MENU
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_task, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.ic_task_sort:
                sortTasksByList();
                return false;
            case R.id.ic_task_list:
                Intent intent = new Intent(getActivity(), TasksListActivity.class);
                getActivity().startActivity(intent);
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //SẮP XẾP TASKS THEO LIST
    private void sortTasksByList() {
        listTasksHeader.clear();
        listDataChild.clear();
        List<Tasks> tasksByList = new ArrayList<>();
        tasksListList = tasksListDAO.getAllTasksList();
        for (TasksList tl : tasksListList) {

            tasks = tasksDAO.getAllTasksByListId(tl.getId());
            for (Tasks t : tasks) {
                tasksByList.add(t);
                Log.d("t.size", tasksByList.size() + "");

            }
            listTasksHeader.add(tl.getName());
            listDataChild.put(tl.getName(), tasksByList);
            Log.d("listDataChild.size", listDataChild.size() + "");
            tasksByList.clear();
            Log.d("t.size2", tasksByList.size() + "");

        }


        expandableListViewTasks = new ExpandableListViewTasks(getActivity(), listTasksHeader, listDataChild);
        expandableTasks.setAdapter(expandableListViewTasks);
    }

    //ANH XA
    public void anhxa(View view) {
        toobarTasks = view.findViewById(R.id.toobarTasks);
        flButtonTask = view.findViewById(R.id.flButtonTask);
        expandableTasks = view.findViewById(R.id.expandableListviewTasks);

    }


}
