package com.danielkioko.peachnotes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.ListView;

public class ToDoListActivity extends AppCompatActivity {

    ListView listView;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);

        listView = findViewById(R.id.todoLV);
        checkBox = findViewById(R.id.checkBox_and_Text);


    }
}
