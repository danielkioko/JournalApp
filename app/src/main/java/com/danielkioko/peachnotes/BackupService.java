package com.danielkioko.peachnotes;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class BackupService extends Service {

    SharedPref sharedPref;
    Backup backup;

    public BackupService() {

        if (sharedPref.loadBackupState() == true) {
                backup.exportDB();
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
