package com.danielkioko.peachnotes;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.view.View;
import android.widget.Toast;

public class BackupService extends Service {

    AlarmManager alarmManager;
    PendingIntent pendingIntent;

    public BackupService() {

        Intent alarmIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        Toast.makeText(this, "Alarm Canceled", Toast.LENGTH_SHORT).show();

        startAlarm(null);

    }

    public void startAlarm(View view) {
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int interval = 10000;

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
        Toast.makeText(this, "Your Notes Are Backing Up!", Toast.LENGTH_SHORT).show();
        cancelAlarm(view);
    }

    public void cancelAlarm(View view) {
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
            Toast.makeText(this, "Finished Backing Up Your Notes", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
