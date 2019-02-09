package com.danielkioko.peachnotes.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class TDDb extends SQLiteOpenHelper {

    public static final String dbname = "ToDo.db";
    public static final String _id = "_id";
    public static final String note = "note";
    public static final String done = "done";
    public static final String todo = "todo";
    SQLiteDatabase db;
    private HashMap hashMap;

    public TDDb(Context context) {
        super(context, dbname, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" create table todo"
                + "(_id integer primary key, note text, done text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + todo);
        onCreate(db);
    }

    public Cursor fetchAll() {
        db = this.getReadableDatabase();
        Cursor cursor = db.query(todo, new String[]{
                "_id", "note"
        }, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public boolean insertNotes(String note, String done) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("note", note);
        contentValues.put("done", done);
        db.insert(todo, null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor z = db.rawQuery("select * from " + todo + " whre _id=" + id
                + "", null);
        return z;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int)
                DatabaseUtils.queryNumEntries(db, todo);
        return numRows;
    }

    public boolean updateToDO(Integer id, String notes, String done) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("note", note);
        contentValues.put("done", done);
        db.update(todo, contentValues, "_id = ? ",
                new String[]{
                        Integer.toString(id)
                });
        return true;
    }

    public Integer deleteTodo(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(todo, "_id = ?",
                new String[]{Integer.toString(id)
                });
    }

    public ArrayList getAll() {
        ArrayList arrayList = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + todo, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            arrayList.add(res.getString(res.getColumnIndex("_id")));
            arrayList.add(res.getString(res.getColumnIndex(note)));
            arrayList.add(res.getString(res.getColumnIndex(done)));
        }

        return arrayList;
    }

}
