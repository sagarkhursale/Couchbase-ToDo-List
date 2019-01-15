package com.example.sagar.to_dolist;

import android.content.Context;
import android.util.Log;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.DatabaseConfiguration;


class AppDatabase {
    private static final String TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE = "to-do-list";
    private static Database database;


    static Database getInstance(Context context) {
        if (database == null) {
            synchronized (LOCK) {
                try {
                    // Helper Object
                    DatabaseConfiguration configuration = new DatabaseConfiguration(context);
                    database = new Database(DATABASE, configuration);

                } catch (CouchbaseLiteException e) {
                    Log.i(TAG, "AppDatabase : " + e.toString());
                }
            }
        }
        return database;
    }

    // END
}
