package com.hlub.dev.project_01_reminder.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hlub.dev.project_01_reminder.constant.Constant;
import com.hlub.dev.project_01_reminder.database.DatabaseManager;
import com.hlub.dev.project_01_reminder.model.TasksList;

import java.util.ArrayList;
import java.util.List;

public class TasksListDAO implements Constant{
    private DatabaseManager databaseManager;
    SQLiteDatabase sqLiteDatabase;

    public TasksListDAO(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        sqLiteDatabase=databaseManager.getWritableDatabase();
    }

    //INSERT
    public long insertTasksList(TasksList tasksList){
        ContentValues values=new ContentValues();
        values.put(COLUMN_TASKSLIST_ID,tasksList.getId());
        values.put(COLUMN_TASKSLIST_NAME,tasksList.getName());

        long id = sqLiteDatabase.insert(TABLE_TASKSLIST, null, values);
        Log.e("Insert TasksList",  id+"");
        return id;
    }

    //GET ALL TASKSLIST
    public List<TasksList> getAllTasksList(){
        List<TasksList>tasksListList=new ArrayList<>();
        String select="SELECT * FROM "+TABLE_TASKSLIST;
        Cursor cursor=sqLiteDatabase.rawQuery(select,null);
        if(cursor.moveToFirst()){
            do {
                TasksList tasksList=new TasksList();
                tasksList.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_TASKSLIST_ID)));
                tasksList.setName(cursor.getString(cursor.getColumnIndex(COLUMN_TASKSLIST_NAME)));

                tasksListList.add(tasksList);
            }while (cursor.moveToNext());
        }
        return tasksListList;
    }

    public TasksList getTasksListByID(long tasksListId){
        TasksList tasksList=null;

        Cursor cursor=sqLiteDatabase.query(TABLE_TASKSLIST,
                new String[]{COLUMN_TASKSLIST_ID,COLUMN_TASKSLIST_NAME},
                COLUMN_TASKSLIST_ID+"=?",
                new String[]{String.valueOf(tasksListId)},
                null,null,null);
        if(cursor!=null && cursor.moveToFirst()){
            tasksList=new TasksList();
            tasksList.setId(cursor.getLong(0));
            tasksList.setName(cursor.getString(1));
        }
        return tasksList;
    }

    //UPDATE TASKSLIST
    public long updateTasksList(TasksList tasksList){
        ContentValues values=new ContentValues();
        values.put(COLUMN_TASKSLIST_NAME,tasksList.getName());

        return sqLiteDatabase.update(TABLE_TASKSLIST,values,COLUMN_TASKSLIST_ID+"=?",new String[]{String.valueOf(tasksList.getId())});

    }

    //DELETE TASKSLIST
    public long deleteTasksList(TasksList tasksList){
        return sqLiteDatabase.delete(TABLE_TASKSLIST,COLUMN_TASKSLIST_ID+"=?",new String[]{String.valueOf(tasksList.getId())});
    }
}
