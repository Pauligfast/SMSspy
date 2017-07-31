package com.example.paulig.spysms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE = "subscribers";
    private static final int VERSION = 2;
    private static final String TABLE_NAME = "numbers";
    private static final String COL_ID = "ID";
    private static final String NUMBER = "number";

    DatabaseHelper(Context context) {
        super(context, DATABASE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "create table " + TABLE_NAME + " (" +
                        COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        NUMBER + " TEXT NOT NULL" +
                        ")"
        );
    }

    boolean InsertValue(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NUMBER, name);
        return db.insert(TABLE_NAME, null, contentValues) != -1;
    }

    public boolean UpdateValue(int id, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID, id);
        contentValues.put(NUMBER, name);
        return db.update(TABLE_NAME, contentValues, COL_ID + "=" + id, null) != -1;
    }

    Cursor getAllData() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select * from " + TABLE_NAME, null);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(" drop table if exists " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
