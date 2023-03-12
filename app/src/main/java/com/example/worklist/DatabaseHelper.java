package com.example.worklist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DatabaseHelper extends SQLiteOpenHelper {
    static final String DATABASE_NAME = "MAIN_DB.DB";
    static final int DATABASE_VERSION =1;
    private   static final String CREATE_DB_QUERY="CREATE TABLE TASK (Id INTEGER PRIMARY KEY AUTOINCREMENT,Title TEXT,Description TEXT,INDateTime TEXT,RDateTime TEXT,Checkbox INTEGER,Complete INTEGER,DdateTime Text,Uid INTEGER)";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_DB_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS TASK");

    }
}
