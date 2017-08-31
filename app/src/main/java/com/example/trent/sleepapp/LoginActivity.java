package com.example.trent.sleepapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.widget.Toast;

import com.example.trent.sleepapp.database.UserContract.UserEntry;
import com.example.trent.sleepapp.database.UserDBHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashSet;
import java.util.Set;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mStatusTextView;
    private TextView mDetailTextView;
    private EditText mEmailField;
    private EditText mPasswordField;

    private UserDBHelper mDbHelper;
    SharedPreferences sharedPrefs;
    SharedPreferences buttonPrefs;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "EmailPassword";

    public static final String PREFNAME = "userPrefs";
    public static final String BUTTONPREFNAME = "btnPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mStatusTextView = (TextView) findViewById(R.id.status);
        mDetailTextView = (TextView) findViewById(R.id.detail);
        mEmailField = (EditText) findViewById(R.id.field_email);
        mPasswordField = (EditText) findViewById(R.id.field_password);

        // Buttons
        findViewById(R.id.email_sign_in_button).setOnClickListener(this);
        findViewById(R.id.email_create_account_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
        findViewById(R.id.verify_email_button).setOnClickListener(this);

        sharedPrefs = getSharedPreferences(PREFNAME, Context.MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
//                    if (!user.isEmailVerified()) {
//                        sendEmailVerification();
//                    }
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    Toast.makeText(LoginActivity.this, "User Signed In", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "TOKEN: " + FirebaseInstanceId.getInstance().getToken());
                    Intent registerIntent = new Intent(LoginActivity.this, UserActivity.class);
                    registerIntent.putExtra("isEnabled", true);
                    LoginActivity.this.startActivity(registerIntent);
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
                updateUI(user);
            }
        };


//        bLogin.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                boolean check = loginAttempt();
//                if (check) {
//                    SharedPreferences.Editor editor = sharedPrefs.edit();
//                    editor.putString("username", etEmail.getText().toString().trim());
//                    editor.commit();
//                    Intent loggedIn = new Intent(LoginActivity.this, UserActivity.class);
//                    LoginActivity.this.startActivity(loggedIn);
//                }
//            }
//        });
//
//        register.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
//                LoginActivity.this.startActivity(registerIntent);
//            }
//        });
//        mDbHelper = new UserDBHelper(this);
//        displayDatabaseInfo();
    }

