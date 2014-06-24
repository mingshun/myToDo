package com.gutspot.apps.android.mytodo.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "main";

    private static final String CREATE_TABLE_TODO_SQL = "CREATE TABLE `todo` ("
            + "`_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + "`finish` INTEGER NOT NULL DEFAULT 0,"
            + "`finished` INTEGET DEFAULT 0,"
            + "`note` TEXT DEFAULT '');";

    private static final String CREATE_TABLE_TODO_CONTENT_SQL = "CREATE TABLE `todo_content` ("
            + "`_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + "`todo_id` INTEGER NOT NULL,"
            + "`content` TEXT NOT NULL,"
            + "`created` INTEGER NOT NULL);";

    private static final String CREATE_TABLE_NOTICE_SQL = "CREATE TABLE `notice` ("
            + "`_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + "`todo_id` INTEGER NOT NULL,"
            + "`time` INTEGER NOT NULL);";

    public DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TODO_SQL);
        db.execSQL(CREATE_TABLE_TODO_CONTENT_SQL);
        db.execSQL(CREATE_TABLE_NOTICE_SQL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

}
