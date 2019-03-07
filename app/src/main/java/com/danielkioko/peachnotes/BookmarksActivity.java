package com.danielkioko.peachnotes;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.danielkioko.peachnotes.DB.BDb;

public class BookmarksActivity extends AppCompatActivity {

    ListView listView;
    FloatingActionButton floatingActionButton;
    EditText etTitle, etUrl;

    SimpleCursorAdapter adapter;

    int id_to_update_bookmarks = 0;
    AlertDialog alertDialog;
    private BDb bDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);

        bDb = new BDb(this);

        listView = findViewById(R.id.bookmarksLV);
        floatingActionButton = findViewById(R.id.fad_add_bookmark);

        Cursor c = bDb.fetchAll();
        String[] fieldNames = new String[]{BDb.title, BDb._id, BDb.url};

        int[] dislay = new int[]{R.id.tv_bookmark, R.id.tv_bookmark_url};
        adapter = new SimpleCursorAdapter(this, R.layout.bookmark_layout, c, fieldNames, dislay, 0);

        listView.setAdapter(adapter);
        listView.setScrollBarSize(0);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog = new AlertDialog.Builder(BookmarksActivity.this).create();

                LayoutInflater layoutInflater = BookmarksActivity.this.getLayoutInflater();
                View dialogView = layoutInflater.inflate(R.layout.bookmark_dialog, null);

                etTitle = dialogView.findViewById(R.id.et_bookmark_title);
                etUrl = dialogView.findViewById(R.id.et_bookmark_url);

                Button save = dialogView.findViewById(R.id.btnSave);
                Button cancel = dialogView.findViewById(R.id.btnCancel);

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveBookmark();
                    }

                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

            }
        });

    }

    private void saveBookmark() {

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            int Value = extras.getInt("id");

            if (Value > 0) {

                if (etTitle.getText().toString().trim().equals("")
                        || etUrl.getText().toString().trim().equals("")) {

                    Toast.makeText(this, "Fill in the all fields", Toast.LENGTH_LONG).show();

                } else {

                    if (bDb.updateNotes(id_to_update_bookmarks, etTitle.getText()
                            .toString(), etUrl.getText()
                            .toString())) {

                        alertDialog.dismiss();

                    } else {
                        Toast.makeText(this, "There's an error. That's all I can tell. Sorry!", Toast.LENGTH_LONG).show();
                    }

                }

            } else {

                if (etTitle.getText().toString().trim().equals("")
                        || etUrl.getText().toString().trim().equals("")) {

                    bDb.deleteNotes(id_to_update_bookmarks);
                    alertDialog.dismiss();

                } else {

                    if (bDb.insertNotes(etTitle.getText().toString(),
                            etUrl.getText().toString())) {

                        alertDialog.dismiss();

                    } else {
                        Toast.makeText(this, "Unfortunately Task Failed.", Toast.LENGTH_LONG).show();
                    }
                }

            }

        }

    }
}
