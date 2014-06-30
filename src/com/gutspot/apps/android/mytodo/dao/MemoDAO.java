package com.gutspot.apps.android.mytodo.dao;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.gutspot.apps.android.mytodo.model.Memo;

public class MemoDAO extends AbstractDAO<Memo> {

    private static final String TABLE_NAME = "memo";

    private static final String COLUMN_TODO_ID = "todo_id";
    private static final String COLUMN_CONTENT = "content";
    private static final String COLUMN_TEXT_COLOR = "text_color";
    private static final String COLUMN_BACKGROUND_COLOR = "background_color";
    private static final String COLUMN_CREATED = "created";

    public MemoDAO(Context context) {
        super(context, TABLE_NAME);
    }

    @Override
    protected ContentValues createValues(Memo entity, boolean create) {
        ContentValues values = new ContentValues();

        values.put(COLUMN_TODO_ID, entity.getToDoId());
        values.put(COLUMN_CONTENT, entity.getContent());
        values.put(COLUMN_TEXT_COLOR, entity.getTextColor());
        values.put(COLUMN_BACKGROUND_COLOR, entity.getBackgroundColor());
        if (create) {
            values.put(COLUMN_CREATED, entity.getCreated().getTime());
        }

        return values;
    }

    @Override
    protected Memo parseValuse(Cursor cursor) {
        Memo memo = new Memo();

        memo.setToDoId(cursor.getLong(cursor.getColumnIndex(COLUMN_TODO_ID)));
        memo.setContent(cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT)));
        memo.setTextColor(cursor.getInt(cursor.getColumnIndex(COLUMN_TEXT_COLOR)));
        memo.setBackgroundColor(cursor.getInt(cursor.getColumnIndex(COLUMN_BACKGROUND_COLOR)));
        memo.setCreated(new Date(cursor.getLong(cursor.getColumnIndex(COLUMN_CREATED))));

        return memo;
    }

}
