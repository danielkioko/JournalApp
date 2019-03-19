package com.danielkioko.peachnotes.SettingsAndPreferences;

import android.content.Context;
import android.content.SharedPreferences;

public class SecurityPref {

    SharedPreferences sharedPreferences;

    public SecurityPref(Context context) {
        sharedPreferences = context.getSharedPreferences("name", Context.MODE_PRIVATE);
    }

    public void setLockPrefState(Boolean state) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("SecurityCheck", state);
        editor.commit();
    }

    public boolean loadSecurityCheck() {
        Boolean state = sharedPreferences.getBoolean("SecurityCheck", false);
        return state;
    }

}
