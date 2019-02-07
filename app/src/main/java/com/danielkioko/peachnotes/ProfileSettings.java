package com.danielkioko.peachnotes;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.danielkioko.peachnotes.Accounts.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileSettings extends AppCompatActivity {

    TextView peachNotesTitle, userName, userEmail, accountSettingsText;
    Button exitToLogin;

    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        exitToLogin = findViewById(R.id.btnLogout);
        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.userEmail);

        firebaseAuth = FirebaseAuth.getInstance();

        peachNotesTitle = findViewById(R.id.peachNotesTitle);
        Typeface titleFont = Typeface.createFromAsset(getAssets(), "Pacifico.ttf");
        peachNotesTitle.setTypeface(titleFont);

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
    protected void onStart() {
        super.onStart();

        String uName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        userName.setText(uName);
        userEmail.setText(email);
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
