package com.example.sagar.to_dolist;

import android.content.Context;
import android.util.Log;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.DatabaseConfiguration;



public class AppDatabase {
    private static final String TAG = AppDatabase.class.getSimpleName();
    private static final AppDatabase ourInstance = new AppDatabase();
    private static final Object LOCK = new Object();
    private static final String DATABASE = "to-do-list";


    public AppDatabase getInstance() {
        return ourInstance;
    }

    private Database getDatabase(Context context) {
        Database database = null;

        if (ourInstance != null) {
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
