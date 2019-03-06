package com.danielkioko.peachnotes.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class BDb extends SQLiteOpenHelper {

    public static final String dbname = "MyBookmarks.db";
    public static final String _id = "_id";
    public static final String title = "title";
    public static final String url = "url";
    public static final String mybookmars = "mybookmarks";
    SQLiteDatabase db;
    private HashMap hp;

    public BDb(Context context) {
        super(context, dbname, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL("create table mynotes"
                + "(_id integer primary key, title text, url text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + mybookmars);
        onCreate(db);
    }

    public Cursor fetchAll() {
        db = this.getReadableDatabase();
        Cursor mCursor = db.query(mybookmars, new String[]{"_id", "title",
                "url"}, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public boolean insertNotes(String title, String url) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("title", title);
        contentValues.put("url", url);

        db.insert(mybookmars, null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor z = db.rawQuery("select * from " + mybookmars + " where _id=" + id
                + "", null);
        return z;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, mybookmars);
        return numRows;
    }

    public boolean updateNotes(Integer id, String title, String url) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("title", title);
        contentValues.put("url", url);

        db.update(mybookmars, contentValues, "_id = ? ",
                new String[]{Integer.toString(id)});
        return true;
    }

    public Integer deleteNotes(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(mybookmars, "_id = ? ",
                new String[]{Integer.toString(id)});
    }

    public ArrayList getAll() {
        ArrayList array_list = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + mybookmars, null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex("_id")));
            array_list.add(res.getString(res.getColumnIndex(title)));
            array_list.add(res.getString(res.getColumnIndex(url)));
            res.moveToNext();
        }
        return array_list;
    }

}