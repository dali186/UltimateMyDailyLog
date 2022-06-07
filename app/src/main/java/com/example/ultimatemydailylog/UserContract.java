package com.example.ultimatemydailylog;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

interface UserContract {
    static final String TABLE_NAME="USER_D";
    static final String COL_ID="ID";
    static final String COL_NAME="NAME";
    static final String COL_SEX="SEX";
    static final String COL_HEIGHT="HEIGHT";
    static final String COL_WEIGHT="WEIGHT";

    static final String USER_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
            TABLE_NAME + "(" +
            COL_ID + " INTEGER NOT NULL PRIMARY KEY, " +
            COL_NAME + " TEXT NOT NULL, " +
            COL_SEX + " TEXT NOT NULL, " +
            COL_HEIGHT + " INTEGER NOT NULL, " +
            COL_WEIGHT + " INTEGER NOT NULL)";
    static final String USER_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    static final String USER_LOAD = "SELECT * FROM " + TABLE_NAME;
    static final String USER_SELECT = "SELECT * FROM "  + TABLE_NAME + " WHERE " + COL_NAME + "=?";
    static final String USER_SELECT_ID = "SELECT ID FROM "  + TABLE_NAME + " WHERE " + COL_NAME + "=? and " + COL_SEX + "=?";
}

class  UserHelper extends SQLiteOpenHelper {
    static final String DB_FILE = "user_d.db";
    static final int DB_VERSION = 1;

    UserHelper(Context context) {
        super(context, DB_FILE, null, DB_VERSION); // 세번째 인자 : cursor factory
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UserContract.USER_CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(UserContract.USER_DROP_TABLE);
        onCreate(db);
    }
}
