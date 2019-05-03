package com.danielkioko.peachnotes.SettingsAndPreferences;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.danielkioko.peachnotes.Notes.HomeActivity;
import com.danielkioko.peachnotes.Notes.SearchListActivity;
import com.danielkioko.peachnotes.R;

import java.util.List;
import java.util.Locale;

public class Setting extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SharedPref sharedPref;
    SecurityPref securityPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Toolbar toolbar = findViewById(R.id.toolbar_settings);
        toolbar.setBackgroundResource(R.color.colorPrimaryDark);
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

    protected Boolean isActivityRunning(Class activityClass) {
        ActivityManager activityManager = (ActivityManager) getBaseContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

        for (ActivityManager.RunningTaskInfo task : tasks) {
            if (activityClass.getCanonicalName().equalsIgnoreCase(task.baseActivity.getClassName()))
                return true;
        }

        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id = menuItem.getItemId();

        if (id == R.id.app_bar_search) {
            if (!isActivityRunning(SearchListActivity.class)) {
                startActivity(new Intent(getApplicationContext(), SearchListActivity.class));
            }
        } else if (id == R.id.nav_notes) {
            if (!isActivityRunning(HomeActivity.class)) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();
            }
        } else if (id == R.id.nav_settings) {
            if (!isActivityRunning(Setting.class)) {
                startActivity(new Intent(getApplicationContext(), Setting.class));
            }
        } else if (id == R.id.nav_about) {
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
