package com.danielkioko.peachnotes;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

public class NewRemiderActivity extends AppCompatActivity {

    Context context;
    AlarmManager alarmManager;
    NewRemiderActivity inst;
    Button setReminder, cancelReminder;
    private PendingIntent pendingIntent;
    private TimePicker timePicker;
    private EditText reminderTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_remider);

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

        cancelReminder = findViewById(R.id.btnCanelReminder);
        cancelReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewRemiderActivity.this, ReminderActivity.class));
                finish();
            }
        });

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
