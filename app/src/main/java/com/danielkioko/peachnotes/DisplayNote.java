package com.danielkioko.peachnotes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.danielkioko.peachnotes.DB.NDb;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DisplayNote extends AppCompatActivity {

    private NDb mydb;
    EditText name;
    EditText content;
    String dateString;
    Bundle extras;
    int id_To_Update = 0;
    Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewnotepad);

        name = findViewById(R.id.txtname);
        content = findViewById(R.id.txtcontent);

        mydb = new NDb(this);

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
            case R.id.Delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Delete Note?")
                        .setPositiveButton("YES",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        mydb.deleteNotes(id_To_Update);
                                        Toast.makeText(DisplayNote.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        quickSave();
        Intent intent = new Intent(
                getApplicationContext(),
                HomeActivity.class);
        startActivity(intent);
        finish();
        return;
    }

    private void quickSave() {
        Bundle extras = getIntent().getExtras();
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("MMM/dd/yyyy");
        String formattedDate = df.format(c.getTime());
        dateString = formattedDate;
        if (extras != null) {
            int Value = extras.getInt("id");
            if (Value > 0) {
                if (content.getText().toString().trim().equals("")
                        || name.getText().toString().trim().equals("")) {
                    Toast.makeText(this, "Please fill in name of the note", Toast.LENGTH_LONG).show();
                } else {
                    if (mydb.updateNotes(id_To_Update, name.getText()
                            .toString(), dateString, content.getText()
                            .toString())) {
                        Toast.makeText(this, "Your note Updated Successfully!!!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "There's an error. That's all I can tell. Sorry!", Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                if (content.getText().toString().trim().equals("")
                        || name.getText().toString().trim().equals("")) {
                    Toast.makeText(this, "Please fill in name of the note", Toast.LENGTH_LONG).show();
                } else {
                    if (mydb.insertNotes(name.getText().toString(), dateString,
                            content.getText().toString())) {
                        Toast.makeText(this, "Added Successfully.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Unfortunately Task Failed.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        quickSave();
    }

    @Override
    protected void onStop() {
        super.onStop();
        quickSave();
    }

}
