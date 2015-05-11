package com.nanlagger.schooljournal.app.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static String DB_PATH = "/data/data/com.nanlagger.schooljournal/databases/";
    static String DB_NAME = "journal.jet";
    private static final int DB_VERSION = 1;
    private static DBHelper instance = null;
    private final Context myContext;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.myContext = context;
    }

    public static synchronized DBHelper getHelper(Context context) {
        if (instance == null)
            instance = new DBHelper(context);
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE profile ("
                        + "_id      INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                        + "login     TEXT    NOT NULL, "
                        + "password  TEXT NOT NULL "
                        + ");"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}