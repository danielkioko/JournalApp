package com.danielkioko.peachnotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class BackupActivity extends AppCompatActivity {

    SharedPref sharedPref;
    Backup backup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);



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
