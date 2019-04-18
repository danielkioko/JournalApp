package com.danielkioko.peachnotes.Reminders;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.danielkioko.peachnotes.DB.RDb;
import com.danielkioko.peachnotes.R;
import com.danielkioko.peachnotes.SettingsAndPreferences.SharedPref;

public class ReminderActivity extends AppCompatActivity {

    FloatingActionButton floatingActionButton;
    ListView listView;
    RDb rDb;
    SimpleCursorAdapter simpleCursorAdapter;
    SharedPref sharedPref;
    private int id_to_update = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);

        listView = findViewById(R.id.lvRemiders);

        floatingActionButton = findViewById(R.id.fabNewRemider);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("id", 0);
                Intent intent = new Intent(ReminderActivity.this, NewRemiderActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        Cursor c = rDb.fetchAll();
        String[] fileNames = new String[]{RDb._id, RDb.title, RDb.hour, RDb.minute};
        int[] display = new int[]{R.id.reminder_id, R.id.tvReminderTitle, R.id.tvReminderTimeHour, R.id.tvReminderTimeMinute};
        simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.reminder_template, c, fileNames, display, 0);
        listView.setAdapter(simpleCursorAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                ConstraintLayout constraintLayout = (ConstraintLayout) arg1;
                CardView cardView = (CardView) constraintLayout.getChildAt(1);
                ConstraintLayout constraintLayoutChild = (ConstraintLayout) cardView.getChildAt(0);

                final TextView textView = (TextView) constraintLayoutChild.getChildAt(1);

                final Bundle bundle = new Bundle();
                bundle.putInt("id", Integer.parseInt(textView.getText().toString()));
                Intent intent = new Intent(getApplicationContext(), NewRemiderActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

    }
}
