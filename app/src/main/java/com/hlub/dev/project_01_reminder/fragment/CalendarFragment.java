package com.hlub.dev.project_01_reminder.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.EventLog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.hlub.dev.project_01_reminder.R;
import com.hlub.dev.project_01_reminder.adapter.TasksByCalendarAdapter;
import com.hlub.dev.project_01_reminder.dao.TasksDAO;
import com.hlub.dev.project_01_reminder.database.DatabaseManager;
import com.hlub.dev.project_01_reminder.model.Tasks;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class CalendarFragment extends Fragment {
    private CalendarView calendarTasks;
    private RecyclerView tasksListview;
    private TasksByCalendarAdapter tasksByCalendarAdapter;
    List<Tasks> tasks = new ArrayList<>();
    private TasksDAO tasksDAO;
    private DatabaseManager databaseManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainTabView = inflater.inflate(R.layout.fragment_calendar, container, false);
        calendarTasks = mainTabView.findViewById(R.id.calendarTasks);
        tasksListview = mainTabView.findViewById(R.id.tasks_listview);


        databaseManager = new DatabaseManager(getActivity());
        tasksDAO = new TasksDAO(databaseManager);
        tasks = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        int m = calendar.get(Calendar.MONTH);
        tasks = tasksDAO.getTasksToDayTomorrow(calendar.get(Calendar.DAY_OF_MONTH),
                m,
                calendar.get(Calendar.YEAR));


        getTasksByDay(calendar.get(Calendar.DAY_OF_MONTH), m, calendar.get(Calendar.YEAR));
        calendarTasks.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                getTasksByDay(dayOfMonth, month, year);
            }
        });
        return mainTabView;
    }

    public void getTasksByDay(int day, int month, int year) {

        int m = month + 1;
        Toast.makeText(getActivity(), day + "/" + m + "/" + year, Toast.LENGTH_SHORT).show();
        tasks = tasksDAO.getTasksToDayTomorrow(day, m, year);
        tasksByCalendarAdapter = new TasksByCalendarAdapter(tasks, getActivity());
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        tasksListview.setLayoutManager(manager);
        tasksListview.setAdapter(tasksByCalendarAdapter);


    }


}
