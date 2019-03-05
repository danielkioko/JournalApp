package com.danielkioko.peachnotes;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class BackupActivity extends AppCompatActivity {

//    private static int GOOGLE_SIGNIN_BACKUP_REQUEST_CODE = 1;
//    private static int GOOGLE_SIGNIN_RESTORE_REQUEST_CODE = 2;
//    private GoogleSignInClient googleSignInClient;
//    private DriveClient driveClient = null;
//    private DriveResourceClient driveResourceClient = null;
//
//    private static final String TAG = "CreateFolderInFolder";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);


        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importDB();
            }
        });

        Button btn2 = findViewById(R.id.button2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportDB();
            }
        });

    }

    private void importDB() {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            if (sd.canWrite()) {
                String currentDBPath = "/data/" + getPackageName()
                        + "/databases/" + "MyNotes.db";
                String backupDBPath = "NDbBackup"; // From SD directory.
                File backupDB = new File(data, currentDBPath);
                File currentDB = new File(sd, backupDBPath);

                FileChannel src = new FileInputStream(backupDB).getChannel();
                FileChannel dst = new FileOutputStream(currentDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Toast.makeText(getApplicationContext(), "Import Successful!",
                        Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {

            Toast.makeText(getApplicationContext(), "Import Failed!", Toast.LENGTH_SHORT)
                    .show();

        }
    }

    private void exportDB() {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "/data/" + getPackageName()
                        + "/databases/" + "MyNotes.db";
                String backupDBPath = "NDbBackup";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Toast.makeText(getApplicationContext(), "Backup Successful!",
                        Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {

            Toast.makeText(getApplicationContext(), "Backup Failed!" + e, Toast.LENGTH_SHORT)
                    .show();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if (requestCode == GOOGLE_SIGNIN_BACKUP_REQUEST_CODE || requestCode == GOOGLE_SIGNIN_RESTORE_REQUEST_CODE) {
//
//            if (resultCode == Activity.RESULT_OK) {
//
//                GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);
//                // driveClient = googleSignInAccount(Drive.getDriveResourceClient(this, googleSignInAccount);
//
//                if (requestCode == GOOGLE_SIGNIN_BACKUP_REQUEST_CODE) {
//                    backupToGoogleDrive();
//                } else if (requestCode == GOOGLE_SIGNIN_RESTORE_REQUEST_CODE) {
//                    restoreFromGoogleDrive();
//                }
//
//            }
//
//        }

    }

//    private void restoreFromGoogleDrive() {
//    }
//
//    private void initBackup() {
//        googleSignInClient = buildGoogleSignInClient();
//        startActivityForResult(googleSignInClient.getSignInIntent(), GOOGLE_SIGNIN_BACKUP_REQUEST_CODE);
//    }
//
//    private void initRestore() {
//        googleSignInClient = buildGoogleSignInClient();
//        startActivityForResult(googleSignInClient.getSignInIntent(), GOOGLE_SIGNIN_RESTORE_REQUEST_CODE);
//    }
//
//    private void signOut() {
//        googleSignInClient.signOut();
//    }
//
//    private GoogleSignInClient buildGoogleSignInClient() {
//
//        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestScopes(Drive.SCOPE_APPFOLDER)
//                .build();
//        return GoogleSignIn.getClient(this, googleSignInOptions);
//    }
//
//    protected void showMessage(String message) {
//        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
//    }
//
//    @SuppressLint("StringFormatInvalid")
//    private void backupToGoogleDrive() {
//
//        final Task<DriveFolder> appFolderTask = driveResourceClient.getAppFolder();
//        final Task<DriveContents> createContentsTask = driveResourceClient.createContents();
//        Tasks.whenAll(appFolderTask, createContentsTask)
//                .continueWithTask(task -> {
//                    DriveFolder parent = appFolderTask.getResult();
//                    DriveContents contents = createContentsTask.getResult();
//                    OutputStream outputStream = contents.getOutputStream();
//                    try (Writer writer = new OutputStreamWriter(outputStream)) {
//                        writer.write("Hello World!");
//                    }
//
//                    MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
//                            .setTitle("New file")
//                            .setMimeType("text/plain")
//                            .setStarred(true)
//                            .build();
//
//                    return driveResourceClient.createFile(parent, changeSet, contents)
//                .addOnSuccessListener(this,
//                        driveFile -> {
//                            showMessage(getString(R.string.file_created,
//                                    driveFile.getDriveId().encodeToString());
//                            finish();
//                        })
//                .addOnFailureListener(this, new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.e(TAG, "Unable to create file", e);
//                        BackupActivity.this.showMessage(BackupActivity.this.getString(R.string.file_create_error));
//                        BackupActivity.this.finish();
//                    }
//                });
//
//                });
//
//        }
}
