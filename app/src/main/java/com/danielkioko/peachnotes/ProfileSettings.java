package com.danielkioko.peachnotes;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.danielkioko.peachnotes.Accounts.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        progressBar = findViewById(R.id.progressBar);

        accountSettingsText = findViewById(R.id.accountSettingsText);

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

        progressBar.setVisibility(View.VISIBLE);

        final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Users");

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        myRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("displayName").getValue().toString();
                String email = dataSnapshot.child("email").getValue().toString();

                userName.setText(name);
                userEmail.setText(email);

                progressBar.setVisibility(View.INVISIBLE);
                accountSettingsText.setVisibility(View.VISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
