package com.danielkioko.peachnotes.Notes;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

public class ListAdapter extends ArrayAdapter<Note> {

    //define static variable
    public static String dbname = "MyNotes.db";
    public static String mynotes = "mynotes";
    //establish connection with SQLiteDataBase
    private final Context c;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase sqlDb;

    public ListAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        this.c = context;
    }

    public ListAdapter open() throws SQLException {
        dbHelper = new DatabaseHelper(c);
        sqlDb = dbHelper.getWritableDatabase();
        return this;
    }

    //fetch data
    public Cursor fetchAllData() {
        String query = "SELECT * FROM " + mynotes;
        Cursor row = sqlDb.rawQuery(query, null);
        if (row != null) {
            row.moveToFirst();
        }
        return row;
    }

    //fetch data by filter
    public Cursor fetchdatabyfilter(String inputText, String filtercolumn) throws SQLException {
        Cursor row = null;
        String query = "SELECT * FROM " + mynotes;
        if (inputText == null || inputText.length() == 0) {
            row = sqlDb.rawQuery(query, null);
        } else {
            query = "SELECT * FROM " + mynotes + " WHERE " + filtercolumn + " like '%" + inputText + "%'";
            row = sqlDb.rawQuery(query, null);
        }
        if (row != null) {
            row.moveToFirst();
        }
        return row;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, dbname, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // db.execSQL("CREATE TABLE IF NOT EXISTS "+mynotes+" (_id integer primary key, name, remark, date)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//            db.execSQL("DROP TABLE IF EXISTS "+mynotes);
//            onCreate(db);
        }
    }

}