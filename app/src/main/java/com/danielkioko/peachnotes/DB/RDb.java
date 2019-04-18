package com.danielkioko.peachnotes.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class RDb extends SQLiteOpenHelper {

    public static final String dbname = "MyReminder.db";
    public static final String _id = "_id";
    public static final String title = "title";
    public static final String hour = "hour";
    public static final String minute = "minute";
    public static final String myreminders = "myreminders";
    SQLiteDatabase db;
    private HashMap hp;

    public RDb(Context context) {
        super(context, dbname, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL("create table myreminders"
                + "(_id integer primary key, title text, hour text, minute text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + myreminders);
        onCreate(db);
    }

    public Cursor fetchAll() {
        db = this.getReadableDatabase();
        Cursor mCursor = db.query(myreminders, new String[]{"_id", "title",
                "hour", "minute"}, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public boolean insertReminder(String title, String hour, String minute) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("hour", hour);
        contentValues.put("minute", minute);
        contentValues.put("title", title);

        db.insert(myreminders, null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor z = db.rawQuery("select * from " + myreminders + " where _id=" + id
                + "", null);
        return z;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, myreminders);
        return numRows;
    }

    public boolean updateReminder(Integer id, String title, String hour, String minute) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("title", title);
        contentValues.put("hour", hour);
        contentValues.put("minute", minute);

        db.update(myreminders, contentValues, "_id = ? ",
                new String[]{Integer.toString(id)});
        return true;
    }

    public Integer deleteReminder(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(myreminders, "_id = ? ",
                new String[]{Integer.toString(id)});
    }

    public ArrayList getAll() {
        ArrayList array_list = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + myreminders, null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex("_id")));
            array_list.add(res.getString(res.getColumnIndex(hour)));
            array_list.add(res.getString(res.getColumnIndex(minute)));
            array_list.add(res.getString(res.getColumnIndex(title)));
            res.moveToNext();
        }
        return array_list;
    }

}