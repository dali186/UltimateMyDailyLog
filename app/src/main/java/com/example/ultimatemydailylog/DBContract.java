package com.example.ultimatemydailylog;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

interface DBContract {
    static final String TABLE_NAME="DIARY_D";
    static final String COL_ID="ID";
    static final String COL_TITLE="DIARY";
    static final String COL_WHEN="DATE";
    static final String COL_DIET="DIET";
    static final String COL_STRENGTH="STRENGTH";
    static final String COL_MENTAL="MENTAL";
    static final String COL_MSG="MSG";

    static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
            TABLE_NAME + "(" +
            COL_ID + " INTEGER NOT NULL PRIMARY KEY, " +
            COL_TITLE + " TEXT NOT NULL, " +
            COL_WHEN + " TEXT NOT NULL, " +
            COL_DIET + " TEXT NOT NULL, " +
            COL_STRENGTH + " TEXT NOT NULL, " +
            COL_MENTAL + " TEXT NOT NULL, " +
            COL_MSG + " TEXT NOT NULL)";
    static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    static final String SQL_LOAD = "SELECT * FROM " + TABLE_NAME;
    static final String SQL_SELECT = "SELECT * FROM "  + TABLE_NAME + " WHERE " + COL_TITLE + "=?";
    static final String SQL_DATE_SELECT = "SELECT * FROM "  + TABLE_NAME + " WHERE " + COL_WHEN + "=?";
    static final String SQL_SELECT_ID = "SELECT ID FROM "  + TABLE_NAME + " WHERE " + COL_TITLE + "=? and " + COL_WHEN + "=?";
}

class  DBHelper extends SQLiteOpenHelper {
    static final String DB_FILE = "diary_d.db";
    static final int DB_VERSION = 1;

    DBHelper(Context context) {
        super(context, DB_FILE, null, DB_VERSION); // 세번째 인자 : cursor factory
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBContract.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DBContract.SQL_DROP_TABLE);
        onCreate(db);
    }
}
