package com.danielkioko.peachnotes;

import android.app.Service;
import android.app.backup.BackupAgentHelper;
import android.app.backup.SharedPreferencesBackupHelper;
import android.content.Intent;
import android.os.IBinder;

public class BackupService extends Service {

    static final String PREFS = "myprefs";
    static final String PREFS_BACKUP_KEY = "myprefs";

    public BackupService() {


        //Log.i("MyFile")

        class MyBackUpPlace extends BackupAgentHelper {

            static final String File_Name_Of_Prefrences = "myPrefrences";
            SharedPreferencesBackupHelper helper = new SharedPreferencesBackupHelper(this, File_Name_Of_Prefrences);

        }


    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
