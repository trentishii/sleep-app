package com.example.trent.sleepapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trent.sleepapp.database.UserContract.UserEntry;
import com.example.trent.sleepapp.database.UserDBHelper;

public class RegisterActivity extends AppCompatActivity {
    private EditText etUsername;
    private EditText etName;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etAge;
    private Button bRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etName = (EditText) findViewById(R.id.etName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etAge = (EditText) findViewById(R.id.etAge);
        bRegister = (Button) findViewById(R.id.bRegister);

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertUser();
                Intent registeredIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                RegisterActivity.this.startActivity(registeredIntent);
            }
        });
    }

    private void insertUser() {
        String nameString = etName.getText().toString().trim();
        String userString = etUsername.getText().toString().trim();
        String emailString = etEmail.getText().toString().trim();
        String passString = etPassword.getText().toString().trim();
        String ageString = etAge.getText().toString().trim();
        int age = Integer.parseInt(ageString);

        // Create Database Helper
        UserDBHelper helper = new UserDBHelper(this);

        // Create the database in write mode
        SQLiteDatabase db = helper.getWritableDatabase();

        // create a contentvalues object to store value pairs in
        ContentValues values = new ContentValues();
        values.put(UserEntry.COLUMN_NAME, nameString);
        values.put(UserEntry.COLUMN_USERNAME, userString);
        values.put(UserEntry.COLUMN_EMAIL, emailString);
        values.put(UserEntry.COLUMN_PASSWORD, passString);
        values.put(UserEntry.COLUMN_AGE, age);

        long newRowId = db.insert(UserEntry.TABLE_NAME, null, values);
        if (newRowId == -1) {
            Toast.makeText(this, "Error registering user", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Pet saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
        }
    }
}
