package com.infinitydheer.themanager.domain.data;

public class TaskDomain {
    private String name;
    private String note;
    private String dueDate;
    private long id;
    private int quad;
    private int done;

    public TaskDomain(){}

    public void setName(String name){
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
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

    public int getQuad() {
        return quad;
    }

    public void setQuad(int quad) {
        this.quad = quad;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("TaskDomain: [")
                .append("id: ").append(getId())
                .append(", name: ").append(getName())
                .append(", note: ").append(getNote())
                .append(", dueDate: ").append(getDueDate())
                .append(", quadrant: "). append(getQuad())
                .append(" done: ").append(isDone())
                .toString();
    }
}
