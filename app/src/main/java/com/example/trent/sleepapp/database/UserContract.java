package com.example.trent.sleepapp.database;

import android.provider.BaseColumns;

/**
 * Created by Trent on 2/8/2017.
 */

public final class UserContract {
    private UserContract() {}

    public static final class UserEntry implements BaseColumns {
        public final static String TABLE_NAME = "users";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_NAME = "name";
        public final static String COLUMN_USERNAME = "username";
        public final static String COLUMN_EMAIL = "email";
        public final static String COLUMN_PASSWORD = "password";
        public final static String COLUMN_AGE = "age";
    }
}
