package com.example.tallybook.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "tallybook.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_RECORD = "record";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_REMARK = "remark";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_RECORD + "(" 
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_AMOUNT + " real not null, "
            + COLUMN_TYPE + " text not null, "
            + COLUMN_CATEGORY + " text not null, "
            + COLUMN_DATE + " text not null, "
            + COLUMN_REMARK + " text);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECORD);
        onCreate(db);
    }
} 