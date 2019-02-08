package com.danielkioko.peachnotes.Accounts;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.danielkioko.peachnotes.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    private EditText mNameSignUpEditText;
    private EditText mEmailSignUpEditText;
    private EditText mPasswordEditText;

    private TextView welcome;

    private Button mSignUpButton;
    private Button mGoToLoginButton;

    private FirebaseAuth mAuth;
    private DatabaseReference mUsersDBref;
    private ProgressDialog mDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //assign the views
        mNameSignUpEditText = findViewById(R.id.etName);
        mEmailSignUpEditText = findViewById(R.id.etRegEmail);
        mPasswordEditText = findViewById(R.id.etRegPassword);
        mSignUpButton = findViewById(R.id.btnRegister);
        mGoToLoginButton = findViewById(R.id.btnToLogin);

        welcome = findViewById(R.id.tv_welcome);
        Typeface typefacePeach = Typeface.createFromAsset(getAssets(), "Pacifico.ttf");
        welcome.setTypeface(typefacePeach);

        //firebase assign
        mAuth = FirebaseAuth.getInstance();
        mDialog = new ProgressDialog(this);

        /**listen to sign up button click**/
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mNameSignUpEditText.getText().toString();
                String email = mEmailSignUpEditText.getText().toString().trim();
                String password = mPasswordEditText.getText().toString().trim();

                if(name.isEmpty()){
                    Toast.makeText(Register.this, "Name cannot be empty!", Toast.LENGTH_SHORT).show();
                }else if(email.isEmpty()){
                    Toast.makeText(Register.this, "Email cannot be empty!", Toast.LENGTH_SHORT).show();
                }else if(password.isEmpty()){
                    Toast.makeText(Register.this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
                }else{
                    mNameSignUpEditText.setVisibility(View.INVISIBLE);
                    mEmailSignUpEditText.setVisibility(View.INVISIBLE);
                    mNameSignUpEditText.setVisibility(View.INVISIBLE);
                    mSignUpButton.setEnabled(false);
                    signUpUserWithFirebase(name, email, password);
                }
            }
        });

        /**listen to go to login button**/
        mGoToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLoginActivity();
            }
        });

    }

    private void signUpUserWithFirebase(final String name, String email, String password){
        mDialog.setMessage("Please wait...");
        mDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    //there was an error
                    Toast.makeText(Register.this, "Error " + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }else{
                    final FirebaseUser newUser = task.getResult().getUser();
                    //success creating user, now set display name as name
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(name)
                            .build();

                    newUser.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    mDialog.dismiss();
                                    if (task.isSuccessful()) {
                                        Log.d(Register.class.getName(), "User profile updated.");
                                        /***CREATE USER IN FIREBASE DB AND REDIRECT ON SUCCESS**/
                                        createUserInDb(newUser.getUid(), newUser.getDisplayName(), newUser.getEmail());

                                    }else{
                                        //error
                                        Toast.makeText(Register.this, "Error " + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
            }
        });
    }

    private void createUserInDb(String userId, String displayName, String email) {
        mUsersDBref = FirebaseDatabase.getInstance().getReference().child("Users");
        User user = new User(userId, displayName, email);
        mUsersDBref.child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(!task.isSuccessful()){
                    //error
                    Toast.makeText(Register.this, "Error " + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }else{
                    //success adding user to db as well
                    //go to users chat list
                    goToInstructions();
                }
            }
        });
    }

    private void goToInstructions() {

    }

    private void goToLoginActivity(){
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        Intent intent = new Intent(
                getApplicationContext(),
                LoginActivity.class);
        startActivity(intent);
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(
                getApplicationContext(),
                LoginActivity.class);
        startActivity(intent);
        finish();
        return;
    }

}
