package com.hlub.dev.project_01_reminder.constant;

public interface Constant {

    //TASKS

    String TABLE_TASKS = "Tasks";

    String COLUMN_TASKS_ID = "tasksId";
    String COLUMN_TASKS_NAME = "tasksName";
    String COLUMN_TASKS_TIME = "time";
    String COLUMN_LIST_ID = "tasksListId";
    String COLUMN_TASKS_NOTE = "note";
    String COLUMN_TASKS_ADDRESS = "address";
    String COLUMN_TASKS_TIME_CREATE = "timeCreate";
    String COLUMN_TASKS_FINISH = "finish";

    String CREATE_TABLE_TASKS = "CREATE TABLE " + TABLE_TASKS + " ( " +
            COLUMN_TASKS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            COLUMN_TASKS_NAME + " VARCHAR(100) NOT NULL," +
            COLUMN_TASKS_TIME + " LONG NOT NULL," +
            COLUMN_LIST_ID + " LONG NOT NULL," +
            COLUMN_TASKS_NOTE + " VARCHAR(100)," +
            COLUMN_TASKS_ADDRESS + " VARCHAR(100) ," +
            COLUMN_TASKS_TIME_CREATE + " LONG NOT NULL," +
            COLUMN_TASKS_FINISH + " INTEGER NOT NULL" +
            " )";

    //TASK LIST

    String TABLE_TASKSLIST="TasksList";

    String COLUMN_TASKSLIST_ID="tasksListId";
    String COLUMN_TASKSLIST_NAME="tasksListName";

    String CREATE_TABLE_TASKSLIST = "CREATE TABLE " + TABLE_TASKSLIST + " ( " +
            COLUMN_TASKSLIST_ID + " LONG PRIMARY KEY  NOT NULL," +
            COLUMN_TASKSLIST_NAME + " VARCHAR(100) NOT NULL" +
            " )";
}
