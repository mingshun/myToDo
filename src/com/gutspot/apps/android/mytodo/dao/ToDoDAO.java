package com.gutspot.apps.android.mytodo.dao;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.gutspot.apps.android.mytodo.model.ToDo;

public class ToDoDAO extends AbstractDAO<ToDo> {

    private static final String TABLE_NAME = "email";

    private static final String COLUMN_FINISH = "finish";
    private static final String COLUMN_FINISHED = "finished";
    private static final String COLUMN_NOTE = "note";

    protected ToDoDAO(Context context) {
        super(context, TABLE_NAME);
    }

    @Override
    protected ContentValues createValues(ToDo entity, boolean create) {
        ContentValues values = new ContentValues();

        values.put(COLUMN_FINISH, entity.isFinish() ? 1 : 0);
        values.put(COLUMN_FINISHED, entity.getFinished().getTime());
        values.put(COLUMN_NOTE, entity.getNote());

        return values;
    }

    @Override
    protected ToDo parseValuse(Cursor cursor) {
        ToDo toDo = new ToDo();

        toDo.setFinish(cursor.getInt(cursor.getColumnIndex(COLUMN_FINISH)) == 1 ? true : false);
        toDo.setFinished(new Date(cursor.getLong(cursor.getColumnIndex(COLUMN_FINISHED))));
        toDo.setNote(cursor.getString(cursor.getColumnIndex(COLUMN_NOTE)));

        return toDo;
    }

}
