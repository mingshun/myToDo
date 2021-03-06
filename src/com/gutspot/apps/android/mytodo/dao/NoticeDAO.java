package com.gutspot.apps.android.mytodo.dao;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.gutspot.apps.android.mytodo.model.Notice;

public class NoticeDAO extends AbstractDAO<Notice> {

    private static final String TABLE_NAME = "notice";

    private static final String COLUMN_TODO_ID = "todo_id";
    private static final String COLUMN_TIME = "time";

    public NoticeDAO(Context context) {
        super(context, TABLE_NAME);
    }

    @Override
    protected ContentValues createValues(Notice entity, ContectValuesState state) {
        ContentValues values = super.createValues(entity, state);

        values.put(COLUMN_TODO_ID, entity.getToDoId());
        values.put(COLUMN_TIME, entity.getTime().getTime());

        return values;
    }

    @Override
    protected Notice parseValuse(Cursor cursor) {
        Notice notice = new Notice(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)),
                cursor.getLong(cursor.getColumnIndex(COLUMN_VERSION)));

        notice.setToDoId(cursor.getLong(cursor.getColumnIndex(COLUMN_TODO_ID)));
        notice.setTime(new Date(cursor.getLong(cursor.getColumnIndex(COLUMN_TIME))));

        return notice;
    }

}
