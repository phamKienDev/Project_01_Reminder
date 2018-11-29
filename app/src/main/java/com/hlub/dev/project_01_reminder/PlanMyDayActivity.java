package com.hlub.dev.project_01_reminder;

import android.animation.ArgbEvaluator;
import android.animation.TimeAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.GradientDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import com.hlub.dev.project_01_reminder.adapter.PlanMyDayAdapter;
import com.hlub.dev.project_01_reminder.dao.TasksDAO;
import com.hlub.dev.project_01_reminder.database.DatabaseManager;
import com.hlub.dev.project_01_reminder.model.Tasks;

import java.util.List;

public class PlanMyDayActivity extends AppCompatActivity {

    ConstraintLayout constraintLayout;
    AnimationDrawable animationDrawable;
    private GridView gridViewPlan;
    TasksDAO tasksDAO;
    DatabaseManager databaseManager;
    List<Tasks> tasks ;
    PlanMyDayAdapter planMyDayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_my_day);
        constraintLayout = findViewById(R.id.view);
        gridViewPlan = findViewById(R.id.gridViewPlan);
        databaseManager=new DatabaseManager(this);
        tasksDAO=new TasksDAO(databaseManager);

        animationDrawable = (AnimationDrawable) constraintLayout.getBackground();

        animationDrawable.setEnterFadeDuration(2500);

        //animationDrawable.setExitFadeDuration(4500);

        animationDrawable.start();

        tasks=tasksDAO.getAllTasks();
        planMyDayAdapter=new PlanMyDayAdapter(tasks,this);
        gridViewPlan.setAdapter(planMyDayAdapter);

    }


}
