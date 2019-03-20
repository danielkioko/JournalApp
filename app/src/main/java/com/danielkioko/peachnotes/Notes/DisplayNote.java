package com.danielkioko.peachnotes.Notes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.danielkioko.peachnotes.DB.NDb;
import com.danielkioko.peachnotes.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DisplayNote extends AppCompatActivity {

    private NDb mydb;
    EditText name;
    TextInputEditText content;
    Button done;
    FloatingActionButton delete, send;
    ImageView imageView;
    String dateString;
    int id_To_Update = 0;

    int PICK_IMAGE = 1;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int value = extras.getInt("id");
            getMenuInflater().inflate(R.menu.display_menu, menu);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
//            case R.id.Delete:
//
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        quickSave();
        saveIncomplete();
        return;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpWindowTransitions();
        setContentView(R.layout.viewnotepad);

        name = findViewById(R.id.txtname);
        content = findViewById(R.id.txtcontent);

        done = findViewById(R.id.btn_done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });

        mydb = new NDb(this);

        send = findViewById(R.id.fab_sendNote);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shareBody = content.getText().toString().trim();
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, name.getText().toString());
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_using)));
            }
        });

        delete = findViewById(R.id.fab_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(DisplayNote.this);
                builder.setMessage("Delete Note?")
                        .setPositiveButton("YES",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        mydb.deleteNotes(id_To_Update);
                                        Toast.makeText(DisplayNote.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(
                                                getApplicationContext(),
                                                HomeActivity.class);
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
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int Value = extras.getInt("id");
            if (Value > 0) {
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


    private void setUpWindowTransitions() {
        Fade fade = new Fade();
        fade.setDuration(3000);
        this.getWindow().setEnterTransition(fade);

        Slide slide = new Slide();
        slide.setDuration(3000);
        this.getWindow().setReturnTransition(slide);
    }

    public void saveIncomplete() {

        Bundle bundle = getIntent().getExtras();
        Calendar calendar = Calendar.getInstance();
        System.out.println("Current time => " + calendar.getTime());

        SimpleDateFormat df = new SimpleDateFormat("MMM, dd");
        String formattedDate = df.format(calendar.getTime());
        dateString = formattedDate;

        String untitled = "Untitled: " + dateString;

        if (bundle != null) {

            if (content.getText() != null && name.getText() == null) {
                mydb.insertNotes(untitled, dateString,
                        content.getText().toString());
            }

        }

    }

    private void saveNote() {
        Bundle extras = getIntent().getExtras();
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("MMM, dd");
        String formattedDate = df.format(c.getTime());
        dateString = formattedDate;

        if (extras != null) {
            int Value = extras.getInt("id");
            if (Value > 0) {
                if (content.getText().toString().trim().equals("")
                        || name.getText().toString().trim().equals("")) {

                    name.setText("untitled");
                    saveNote();
                    //Toast.makeText(this, "Please fill in name of the note", Toast.LENGTH_LONG).show();
                } else {
                    if (mydb.updateNotes(id_To_Update, name.getText()
                            .toString(), dateString, content.getText()
                            .toString())) {
                        Intent intent = new Intent(
                                getApplicationContext(),
                                HomeActivity.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(this, "Your note Updated Successfully!!!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "There's an error. That's all I can tell. Sorry!", Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                if (content.getText().toString().trim().equals("")
                        || name.getText().toString().trim().equals("")) {
                    name.setText("untitled");
                    saveNote();
                    //Toast.makeText(this, "Please fill in name of the note", Toast.LENGTH_LONG).show();
                } else {
                    if (mydb.insertNotes(name.getText().toString(), dateString,
                            content.getText().toString())) {
                        Intent intent = new Intent(
                                getApplicationContext(),
                                HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                        Toast.makeText(this, "Added Successfully.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Unfortunately Task Failed.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

    private void quickSave() {
        Bundle extras = getIntent().getExtras();
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("MMM, dd");
        String formattedDate = df.format(c.getTime());
        dateString = formattedDate;

        if (extras != null) {
            int Value = extras.getInt("id");
            if (Value > 0) {
                if (content.getText().toString().trim().equals("")
                        || name.getText().toString().trim().equals("")) {

                    mydb.deleteNotes(id_To_Update);
                    Intent intent = new Intent(
                            getApplicationContext(),
                            HomeActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    if (mydb.updateNotes(id_To_Update, name.getText()
                            .toString(), dateString, content.getText()
                            .toString())) {
                        Intent intent = new Intent(
                                getApplicationContext(),
                                HomeActivity.class);
                        startActivity(intent);
                        finish();
                        //Toast.makeText(this, "Your note Updated Successfully!!!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "There's an error. That's all I can tell. Sorry!", Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                if (content.getText().toString().trim().equals("")
                        || name.getText().toString().trim().equals("")) {

                    mydb.deleteNotes(id_To_Update);
                    Intent intent = new Intent(
                            getApplicationContext(),
                            HomeActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    if (mydb.insertNotes(name.getText().toString(), dateString,
                            content.getText().toString())) {
                        Intent intent = new Intent(
                                getApplicationContext(),
                                HomeActivity.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(this, "Added Successfully.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Unfortunately Task Failed.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }
}
