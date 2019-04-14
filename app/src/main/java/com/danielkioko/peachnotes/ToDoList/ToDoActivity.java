package com.danielkioko.peachnotes.ToDoList;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.danielkioko.peachnotes.DB.ADb;
import com.danielkioko.peachnotes.R;
import com.danielkioko.peachnotes.SettingsAndPreferences.SharedPref;

public class ToDoActivity extends AppCompatActivity {

    SharedPref sharedPref;
    ListView listView;
    FloatingActionButton newTodo;
    SimpleCursorAdapter adapter;
    ADb aDb;
    private int id_to_update = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = new SharedPref(this);

        newTodo = findViewById(R.id.fabNewToDo);
        newTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ToDoActivity.this, NewToDo.class));
            }
        });

        if (sharedPref.loadNightModeState()) {
            setTheme(R.style.DarkAppTheme_NoActionBar);
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            setTheme(R.style.AppTheme_NoActionBar);
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO);
        }

        setContentView(R.layout.activity_to_do);
        listView = findViewById(R.id.todoLv);

        Cursor c = aDb.fetchAll();
        String[] fieldNames = new String[]{ADb.task, ADb.date, ADb._id, ADb.is_done};
        int[] display = new int[]{R.id.todoCheckBox, R.id.tvTime};

        adapter = new SimpleCursorAdapter(this, R.layout.todo_template, c, fieldNames, display, 0);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                ConstraintLayout constraintLayout = (ConstraintLayout) arg1;
                final TextView textViewId = (TextView)
                        constraintLayout.getChildAt(0);
                final CheckBox checkBox = (CheckBox) constraintLayout.getChildAt(1);
                final TextView textViewTime = (TextView) constraintLayout.getChildAt(2);

                final Bundle dataBundle = new Bundle();
                dataBundle.putInt("id", Integer.parseInt(textViewId.getText().toString()));

                if (checkBox.isChecked()) {
                    aDb.updateIfTaskDone(Integer.parseInt(textViewId.toString()), "done");
                } else {
                    aDb.updateIfTaskDone(Integer.parseInt(textViewId.toString()), "notDone");
                }

            }
        });

    }
}
