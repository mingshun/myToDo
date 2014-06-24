package com.gutspot.apps.android.mytodo.model;

import java.util.Date;

public class ToDo extends AbstractEntity {
    /**
     * Represents the state of todo, <code>ture</code> for finished and
     * <code>false</code> for unfinished.
     */
    private boolean finish;

    /**
     * Represents the finish timestamp of the todo.
     */
    private Date finished;

    /**
     * Represents the note of the todo.
     */
    private String note;

    public ToDo() {

    }

    public ToDo(long id) {
        super(id);
    }

    public boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    public Date getFinished() {
        return finished;
    }

    public void setFinished(Date finished) {
        this.finished = finished;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

}
