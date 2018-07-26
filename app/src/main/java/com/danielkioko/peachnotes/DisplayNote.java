package com.danielkioko.peachnotes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DisplayNote extends AppCompatActivity {

    private NDb mydb;
    EditText name;
    EditText content;
    private CoordinatorLayout coordinatorLayout;
    String dateString;
    Bundle extras;
    int id_To_Update = 0;
    Snackbar snackbar;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.viewnotepad);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        name = findViewById(R.id.txtname);
        Typeface typefaceTitle = Typeface.createFromAsset(getAssets(), "JosefinSans-Bold.ttf");
        name.setTypeface(typefaceTitle);
        content = findViewById(R.id.txtcontent);
        Typeface typefaceContent = Typeface.createFromAsset(getAssets(), "JosefinSans-Regular.ttf");
        content.setTypeface(typefaceContent);

        coordinatorLayout = findViewById(R.id
                .coordinatorLayout);
        mydb = new NDb(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int Value = extras.getInt("id");
            if (Value > 0) {
                snackbar = Snackbar
                        .make(coordinatorLayout, "Note Id : " + String.valueOf(Value), Snackbar.LENGTH_LONG);
                snackbar.show();
                Cursor rs = mydb.getData(Value);
                id_To_Update = Value;
                rs.moveToFirst();
                String nam = rs.getString(rs.getColumnIndex(NDb.name));
                String contents = rs.getString(rs.getColumnIndex(NDb.remark));
                if (!rs.isClosed()) {
                    rs.close();
                }
                name.setText(nam);
                content.setText(contents);
            }
        }
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
                                            mydb.deleteNotes(id_To_Update);
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
                    Calendar c = Calendar.getInstance();
                    System.out.println("Current time => " + c.getTime());
                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                    String formattedDate = df.format(c.getTime());
                    dateString = formattedDate;
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
                                    snackbar = Snackbar
                                            .make(coordinatorLayout, "Your note Updated Successfully!!!", Snackbar.LENGTH_LONG);
                                    snackbar.show();

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
