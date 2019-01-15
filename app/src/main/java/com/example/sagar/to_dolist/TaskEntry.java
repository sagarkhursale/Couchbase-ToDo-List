package com.example.sagar.to_dolist;

import java.util.Date;


public class TaskEntry {

    private int id, priority;
    private String description;
    private Date updatedAt;


    public TaskEntry(int id, int priority, String description, Date updatedAt) {
        this.id = id;
        this.priority = priority;
        this.description = description;
        this.updatedAt = updatedAt;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

// END
}
