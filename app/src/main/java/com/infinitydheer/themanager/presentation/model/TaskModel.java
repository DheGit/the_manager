package com.infinitydheer.themanager.presentation.model;

import java.util.Calendar;

public class TaskModel {
    private String name;
    private String note;
    private Calendar date;
    private int quad;
    private long id;
    private int done;
    private boolean selected;

    public TaskModel(){
        done=-1;
        selected=false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public int getQuad() {
        return quad;
    }

    public void setQuad(int quad) {
        this.quad = quad;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int isDone() {
        return done;
    }

    public void setDone(int done) {
        this.done = done;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("TaskModel: [")
                .append("id: ").append(getId())
                .append(", name: ").append(getName())
                .append(", note: ").append(getNote())
                .append(", dueDate: ").append(getDate().toString())
                .append(", quadrant: "). append(getQuad())
                .append(" done: ").append(isDone())
                .toString();
    }
}
