package com.gutspot.apps.android.mytodo.model;

import java.util.Date;

public class Notice extends AbstractEntity {

    /**
     * Represents the identifier of the todo.
     */
    private long toDoId;

    /**
     * Represents the notice time of the todo.
     */
    private Date time;

    public Notice() {

    }

    public Notice(long id, long version) {
        super(id, version);
    }

    public long getToDoId() {
        return toDoId;
    }

    public void setToDoId(long toDoId) {
        this.toDoId = toDoId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

}
