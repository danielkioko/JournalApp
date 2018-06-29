package com.danielkioko.peachnotes;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.danielkioko.peachnotes.Accounts.LoginActivity;
import com.danielkioko.peachnotes.IntroTabs.IntroActivity;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileSettings extends AppCompatActivity {

    TextView peachNotesTitle, peachNotesdescription, developerName, about;
    Button exitToLogin, toGuide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        exitToLogin = findViewById(R.id.btnLogout);

        toGuide = findViewById(R.id.btnToGuide);
        toGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileSettings.this, IntroActivity.class);
                startActivity(intent);
            }
        });

        peachNotesTitle = findViewById(R.id.peachNotesTitle);
        Typeface titleFont = Typeface.createFromAsset(getAssets(), "Pacifico.ttf");
        peachNotesTitle.setTypeface(titleFont);

        peachNotesdescription = findViewById(R.id.description);
        Typeface descriptionFont = Typeface.createFromAsset(getAssets(), "JosefinSans-Bold.ttf");
        peachNotesdescription.setTypeface(descriptionFont);

        developerName = findViewById(R.id.developerName);
        Typeface nameFont = Typeface.createFromAsset(getAssets(), "JosefinSans-Regular.ttf");
        developerName.setTypeface(nameFont);

        about = findViewById(R.id.about);
        Typeface aboutFont = Typeface.createFromAsset(getAssets(), "JosefinSans-Regular.ttf");
        about.setTypeface(aboutFont);


        exitToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent logout = new Intent(ProfileSettings.this, LoginActivity.class);
                logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(logout);
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent backToNotes = new Intent(
                getApplicationContext(),
                MyNotes.class);
        startActivity(backToNotes);
        finish();
        return;
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        Intent backToNotes = new Intent(ProfileSettings.this, MyNotes.class);
        startActivity(backToNotes);
        return true;
    }
}
