package com.example.trent.sleepapp.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.trent.sleepapp.database.UserContract.UserEntry;

/**
 * Created by Trent on 2/8/2017.
 */

public class UserDBHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Users.db";

    public UserDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_USERS_TABLE =  "CREATE TABLE " + UserEntry.TABLE_NAME + " ("
                + UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + UserEntry.COLUMN_NAME + " TEXT NOT NULL, "
                + UserEntry.COLUMN_USERNAME + " TEXT NOT NULL, "
                + UserEntry.COLUMN_EMAIL + " TEXT NOT NULL, "
                + UserEntry.COLUMN_PASSWORD + " TEXT NOT NULL, "
                + UserEntry.COLUMN_AGE + " INTEGER NOT NULL DEFAULT 0);";

        db.execSQL(SQL_CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
//        db.execSQL(SQL_DELETE_ENTRIES);
//        onCreate(db);
    }

}
