package com.gutspot.apps.android.mytodo.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gutspot.apps.android.mytodo.model.AbstractEntity;
import com.gutspot.apps.android.mytodo.utils.DBOpenHelper;

public abstract class AbstractDAO<T extends AbstractEntity> {
    protected static final String COLUMN_ID = "_id";
    protected static final String COLUMN_VERSION = "version";

    private DBOpenHelper helper;
    private String tableName;

    protected AbstractDAO(Context context, String tableName) {
        helper = new DBOpenHelper(context);
        this.tableName = tableName;
    }

    protected DBOpenHelper getDBOpenHelper() {
        return helper;
    }

    public long create(T entity) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = this.createValues(entity, true);
        long id = db.insert(tableName, null, values);
        db.close();
        return id;
    }

    public int update(T entity) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = this.createValues(entity, false);
        String selection = COLUMN_ID + "=?";
        String[] selectionArgs = new String[] { String.valueOf(entity.getId()) };
        int result = db.update(tableName, values, selection, selectionArgs);
        db.close();
        return result;
    }

    public int remove(long id) {
        String selection = COLUMN_ID + "=?";
        String[] selectionArgs = new String[] { String.valueOf(id) };
        return remove(selection, selectionArgs);
    }

    public int remove(String selection, String[] selectionArgs) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int result = db.delete(tableName, selection, selectionArgs);
        db.close();
        return result;
    }

    public T findById(long id) {
        SQLiteDatabase db = helper.getReadableDatabase();
        String selection = COLUMN_ID + "=?";
        String[] selectionArgs = new String[] { String.valueOf(id) };
        Cursor cursor = db.query(tableName, null, selection, selectionArgs,
                null, null, null);

        T entity = null;
        if (cursor.moveToFirst()) {
            entity = parseValuse(cursor);
        }
        cursor.close();
        return entity;
    }

    public List<T> find() {
        return this.find(null, null);
    }

    public List<T> find(String selection, String[] selectionArgs) {
        return this.find(selection, selectionArgs, null, null, null);
    }

    public List<T> find(String selection, String[] selectionArgs,
            String groupBy, String having, String orderBy) {
        return this.find(selection, selectionArgs, groupBy, having, orderBy, null);
    }

    public List<T> find(String selection, String[] selectionArgs,
            String groupBy, String having, String orderBy, String limit) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(tableName, null, selection, selectionArgs,
                groupBy, having, orderBy, limit);

        List<T> entities = new ArrayList<T>();
        while (cursor.moveToNext()) {
            T entity = parseValuse(cursor);
            entities.add(entity);
        }
        cursor.close();
        return entities;
    }

    protected ContentValues createValues(T entity, boolean create) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_VERSION, new Date().getTime());
        return values;
    }

    protected abstract T parseValuse(Cursor cursor);
}
