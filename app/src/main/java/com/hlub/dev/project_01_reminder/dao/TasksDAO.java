package com.hlub.dev.project_01_reminder.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hlub.dev.project_01_reminder.constant.Constant;
import com.hlub.dev.project_01_reminder.database.DatabaseManager;
import com.hlub.dev.project_01_reminder.model.Tasks;
import com.hlub.dev.project_01_reminder.model.TasksList;

import java.util.ArrayList;
import java.util.List;

public class TasksDAO implements Constant {

    private DatabaseManager databaseManager;
    SQLiteDatabase sqLiteDatabase;

    public TasksDAO(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        sqLiteDatabase = databaseManager.getWritableDatabase();
    }

    //INSERT
    public long insertTasks(Tasks tasks) {
        ContentValues values = new ContentValues();
//        values.put(COLUMN_TASKS_ID, tasks.getId());->AUTOINCREMENT
        values.put(COLUMN_TASKS_NAME, tasks.getNameTask());
        values.put(COLUMN_TASKS_TIME, tasks.getTime());
        values.put(COLUMN_LIST_ID, tasks.getTaskListId());
        values.put(COLUMN_TASKS_NOTE, tasks.getNote());
        values.put(COLUMN_TASKS_ADDRESS, tasks.getAddress());
        values.put(COLUMN_TASKS_TIME_CREATE, tasks.getTimeCreate());
        values.put(COLUMN_TASKS_FINISH, tasks.getFinish());
        long id = sqLiteDatabase.insert(TABLE_TASKS, null, values);
        Log.e("Insert Tasks", id + "");
        return id;
    }

    //GET ALL TASKS
    public List<Tasks> getAllTasks() {
        List<Tasks> tasksList = new ArrayList<>();
        String select = "SELECT * FROM " + TABLE_TASKS;
        Cursor cursor = sqLiteDatabase.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {
                Tasks tasks = new Tasks();
                tasks.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_TASKS_ID)));
                tasks.setNameTask(cursor.getString(cursor.getColumnIndex(COLUMN_TASKS_NAME)));
                tasks.setTime(cursor.getLong(cursor.getColumnIndex(COLUMN_TASKS_TIME)));
                tasks.setTaskListId(cursor.getLong(cursor.getColumnIndex(COLUMN_LIST_ID)));
                tasks.setNote(cursor.getString(cursor.getColumnIndex(COLUMN_TASKS_NOTE)));
                tasks.setAddress(cursor.getString(cursor.getColumnIndex(COLUMN_TASKS_ADDRESS)));
                tasks.setTimeCreate(cursor.getLong(cursor.getColumnIndex(COLUMN_TASKS_TIME_CREATE)));
                tasks.setFinish(cursor.getInt(cursor.getColumnIndex(COLUMN_TASKS_FINISH)));

                tasksList.add(tasks);
            } while (cursor.moveToNext());
        }
        return tasksList;
    }

    public Tasks getTasksByID(long tasksId) {
        Tasks tasks = null;

        Cursor cursor = sqLiteDatabase.query(TABLE_TASKS,
                new String[]{COLUMN_TASKS_ID, COLUMN_TASKS_NAME, COLUMN_TASKS_TIME,
                        COLUMN_LIST_ID, COLUMN_TASKS_NOTE, COLUMN_TASKS_ADDRESS, COLUMN_TASKS_TIME_CREATE, COLUMN_TASKS_FINISH},
                COLUMN_TASKS_ID + "=?",
                new String[]{String.valueOf(tasksId)},
                null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            tasks = new Tasks();
            tasks.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_TASKS_ID)));
            tasks.setNameTask(cursor.getString(cursor.getColumnIndex(COLUMN_TASKS_NAME)));
            tasks.setTime(cursor.getLong(cursor.getColumnIndex(COLUMN_TASKS_TIME)));
            tasks.setTaskListId(cursor.getLong(cursor.getColumnIndex(COLUMN_LIST_ID)));
            tasks.setNote(cursor.getString(cursor.getColumnIndex(COLUMN_TASKS_NOTE)));
            tasks.setAddress(cursor.getString(cursor.getColumnIndex(COLUMN_TASKS_ADDRESS)));
            tasks.setTimeCreate(cursor.getLong(cursor.getColumnIndex(COLUMN_TASKS_TIME_CREATE)));
            tasks.setFinish(cursor.getInt(cursor.getColumnIndex(COLUMN_TASKS_FINISH)));
        }
        return tasks;
    }

    //UPDATE TASKSLIST
    public long updateTasks(Tasks tasks) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASKS_NAME, tasks.getNameTask());
        values.put(COLUMN_TASKS_TIME, tasks.getTime());
        values.put(COLUMN_LIST_ID, tasks.getTaskListId());
        values.put(COLUMN_TASKS_NOTE, tasks.getNote());
        values.put(COLUMN_TASKS_ADDRESS, tasks.getAddress());
        values.put(COLUMN_TASKS_TIME_CREATE, tasks.getTimeCreate());
        values.put(COLUMN_TASKS_FINISH, tasks.getFinish());

        return sqLiteDatabase.update(TABLE_TASKS, values, COLUMN_TASKS_ID + "=?", new String[]{String.valueOf(tasks.getId())});

    }

    //DELETE TASKSLIST
    public long deleteTasks(Tasks tasks) {
        return sqLiteDatabase.delete(TABLE_TASKS, COLUMN_TASKS_ID + "=?", new String[]{String.valueOf(tasks.getId())});
    }

    //GET TASKS BY TODAY, TOMORROW
    public List<Tasks> getTasksToDayTomorrow(int day,int month,int year) {
        List<Tasks> tasksList = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_TASKS + " WHERE strftime('%d',Tasks.time/1000,'unixepoch')='" + day + "'" +
                " AND strftime('%m',Tasks.time/1000,'unixepoch')='"+month+"' AND strftime('%Y',Tasks.time/1000,'unixepoch')='"+year+"'";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        Log.d("Today", sql);
        if (cursor.moveToFirst()) {
            do {
                Tasks tasks = new Tasks();
                tasks.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_TASKS_ID)));
                tasks.setNameTask(cursor.getString(cursor.getColumnIndex(COLUMN_TASKS_NAME)));
                tasks.setTime(cursor.getLong(cursor.getColumnIndex(COLUMN_TASKS_TIME)));
                tasks.setTaskListId(cursor.getLong(cursor.getColumnIndex(COLUMN_LIST_ID)));
                tasks.setNote(cursor.getString(cursor.getColumnIndex(COLUMN_TASKS_NOTE)));
                tasks.setAddress(cursor.getString(cursor.getColumnIndex(COLUMN_TASKS_ADDRESS)));
                tasks.setTimeCreate(cursor.getLong(cursor.getColumnIndex(COLUMN_TASKS_TIME_CREATE)));
                tasks.setFinish(cursor.getInt(cursor.getColumnIndex(COLUMN_TASKS_FINISH)));

                tasksList.add(tasks);
            } while (cursor.moveToNext());
        }
        return tasksList;
    }
}