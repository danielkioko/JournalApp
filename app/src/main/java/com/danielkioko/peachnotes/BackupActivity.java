package com.danielkioko.peachnotes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveClient;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveResourceClient;
import com.google.android.gms.tasks.Task;

public class BackupActivity extends AppCompatActivity {

    private static int GOOGLE_SIGNIN_BACKUP_REQUEST_CODE = 1;
    private static int GOOGLE_SIGNIN_RESTORE_REQUEST_CODE = 2;
    private GoogleSignInClient googleSignInClient;
    private DriveClient driveClient = null;
    private DriveResourceClient driveResourceClient = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGNIN_BACKUP_REQUEST_CODE || requestCode == GOOGLE_SIGNIN_RESTORE_REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {

                GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);
                // driveClient = googleSignInAccount(Drive.getDriveResourceClient(this, googleSignInAccount);

                if (requestCode == GOOGLE_SIGNIN_BACKUP_REQUEST_CODE) {
                    backupToGoogleDrive();
                } else if (requestCode == GOOGLE_SIGNIN_RESTORE_REQUEST_CODE) {
                    restoreFromGoogleDrive();
                }

            }

        }

    }

    private void initBackup() {
        googleSignInClient = buildGoogleSignInClient();
        startActivityForResult(googleSignInClient.getSignInIntent(), GOOGLE_SIGNIN_BACKUP_REQUEST_CODE);
    }

    private void initRestore() {
        googleSignInClient = buildGoogleSignInClient();
        startActivityForResult(googleSignInClient.getSignInIntent(), GOOGLE_SIGNIN_RESTORE_REQUEST_CODE);
    }

    private void signOut() {
        googleSignInClient.signOut();
    }

    private GoogleSignInClient buildGoogleSignInClient() {

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(Drive.SCOPE_APPFOLDER)
                .build();
        return GoogleSignIn.getClient(this, googleSignInOptions);
    }

    private void backupToGoogleDrive() {

        Task<DriveFolder> appFolderTask = driveResourceClient.getAppFolder();

    }

    private void restoreFromGoogleDrive() {


    }

}
