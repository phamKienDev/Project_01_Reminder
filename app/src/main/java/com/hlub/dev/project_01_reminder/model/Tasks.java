package com.hlub.dev.project_01_reminder.model;

public class Tasks {
    private int id;
    private String nameTask;
    private Long time;
    private long tasksListId;
    private String note;
    private String address;
    private Long timeCreate;
    private int finish;


    public Tasks(int id, String nameTask, Long time, long tasksListId, String note, String address, Long timeCreate, int finish) {
        this.id = id;
        this.nameTask = nameTask;
        this.time = time;
        this.tasksListId = tasksListId;
        this.note = note;
        this.address = address;
        this.timeCreate = timeCreate;
        this.finish = finish;
    }
    public Tasks( String nameTask, Long time, long tasksListId, String note, String address, Long timeCreate, int finish) {
        this.nameTask = nameTask;
        this.time = time;
        this.tasksListId = tasksListId;
        this.note = note;
        this.address = address;
        this.timeCreate = timeCreate;
        this.finish = finish;
    }


    public Tasks() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameTask() {
        return nameTask;
    }

    public void setNameTask(String nameTask) {
        this.nameTask = nameTask;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public long getTaskListId() {
        return tasksListId;
    }

    public void setTaskListId(long taskListId) {
        this.tasksListId = taskListId;
    }


    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getTimeCreate() {
        return timeCreate;
    }

    public void setTimeCreate(Long timeCreate) {
        this.timeCreate = timeCreate;
    }

    public int getFinish() {
        return finish;
    }

    public void setFinish(int finish) {
        this.finish = finish;
    }

}
