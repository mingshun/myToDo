package com.gutspot.apps.android.mytodo.dao;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.gutspot.apps.android.mytodo.model.ToDoContent;

public class ToDoContentDAO extends AbstractDAO<ToDoContent> {

    private static final String TABLE_NAME = "todo_content";

    private static final String COLUMN_TODO_ID = "todo_id";
    private static final String COLUMN_CONTENT = "content";
    private static final String COLUMN_CREATED = "created";

    protected ToDoContentDAO(Context context) {
        super(context, TABLE_NAME);
    }

    @Override
    protected ContentValues createValues(ToDoContent entity, boolean create) {
        ContentValues values = new ContentValues();

        values.put(COLUMN_TODO_ID, entity.getToDoId());
        values.put(COLUMN_CONTENT, entity.getContent());
        values.put(COLUMN_CREATED, entity.getCreated().getTime());

        return values;
    }

    @Override
    protected ToDoContent parseValuse(Cursor cursor) {
        ToDoContent toDoContent = new ToDoContent();

        toDoContent.setToDoId(cursor.getLong(cursor.getColumnIndex(COLUMN_TODO_ID)));
        toDoContent.setContent(cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT)));
        toDoContent.setCreated(new Date(cursor.getLong(cursor.getColumnIndex(COLUMN_CREATED))));

        return toDoContent;
    }
}
