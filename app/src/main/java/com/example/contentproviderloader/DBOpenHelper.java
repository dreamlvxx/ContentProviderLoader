package com.example.contentproviderloader;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {

    private Context context;

    private static final String DB_NAME = "dream_xx.db";
    public static final String TABLE_PEOPEL_NAME = "people";

    private static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_PEOPEL_NAME
            + "("
            + "_id INTEGER,"
            + "id INTEGER PRIMARY KEY,"
            + "name VARCHAR(20) NOT NULL"
            + ")";

    public DBOpenHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
    }

    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}