package com.danielkioko.peachnotes.Authentication;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.danielkioko.peachnotes.Notes.HomeActivity;
import com.danielkioko.peachnotes.R;
import com.danielkioko.peachnotes.SharedPref;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class AuthenticationActivity extends AppCompatActivity {

    private static final String KEY_NAME = "yourKey";
    TextView textView;
    private Cipher cipher;
    private KeyStore keyStore;
    private KeyGenerator keyGenerator;
    private FingerprintManager.CryptoObject cryptoObject;
    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;
    SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = new SharedPref(this);

        if (!sharedPref.loadLockState()) {
            startActivity(new Intent(AuthenticationActivity.this, HomeActivity.class));
        }

        setContentView(R.layout.activity_setup_authentication);
        textView = findViewById(R.id.textview4);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
            fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

            if (!fingerprintManager.isHardwareDetected()) {
                textView.setText("Your device doesn't support fingerprint authentication");
            }

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                textView.setText("Please enable the fingerprint permission");
            }

            if (!fingerprintManager.hasEnrolledFingerprints()) {
                textView.setText("No fingerprint configured. Please register at least one fingerprint in your device's Settings");
            }

            if (!keyguardManager.isKeyguardSecure()) {
                textView.setText("Please enable lockscreen security in your device's Settings");
            } else {

                try {
                    generateKey();
                } catch (FingerprintException e) {
                    e.printStackTrace();
                }

                if (initCipher()) {

                    cryptoObject = new FingerprintManager.CryptoObject(cipher);

                    FingerprintHandlerTwo helper = new FingerprintHandlerTwo(this);
                    helper.startAuth(fingerprintManager, cryptoObject);

                }

            }

        }

    }

    private void generateKey() throws FingerprintException {

        try {

            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            keyStore.load(null);
            keyGenerator.init(new

                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)

                    //Configure this key so that the user has to confirm their identity with a fingerprint each time they want to use it//
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());

            keyGenerator.generateKey();

        } catch (KeyStoreException
                | NoSuchAlgorithmException
                | NoSuchProviderException
                | InvalidAlgorithmParameterException
                | CertificateException
                | IOException exc) {
            exc.printStackTrace();
            throw new FingerprintException(exc);
        }

    }

    public boolean initCipher() {

        try {
            //Obtain a cipher instance and configure it with the properties required for fingerprint authentication//
            cipher = Cipher.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES + "/"
                            + KeyProperties.BLOCK_MODE_CBC + "/"
                            + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException |
                NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            //Return true if the cipher has been initialized successfully//
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {

            //Return false if cipher initialization failed//
            return false;
        } catch (KeyStoreException | CertificateException
                | UnrecoverableKeyException | IOException
                | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }

    }

    public void authenticationComplete() {
        Intent intent = new Intent(AuthenticationActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private class FingerprintException extends Exception {
        public FingerprintException(Exception e) {
            super(e);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public class FingerprintHandlerTwo extends FingerprintManager.AuthenticationCallback {

        android.app.AlertDialog alertDialog;

        // You should use the CancellationSignal method whenever your app can no longer process user input, for example when your app goes
        // into the background. If you don’t use this method, then other apps will be unable to access the touch sensor, including the lockscreen!//

        private CancellationSignal cancellationSignal;
        private Context context;

        public FingerprintHandlerTwo(Context mContext) {
            context = mContext;
        }

        //Implement the startAuth method, which is responsible for starting the fingerprint authentication process//

        public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {

            cancellationSignal = new CancellationSignal();
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
        }

        @Override
        //onAuthenticationError is called when a fatal error has occurred. It provides the error code and error message as its parameters//

        public void onAuthenticationError(int errMsgId, CharSequence errString) {

            //I’m going to display the results of fingerprint authentication as a series of toasts.
            //Here, I’m creating the message that’ll be displayed if an error occurs//

            Toast.makeText(context, "Authentication error\n" + errString, Toast.LENGTH_LONG).show();
        }

        @Override

        //onAuthenticationFailed is called when the fingerprint doesn’t match with any of the fingerprints registered on the device//

        public void onAuthenticationFailed() {
            Toast.makeText(context, "Authentication failed, Try Again", Toast.LENGTH_LONG).show();
        }

        @Override

        //onAuthenticationHelp is called when a non-fatal error has occurred. This method provides additional information about the error,
        //so to provide the user with as much feedback as possible I’m incorporating this information into my toast//
        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
            Toast.makeText(context, "Authentication help\n" + helpString, Toast.LENGTH_LONG).show();
        }

        @Override

        //onAuthenticationSucceeded is called when a fingerprint has been successfully matched to one of the fingerprints stored on the user’s device//
        public void onAuthenticationSucceeded(
                FingerprintManager.AuthenticationResult result) {
            //Toast.makeText(context, "", Toast.LENGTH_LONG).show();
            authenticationComplete();
        }

    }

}
