package com.danielkioko.peachnotes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DisplayNote extends AppCompatActivity {

    private NDb mydb;
    EditText name;
    EditText content;

    Button saveNote;

    private CoordinatorLayout coordinatorLayout;
    String dateString;
    Bundle extras;

    int id_To_Update = 0;

    Snackbar snackbar;
    Toolbar toolbar;

    private FirebaseDatabase database;
    private StorageReference storage;
    private DatabaseReference databaseRef;
    private DatabaseReference mDatabaseUsers;
    private FirebaseUser mCurrentUser;
    private FirebaseAuth mAuth;

    private Uri uri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.viewnotepad);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        storage = FirebaseStorage.getInstance().getReference();
        databaseRef = FirebaseDatabase.getInstance().getReference().child("Notes");
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());

//        String noteID = getIntent().getExtras().getString("noteID");

        name = findViewById(R.id.txtname);
        Typeface typefaceTitle = Typeface.createFromAsset(getAssets(), "JosefinSans-Bold.ttf");
        name.setTypeface(typefaceTitle);
        content = findViewById(R.id.txtcontent);
        Typeface typefaceContent = Typeface.createFromAsset(getAssets(), "JosefinSans-Regular.ttf");
        content.setTypeface(typefaceContent);

        coordinatorLayout = findViewById(R.id
                .coordinatorLayout);

        saveNote = findViewById(R.id.btnSave);
        saveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String title = name.getText().toString().trim();
                final String notes = content.getText().toString().trim();

                Calendar calendar = Calendar.getInstance();
                System.out.println("Current time => " + calendar.getTime());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                String date = simpleDateFormat.format(calendar.getTime());
                dateString = date;

                databaseRef = FirebaseDatabase.getInstance().getReference();
                Noter noter = new Noter(title, notes, date);
                databaseRef.child("note_data").setValue(noter).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(DisplayNote.this, MyNotes.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_LONG).show();
                        }
                    }
                });

//                final StorageReference storageReference = storage.child("note_data").child(uri.getLastPathSegment());
//                storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                            @Override
//                            public void onSuccess(Uri uri) {
//
//                                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
//                                final DatabaseReference newNote = databaseRef.push();
//
//                                mDatabaseUsers.addValueEventListener(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                        newNote.child("title").setValue(title);
//                                        newNote.child("notes").setValue(notes);
//                                        newNote.child("time").setValue(dataSnapshot.child("time").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//                                                if (task.isSuccessful()){
//                                                    Intent intent = new Intent(DisplayNote.this, MyNotes.class);
//                                                    startActivity(intent);
//                                                } else {
//                                                }
//                                            }
//                                        });
//                                    }
//
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                                        Toast.makeText(getApplicationContext(), "Error! Locally Saved Instead", Toast.LENGTH_LONG).show();
//                                    }
//                                });
//                            }
//                        });
//                    }
//                });
//
            }
        });

        databaseRef.child("notes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String noteTitle = (String) dataSnapshot.child("title").getValue();
                String noteContent = (String) dataSnapshot.child("notes").getValue();

                if (noteTitle == null && noteContent == null) {
                    name.setText("");
                    content.setText("");
                } else {
                    name.setText(noteTitle);
                    content.setText(noteContent);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Oops! Something Happened, Close the app and try again", Toast.LENGTH_LONG).show();
            }
        });

    }
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                int Value = extras.getInt("id");
                getMenuInflater().inflate(R.menu.display_menu, menu);
            }
            return true;
        }

    public boolean onOptionsItemSelected(MenuItem item) {
            super.onOptionsItemSelected(item);

        switch (item.getItemId()) {

            case R.id.Delete:
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Delete Note")
                            .setPositiveButton("YES",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int id) {

                                            String noteID = getIntent().getExtras().getString("noteID");
                                            databaseRef.child(noteID).removeValue();

                                            Toast.makeText(DisplayNote.this, "Deleted Successfully",Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(
                                                    getApplicationContext(),
                                                    MyNotes.class);
                                            startActivity(intent);
                                            finish();

                                        }
                                    })
                            .setNegativeButton("NO",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int id) {
                                        }
                                    });
                    AlertDialog d = builder.create();
                    d.setTitle("Are you sure");
                    d.show();
                    return true;

                case R.id.Save:
                    Bundle extras = getIntent().getExtras();

                    if (extras != null) {
                        int Value = extras.getInt("id");
                        if (Value > 0) {
                            if (content.getText().toString().trim().equals("")
                                    || name.getText().toString().trim().equals("")) {

                                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(coordinatorLayout.getWindowToken(), 0);

                                snackbar = Snackbar
                                        .make(coordinatorLayout, "Write Something, Anything In Mind?", Snackbar.LENGTH_LONG);
                                snackbar.show();

                            } else {
                                if (mydb.updateNotes(id_To_Update, name.getText()
                                        .toString(), dateString, content.getText()
                                        .toString())) {

                                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(coordinatorLayout.getWindowToken(), 0);


                                } else {
                                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(coordinatorLayout.getWindowToken(), 0);
                                    snackbar = Snackbar
                                            .make(coordinatorLayout, "There's an error. That's all I can tell. Sorry!", Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }
                            }
                        } else {
                            if (content.getText().toString().trim().equals("")
                                    || name.getText().toString().trim().equals("")) {
                                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(coordinatorLayout.getWindowToken(), 0);
                                snackbar = Snackbar
                                        .make(coordinatorLayout, "You Forgot To Finish Writing 😕", Snackbar.LENGTH_LONG);
                                snackbar.show();
                            } else {
                                if (mydb.insertNotes(name.getText().toString(), dateString,
                                        content.getText().toString())) {

                                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(coordinatorLayout.getWindowToken(), 0);

                                    Intent backToMyNotes = new Intent(DisplayNote.this, MyNotes.class);

                                    startActivity(backToMyNotes);
                                    Toast.makeText(DisplayNote.this, "Added Successfully", Toast.LENGTH_LONG).show();
                                } else {
                                    snackbar = Snackbar
                                            .make(coordinatorLayout, "Unfortunately Task Failed.", Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }
                            }
                        }
                    }
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        toDisplayNotes();
        return true;
    }

        public void toDisplayNotes(){
            Intent toAllNotes = new Intent(DisplayNote.this, MyNotes.class);
            startActivity(toAllNotes);
        }

        @Override
        public void onBackPressed() {
            Intent intent = new Intent(
                    getApplicationContext(),
                    MyNotes.class);
            startActivity(intent);
            finish();
            return;
        }
}
