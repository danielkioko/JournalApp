package com.danielkioko.peachnotes.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class ADb extends SQLiteOpenHelper {

    public static final String dbname = "MyTasks.db";
    public static final String _id = "id";
    public static final String task = "task";
    public static final String is_done = "is_done";
    public static final String date = "date";
    public static final String mytasks = "mytasks";
    SQLiteDatabase db;
    private HashMap hp;

    public ADb(Context context) {
        super(context, dbname, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table mytasks"
                + "(_id integer primary key, task text, is_done text, date text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + mytasks);
        onCreate(db);
    }

    public Cursor fetchAll() {
        db = this.getReadableDatabase();
        Cursor mCursor = db.query(mytasks, new String[]{"_id", "task",
                "is_done", "date"}, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public boolean insertTask(String task, String is_done, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("task", task);
        contentValues.put("is_done", is_done);
        contentValues.put("date", date);

        db.insert(mytasks, null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor z = db.rawQuery("select * from " + mytasks + " where _id=" + id
                + "", null);
        return z;
    }

    public boolean updateIfTaskDone(Integer id, String is_done) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("is_done", is_done);

        db.update(mytasks, contentValues, "_id = ? ",
                new String[]{Integer.toString(id)});
        return true;

    }

    public boolean updateTask(Integer id, String task, String is_done,
                              String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("task", task);
        contentValues.put("is_done", is_done);
        contentValues.put("date", date);

        db.update(mytasks, contentValues, "_id = ? ",
                new String[]{Integer.toString(id)});
        return true;
    }

    public Integer deleteTask(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(mytasks, "_id = ? ",
                new String[]{Integer.toString(id)});
    }

    public ArrayList getAll() {
        ArrayList array_list = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + mytasks, null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex("_id")));
            array_list.add(res.getString(res.getColumnIndex(task)));
            array_list.add(res.getString(res.getColumnIndex(is_done)));
            array_list.add(res.getString(res.getColumnIndex(date)));
            res.moveToNext();
        }
        return array_list;
    }

}
