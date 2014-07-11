package com.gutspot.apps.android.mytodo.dao;

import java.util.Date;
import java.util.List;

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

    public List<ToDo> findOrderByCreated() {
        String orderBy = COLUMN_CREATED + " asc";
        return this.find(null, null, null, null, orderBy);
    }

    public List<ToDo> findUnfinishedOrderByCreated() {
        String selection = COLUMN_FINISHED + "=?";
        String[] selectionArgs = new String[] { String.valueOf(-1) };
        String orderBy = COLUMN_CREATED + " ASC";
        return this.find(selection, selectionArgs, null, null, orderBy);
    }

    public List<ToDo> findFinishedOrderByCreated() {
        String selection = COLUMN_FINISHED + ">?";
        String[] selectionArgs = new String[] { String.valueOf(-1) };
        String orderBy = COLUMN_FINISHED + " DESC";
        return this.find(selection, selectionArgs, null, null, orderBy);
    }

    @Override
    protected ContentValues createValues(ToDo entity, ContectValuesState state) {
        ContentValues values = super.createValues(entity, state);

        if (state == ContectValuesState.CREATE) {
            values.put(COLUMN_CREATED, entity.getCreated().getTime());
        }

        if (entity.getFinished() == null) {
            values.put(COLUMN_FINISHED, -1);
        } else {
            values.put(COLUMN_FINISHED, entity.getFinished().getTime());
        }

        return values;
    }

    @Override
    protected ToDo parseValuse(Cursor cursor) {
        ToDo toDo = new ToDo(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)),
                cursor.getLong(cursor.getColumnIndex(COLUMN_VERSION)));

        toDo.setCreated(new Date(cursor.getLong(cursor.getColumnIndex(COLUMN_CREATED))));
        long finished = cursor.getLong(cursor.getColumnIndex(COLUMN_FINISHED));
        if (finished == -1) {
            toDo.setFinished(null);
        } else {
            toDo.setFinished(new Date(finished));
        }

        return toDo;
    }
}
