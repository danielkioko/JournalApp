package com.danielkioko.peachnotes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.danielkioko.peachnotes.DB.NDb;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DisplayNote extends AppCompatActivity {

    private NDb mydb;
    EditText name;
    EditText content;
    Button button;
    FloatingActionButton add;
    ImageView imageView;
    String dateString;
    int id_To_Update = 0;

    int PICK_IMAGE = 1;

    public static byte[] imageViewToByte(ImageView image) {

        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        return byteArray;
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
//            case R.id.Delete:
//
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        saveNote();
        return;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewnotepad);

        name = findViewById(R.id.txtname);
        content = findViewById(R.id.txtcontent);
        imageView = findViewById(R.id.attached_images);

        add = findViewById(R.id.btn_attach);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE);

//                ActivityCompat.requestPermissions(
//                        DisplayNote.this,
//                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                        PICK_IMAGE
//                );

            }
        });

        mydb = new NDb(this);

        button = findViewById(R.id.btn_delete);
        button.setOnClickListener(new View.OnClickListener() {
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
                byte[] imageUri = rs.getBlob(rs.getColumnIndex(NDb.image));

                //String(rs.getColumnIndex(NDb.image));

                if (!rs.isClosed()) {
                    rs.close();
                }

                name.setText(nam);
                content.setText(contents);

                Bitmap bitmap = BitmapFactory.decodeByteArray(imageUri, 0, imageUri.length);
                imageView.setImageBitmap(bitmap);

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
                    Toast.makeText(this, "Please fill in name of the note", Toast.LENGTH_LONG).show();
                } else {
                    if (mydb.updateNotes(id_To_Update, name.getText()
                            .toString(), dateString, content.getText()
                            .toString(), imageViewToByte(imageView))) {
                        Intent intent = new Intent(
                                getApplicationContext(),
                                HomeActivity.class);
                        startActivity(intent);
                        finish();
                        //Toast.makeText(this, "Your note Updated Successfully!!!", Toast.LENGTH_LONG).show();
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
                            content.getText().toString(), imageViewToByte(imageView))) {
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
                            .toString(), imageViewToByte(imageView))) {
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
                            content.getText().toString(), imageViewToByte(imageView))) {
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PICK_IMAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE);
            } else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);

                imageView.setImageURI(uri);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
