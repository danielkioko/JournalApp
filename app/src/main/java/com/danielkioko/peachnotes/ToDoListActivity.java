package com.danielkioko.peachnotes;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.danielkioko.peachnotes.DB.TDDb;

public class ToDoListActivity extends AppCompatActivity {

    ListView listView;
    FloatingActionButton floatingActionButton;
    TDDb tdDb;
    int id_To_Update = 0;
    String value = "done";
    SimpleCursorAdapter adapter;
    private CheckBox checkBox;
    private String note;
    private String is_done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);

        listView = findViewById(R.id.todoLV);
        checkBox = findViewById(R.id.checkBox_and_Text);

        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ToDoListActivity.this, NewToDoActivity.class));
            }
        });

        tdDb = new TDDb(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int Value = extras.getInt("id");
            if (Value > 0) {
                Cursor rs = tdDb.getData(Value);
                id_To_Update = Value;
                rs.moveToFirst();
                String note = rs.getString(rs.getColumnIndex(TDDb.note));
                String done = rs.getString(rs.getColumnIndex(TDDb.done));
                if (!rs.isClosed()) {
                    rs.close();
                }
            }
        }

        Cursor c = tdDb.fetchAll();
        String[] fieldNames = new String[]{TDDb.note, TDDb.done};

        int[] display = new int[]{
                R.id.checkBox_and_Text
        };

        adapter = new SimpleCursorAdapter(this, R.layout.todo_template, c, fieldNames, display, 0);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                checkBox.setChecked(true);

                if (note != null) {
                    setValueChecked();
                } else {
                    setValueUncheked();
                }

            }
        });

    }

    private void setValueUncheked() {
        tdDb.updateToDO(id_To_Update, note, is_done);
    }

    private void setValueChecked() {
        tdDb.updateToDO(id_To_Update, note, is_done);
    }

}
