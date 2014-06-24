package com.gutspot.apps.android.mytodo.model;

import java.util.Date;

public class ToDoContent extends AbstractEntity {

    /**
     * Represents the identifier of the todo.
     */
    private long toDoId;

    /**
     * Represents the content of the todo.
     */
    private String content;

    /**
     * Represents the time of this content created.
     */
    private Date created;

    public ToDoContent() {

    }

    public ToDoContent(long id) {
        super(id);
    }

    public long getToDoId() {
        return toDoId;
    }

    public void setToDoId(long toDoId) {
        this.toDoId = toDoId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

}
