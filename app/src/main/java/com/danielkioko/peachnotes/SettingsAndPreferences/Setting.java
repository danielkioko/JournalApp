package com.danielkioko.peachnotes.SettingsAndPreferences;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.danielkioko.peachnotes.Notes.HomeActivity;
import com.danielkioko.peachnotes.R;

import java.util.Locale;

public class Setting extends AppCompatActivity {

    SharedPref sharedPref;
    SecurityPref securityPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Toolbar toolbar = findViewById(R.id.toolbar_settings);
        setSupportActionBar(toolbar);

        TextView textView = findViewById(R.id.paperPenToolbarTitleInSettings);
        AssetManager assetManager = getApplicationContext().getAssets();
        Typeface typeface = Typeface.createFromAsset(assetManager, String.format(Locale.US, "fonts/%s", "Pacifico.ttf"));
        textView.setTypeface(typeface);

        sharedPref = new SharedPref(this);
        securityPref = new SecurityPref(this);

        if (sharedPref.loadNightModeState()) {
            setTheme(R.style.DarkAppTheme_NoActionBar);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            setTheme(R.style.AppTheme_NoActionBar);
            AppCompatDelegate.setDefaultNightMode
                    (AppCompatDelegate.MODE_NIGHT_NO);
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

        if (securityPref.loadSecurityCheck()) {
            cSwitch.setChecked(true);
        } else {
            cSwitch.setChecked(false);
        }

        cSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    securityPref.setLockPrefState(true);
                } else {
                    securityPref.setLockPrefState(false);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
    }

    private void restartApp() {
        startActivity(new Intent(getApplicationContext(), Setting.class));
        finish();
    }

}
