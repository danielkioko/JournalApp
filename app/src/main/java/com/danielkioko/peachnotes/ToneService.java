package com.danielkioko.peachnotes;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.Random;

public class ToneService extends Service {

    MediaPlayer mediaPlayer;
    private boolean isRunning;
    private Context context;
    private int startId;

    public ToneService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("MyActivity", "In the Richard service");
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        final NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent intent1 = new Intent(this.getApplicationContext(), NewRemiderActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent1, 0);

        Notification notification = new Notification.Builder(this)
                .setContentTitle("Alarm")
                .setContentText("alarm")
                .setSmallIcon(R.drawable.smilingpeach)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        String state = intent.getExtras().getString("extra");

        Log.e("what is going on here  ", state);

        assert state != null;
        switch (state) {
            case "no":
                startId = 0;
                break;
            case "yes":
                startId = 1;
                break;
            default:
                startId = 0;
                break;
        }

        if (!this.isRunning && startId == 1) {
            Log.e("if there was not sound ", " and you want start");

            int min = 1;
            int max = 9;

            Random r = new Random();
            int random_number = r.nextInt(max - min + 1) + min;
            Log.e("random number is ", String.valueOf(random_number));

            if (random_number == 1) {
                mediaPlayer = MediaPlayer.create(this, R.raw.reminder_tone);
            } else if (random_number == 2) {
                mediaPlayer = MediaPlayer.create(this, R.raw.reminder_tone);
            } else if (random_number == 3) {
                mediaPlayer = MediaPlayer.create(this, R.raw.reminder_tone);
            } else if (random_number == 4) {
                mediaPlayer = MediaPlayer.create(this, R.raw.reminder_tone);
            } else if (random_number == 5) {
                mediaPlayer = MediaPlayer.create(this, R.raw.reminder_tone);
            } else if (random_number == 6) {
                mediaPlayer = MediaPlayer.create(this, R.raw.reminder_tone);
            } else if (random_number == 7) {
                mediaPlayer = MediaPlayer.create(this, R.raw.reminder_tone);
            } else if (random_number == 8) {
                mediaPlayer = MediaPlayer.create(this, R.raw.reminder_tone);
            } else if (random_number == 9) {
                mediaPlayer = MediaPlayer.create(this, R.raw.reminder_tone);
            } else {
                mediaPlayer = MediaPlayer.create(this, R.raw.reminder_tone);
            }

            mediaPlayer.start();


            notificationManager.notify(0, notification);

            this.isRunning = true;
            this.startId = 0;

        } else if (!this.isRunning && startId == 0) {
            Log.e("if there was not sound ", " and you want end");

            this.isRunning = false;
            this.startId = 0;

        } else if (this.isRunning && startId == 1) {
            Log.e("if there is sound ", " and you want start");

            this.isRunning = true;
            this.startId = 0;

        } else {
            Log.e("if there is sound ", " and you want end");

            mediaPlayer.stop();
            mediaPlayer.reset();

            this.isRunning = false;
            this.startId = 0;
        }

        Log.e("MyActivity", "In the service");
        return START_NOT_STICKY;

    }

    @Override
    public void onDestroy() {
        Log.e("JSLog", "on destroy called");
        super.onDestroy();
        this.isRunning = false;
    }

}
