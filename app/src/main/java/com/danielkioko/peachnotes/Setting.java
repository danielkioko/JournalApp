package com.danielkioko.peachnotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.danielkioko.peachnotes.Notes.HomeActivity;

public class Setting extends AppCompatActivity {

    SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        sharedPref = new SharedPref(this);

        if (sharedPref.loadNightModeState() == true) {
            setTheme(R.style.DarkAppTheme_NoActionBar);
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            setTheme(R.style.AppTheme_NoActionBar);
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO);
        }

        Switch aSwitch = findViewById(R.id.switch_dark_mode);
        Switch bSwitch = findViewById(R.id.backup_switch);
        Switch cSwitch = findViewById(R.id.switch_fingerprint_unlock);

        if (sharedPref.loadNightModeState()) {
            aSwitch.setChecked(true);
        }

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    sharedPref.setNightModeState(true);
                    restartApp();
                } else {
                    sharedPref.setNightModeState(false);
                    restartApp();
                }

            }
        });

        if (sharedPref.loadBackupState()) {
            bSwitch.setChecked(true);
        } else {
            bSwitch.setChecked(false);
        }

        bSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    sharedPref.setBackupEnabled(true);
                } else {
                    sharedPref.setBackupEnabled(false);
                }

            }
        });

        if (sharedPref.loadLockState()) {
            cSwitch.setChecked(true);
        } else {
            cSwitch.setChecked(false);
        }

        cSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    sharedPref.setLockState(true);
                } else {
                    sharedPref.setLockState(false);
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        finish();
    }

    private void restartApp() {
        startActivity(new Intent(getApplicationContext(), Setting.class));
        finish();
    }

}
