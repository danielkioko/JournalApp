package com.danielkioko.peachnotes;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.danielkioko.peachnotes.DB.TDDb;

public class NewToDoActivity extends AppCompatActivity {

    EditText todo;
    Button finished;
    Bundle extras;
    CheckBox checkBox;
    int id_To_Update = 0;
    private TDDb tdDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_to_do);

        todo = findViewById(R.id.et_todo);
        checkBox = findViewById(R.id.checkBox);
        finished = findViewById(R.id.btn_add_todo);
        finished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewToDoActivity.this, ToDoListActivity.class));
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String done = "done";
                if (!checkBox.isChecked()) {
                    tdDb.updateToDO(id_To_Update, todo.getText().toString(), done);
                } else {
                    tdDb.updateToDO(id_To_Update, todo.getText().toString(), "");
                }
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
                todo.setText(note);
                if (done != null) {
                    checkBox.setChecked(true);
                } else {
                    checkBox.setChecked(false);
                }
            }
        }

    }
}
