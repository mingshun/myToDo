package com.gutspot.apps.android.mytodo.model;

import java.util.Date;

public class Memo extends AbstractEntity {
    /**
     * Represents the identifier of the todo.
     */
    private long toDoId;

    /**
     * Represents the contend of the memo.
     */
    private String content;

    /**
     * Represents the text color of the memo.
     */
    private String textColor;

    /**
     * Represents the background color of the memo.
     */
    private String backgroundColor;

    /**
     * Represents the create timestamp of the memo.
     */
    private Date created;

    public Memo() {

    }

    public Memo(long id) {
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

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

}
