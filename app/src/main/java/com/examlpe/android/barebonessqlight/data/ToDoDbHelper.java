package com.examlpe.android.barebonessqlight.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by peterpomlett on 04/12/2017.
 */

public class ToDoDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "toDo.db";
    private static final int DATABASE_VERSION = 1;

    public ToDoDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_WAITLIST_TABLE = "CREATE TABLE " + ToDoContract.ToDoEntry.TABLE_NAME + " (" +
                ToDoContract.ToDoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ToDoContract.ToDoEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                ToDoContract.ToDoEntry.COLUMN_CHECK_MARK + " INTERGER NOT NULL" +
                "); ";
        sqLiteDatabase.execSQL(SQL_CREATE_WAITLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ToDoContract.ToDoEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
