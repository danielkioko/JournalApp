package com.danielkioko.peachnotes;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {

    SharedPreferences sharedPreferences;

    public SharedPref(Context context) {
        sharedPreferences = context.getSharedPreferences("filename", Context.MODE_PRIVATE);
    }

    public void setNightModeState(Boolean state) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("NightMode", state);
        editor.commit();
    }

    public Boolean loadNightModeState() {
        Boolean state = sharedPreferences.getBoolean("NightMode", false);
        return state;
    }

    public void setBackupEnabled(Boolean state) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("BackupEnabled", state);
        editor.commit();
    }

    public Boolean loadBackupState() {
        Boolean state = sharedPreferences.getBoolean("BackupEnabled", true);
        return state;
    }

    public void setLockState(Boolean state) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("LockMode", state);
        editor.commit();
    }

    public Boolean loadLockState() {
        Boolean state = sharedPreferences.getBoolean("LockEnabled", false);
        return state;
    }

}
