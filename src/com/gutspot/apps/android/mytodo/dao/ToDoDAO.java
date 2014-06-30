package com.gutspot.apps.android.mytodo.dao;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.gutspot.apps.android.mytodo.model.ToDo;

public class ToDoDAO extends AbstractDAO<ToDo> {

    private static final String TABLE_NAME = "todo";

    private static final String COLUMN_CREATED = "created";
    private static final String COLUMN_FINISHED = "finished";

    public ToDoDAO(Context context) {
        super(context, TABLE_NAME);
    }

    @Override
    protected ContentValues createValues(ToDo entity, boolean create) {
        ContentValues values = new ContentValues();

        if (create) {
            values.put(COLUMN_CREATED, entity.getCreated().getTime());
        }
        Long finished = entity.getFinished() == null ? null : entity.getFinished().getTime();
        values.put(COLUMN_FINISHED, finished);

        return values;
    }

    @Override
    protected ToDo parseValuse(Cursor cursor) {
        ToDo toDo = new ToDo();

        toDo.setCreated(new Date(cursor.getLong(cursor.getColumnIndex(COLUMN_CREATED))));
        toDo.setFinished(new Date(cursor.getLong(cursor.getColumnIndex(COLUMN_FINISHED))));

        return toDo;
    }
}
