package com.hlub.dev.project_01_reminder.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hlub.dev.project_01_reminder.constant.Constant;

public class DatabaseManager extends SQLiteOpenHelper implements Constant {

    //co so du lieu
    public DatabaseManager(Context context) {
        super(context, "ReminderManager", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TASKS);
        db.execSQL(CREATE_TABLE_TASKSLIST);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKSLIST);
        onCreate(db);

    }
}
