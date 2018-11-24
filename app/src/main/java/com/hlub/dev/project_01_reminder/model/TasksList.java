package com.hlub.dev.project_01_reminder.model;

public class TasksList {
    private long id;
    private String name;

    public TasksList(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public TasksList() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return name;
    }
}