//    private boolean loginAttempt() {
//        SQLiteDatabase logindb = mDbHelper.getReadableDatabase();
//        String[] projection = {UserEntry.COLUMN_USERNAME, UserEntry.COLUMN_PASSWORD};
//        String userString = etEmail.getText().toString().trim();
//        String passString = etPassword.getText().toString().trim();
//        String selection = UserEntry.COLUMN_USERNAME + " = ?";
//        String[] selectionArgs = { userString };
//
//        Cursor c = logindb.query(UserEntry.TABLE_NAME,
//                projection,
//                selection,
//                selectionArgs,
//                null,
//                null,
//                null);
//
//        if (c.getCount() >= 1) {
//            int passIndex = c.getColumnIndex(UserEntry.COLUMN_PASSWORD);
//            while (c.moveToNext()) {
//                String password = c.getString(passIndex);
//                if (password.equals(passString)) {
//                    return true;
//                }
//            }
//        }
//        Toast.makeText(this, "Password and Username do not match", Toast.LENGTH_SHORT).show();
//        return false;
//    }

    @Override
    protected void onStart() {
        super.onStart();
        //displayDatabaseInfo();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

//    private void displayDatabaseInfo() {
//        // To access our database, we instantiate our subclass of SQLiteOpenHelper
//        // and pass the context, which is the current activity.
//        UserDBHelper mDbHelper = new UserDBHelper(this);
//
//        // Create and/or open a database to read from it
//        SQLiteDatabase db = mDbHelper.getReadableDatabase();
//
//        // Perform this raw SQL query "SELECT * FROM pets"
//        // to get a Cursor that contains all rows from the pets table.
//        Cursor cursor = db.rawQuery("SELECT * FROM " + UserEntry.TABLE_NAME, null);
//        try {
//            // Display the number of rows in the Cursor (which reflects the number of rows in the
//            // pets table in the database).
//            TextView displayView = (TextView) findViewById(R.id.dbStatus);
//            displayView.setText("Number of rows in user database table: " + cursor.getCount());
//        } finally {
//            // Always close the cursor when you're done reading from it. This releases all its
//            // resources and makes it invalid.
//            cursor.close();
//        }
//    }

    private void createAccount(String email, String password) {
        buttonPrefs = getSharedPreferences(BUTTONPREFNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = buttonPrefs.edit();
        editor.putBoolean("bPAM", true);
        editor.putBoolean("b2PAM", true);
        editor.putBoolean("b3PAM", true);
        editor.putBoolean("b4PAM", true);
        editor.putBoolean("bSSS", true);
        editor.putBoolean("b2SSS", true);
        editor.putBoolean("b3SSS", true);
        editor.putBoolean("b4SSS", true);
        editor.putBoolean("bPVT", true);
        editor.putBoolean("b2PVT", true);
        editor.putBoolean("b3PVT", true);
        editor.putBoolean("b4PVT", true);
        editor.putBoolean("bSleepLog", true);
        editor.putBoolean("bLEEDS", true);
        editor.putBoolean("bPANAS", true);
        editor.putBoolean("bJournal", true);
        editor.putBoolean("DayTime1Done", false);
        editor.putBoolean("DayTime2Done", false);
        editor.putBoolean("WakeTimeDone", false);
        editor.putBoolean("SleepTimeDone", false);
        editor.putBoolean("PAMDone", false);
        editor.putBoolean("PAM2Done", false);
        editor.putBoolean("PAM3Done", false);
        editor.putBoolean("PAM4Done", false);
        editor.putBoolean("SSSDone", false);
        editor.putBoolean("SSS2Done", false);
        editor.putBoolean("SSS3Done", false);
        editor.putBoolean("SSS4Done", false);
        editor.putBoolean("PVTDone", false);
        editor.putBoolean("PVT2Done", false);
        editor.putBoolean("PVT3Done", false);
        editor.putBoolean("PVT4Done", false);
        editor.putBoolean("JournalDone", false);
        editor.putBoolean("SleepLogDone", false);
        editor.putBoolean("PANASDone", false);
        editor.putBoolean("LEEDSDone", false);
        editor.commit();
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

//        showProgressDialog();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "sign in failed",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // [START_EXCLUDE]
//                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }

    private void signIn(final String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

//        showProgressDialog();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(LoginActivity.this, "sign in failed",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            mStatusTextView.setText(R.string.auth_failed);
                        } else {
                            SharedPreferences.Editor editor = sharedPrefs.edit();
                            editor.putString("email", email.trim());
                            Set<String> journals = new HashSet<String>();
                            journals.add("Tobacco");
                            journals.add("Coffee");
                            journals.add("Food");
                            journals.add("Exercise");
                            journals.add("Alcohol");
                            journals.add("Medicine");
                            editor.putStringSet("journals", journals);
                            editor.commit();
                            Intent registerIntent = new Intent(LoginActivity.this, UserActivity.class);
                            LoginActivity.this.startActivity(registerIntent);
                        }

//                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });

        // [END sign_in_with_email]
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);
    }

    private void sendEmailVerification() {
        // Disable button
        findViewById(R.id.verify_email_button).setEnabled(false);

        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        // Re-enable button
//                        findViewById(R.id.verify_email_button).setEnabled(true);

                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(LoginActivity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END send_email_verification]
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

    private void updateUI(FirebaseUser user) {
//        hideProgressDialog();
        if (user != null) {
            mStatusTextView.setText(getString(R.string.emailpassword_status_fmt,
                    user.getEmail(), user.isEmailVerified()));
            mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));

            findViewById(R.id.email_password_buttons).setVisibility(View.GONE);
            findViewById(R.id.email_password_fields).setVisibility(View.GONE);
            findViewById(R.id.signed_in_buttons).setVisibility(View.VISIBLE);

            findViewById(R.id.verify_email_button).setEnabled(!user.isEmailVerified());
        } else {
            mStatusTextView.setText(R.string.signed_out);
            mDetailTextView.setText(null);

            findViewById(R.id.email_password_buttons).setVisibility(View.VISIBLE);
            findViewById(R.id.email_password_fields).setVisibility(View.VISIBLE);
            findViewById(R.id.signed_in_buttons).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.email_create_account_button) {
            createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
        } else if (i == R.id.email_sign_in_button) {
            signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
        } else if (i == R.id.sign_out_button) {
            signOut();
        } else if (i == R.id.verify_email_button) {
            sendEmailVerification();
        }
    }
}
