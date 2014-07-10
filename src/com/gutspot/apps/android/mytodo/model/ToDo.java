package com.gutspot.apps.android.mytodo.model;

import java.util.Date;

public class ToDo extends AbstractEntity {
    /**
     * Represents the create timestamp of the todo.
     */
    private Date created;

    /**
     * Represents the finish timestamp of the todo. <code>-1</code> means
     * unfinished.
     */
    private Date finished;

    public ToDo() {

    }

    public ToDo(long id, long version) {
        super(id, version);
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getFinished() {
        return finished;
    }

    public void setFinished(Date finished) {
        this.finished = finished;
    }

}
