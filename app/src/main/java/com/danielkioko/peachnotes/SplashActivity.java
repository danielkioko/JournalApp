package com.danielkioko.peachnotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.danielkioko.peachnotes.Authentication.AuthenticationActivity;
import com.danielkioko.peachnotes.Notes.HomeActivity;

public class SplashActivity extends AppCompatActivity {

    SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (sharedPref.loadSecurityState() == true) {
            startActivity(new Intent(SplashActivity.this, AuthenticationActivity.class));
            finish();
        } else {
            startActivity(new Intent(SplashActivity.this, HomeActivity.class));
            finish();
        }

    }
}
