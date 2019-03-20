package com.danielkioko.peachnotes.Notes;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

import com.danielkioko.peachnotes.DB.NDb;
import com.danielkioko.peachnotes.R;

import java.util.ArrayList;
import java.util.Locale;

public class SearchListActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<Note> noteList = new ArrayList<Note>();
    ListAdapter listAdapter;

    NDb sqLiteHelper;
    EditText etSearch;
    Note note;
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);

        listView = findViewById(R.id.searchListView);
        etSearch = findViewById(R.id.editTextListSearch);

        listView.setTextFilterEnabled(true);
        sqLiteHelper = new NDb(this);
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();

        cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + NDb.mynotes + "", null);

        noteList = new ArrayList<Note>();

        if (cursor.moveToFirst()) {
            do {
                String noteName = cursor.getString(cursor.getColumnIndex(NDb.name));
                String noteDate = cursor.getString(cursor.getColumnIndex(NDb.dates));

                note = new Note(noteName, noteDate);

                noteList.add(note);

            } while (cursor.moveToNext());
        }

        listAdapter = new ListAdapter(SearchListActivity.this, R.layout.search_list_template, noteList);
        listView.setAdapter(listAdapter);
        cursor.close();

        etSearch.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String text = etSearch.getText().toString().toLowerCase(Locale.getDefault());
                listAdapter.filter(text);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence stringVar, int start, int before, int count) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
