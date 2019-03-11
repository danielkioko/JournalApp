package com.danielkioko.peachnotes;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BackupService extends Service {

    SharedPref sharedPref;
    Backup backup;

    public BackupService() {

        if (sharedPref.loadBackupState() == true) {

            Calendar c = Calendar.getInstance();
            System.out.println("Current time => " + c.getTime());

            SimpleDateFormat day = new SimpleDateFormat("dd");
            String formattedDate = day.format(c.getTime());

            if (formattedDate == "01") {
                backup.exportDB();
            }

        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
