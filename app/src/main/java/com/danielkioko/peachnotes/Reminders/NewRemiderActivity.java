package com.danielkioko.peachnotes.Reminders;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.danielkioko.peachnotes.DB.RDb;
import com.danielkioko.peachnotes.R;

import java.util.Calendar;

public class NewRemiderActivity extends AppCompatActivity {

    Context context;
    AlarmManager alarmManager;
    NewRemiderActivity inst;
    Button setReminder, cancelReminder;
    private PendingIntent pendingIntent;
    private TimePicker timePicker;
    private EditText reminderTitle;
    RDb rDb;
    String timeString;
    int id_to_update = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_remider);

        rDb = new RDb(this);

        this.context = this;
        reminderTitle = findViewById(R.id.etReminderTitle);
        timePicker = findViewById(R.id.alarmTimePicker);

        final Intent myIntent = new Intent(this, AlarmReceiver.class);
        final Calendar calendar = Calendar.getInstance();

        setReminder = findViewById(R.id.btnAddReminder);
        setReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                calendar.add(Calendar.SECOND, 3);

                final int hour = timePicker.getCurrentHour();
                final int minute = timePicker.getCurrentMinute();

                Log.e("MyActivity", "In the receiver with " + hour + " and " + minute);

                calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());

                myIntent.putExtra("extra", "yes");
                pendingIntent = PendingIntent.getBroadcast(NewRemiderActivity.this, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int Value = extras.getInt("id");
            if (Value > 0) {
                Cursor rs = rDb.getData(Value);
                id_to_update = Value;
                rs.moveToFirst();
                String title = rs.getString(rs.getColumnIndex(RDb.title));
                String hour = rs.getString(rs.getColumnIndex(RDb.hour));
                String minute = rs.getString(rs.getColumnIndex(RDb.minute));

                if (!rs.isClosed()) {
                    rs.close();
                }

                reminderTitle.setText(title);
                timePicker.setHour(Integer.parseInt(hour));
                timePicker.setMinute(Integer.parseInt(minute));

            }
        }

        cancelReminder = findViewById(R.id.btnCanelReminder);
        cancelReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rDb.deleteReminder(id_to_update);
                Toast.makeText(NewRemiderActivity.this, "Deleted", Toast.LENGTH_LONG).show();
                startActivity(new Intent(NewRemiderActivity.this, ReminderActivity.class));
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(NewRemiderActivity.this, ReminderActivity.class));
        finish();
    }

    public void setAlarmText(String alarmText) {
        reminderTitle.setText(alarmText);
    }

    @Override
    protected void onStart() {
        super.onStart();
        inst = this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("MyActivity", "On Destroy");
    }

}
