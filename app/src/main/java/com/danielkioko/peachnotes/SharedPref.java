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

    public void setLockEnabled(Boolean securityState) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("Secured", securityState);
        editor.commit();

    }

    public Boolean loadSecurityState() {

        Boolean state = sharedPreferences.getBoolean("Secured", false);
        return state;

    }

}
