package com.example.sagar.to_dolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.MutableDocument;

import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private final String TAG = MainActivity.class.getSimpleName();
    private final String ID = "id";
    private final String DESCRIPTION = "description";
    private final String PRIORITY = "priority";
    private final String DATE = "updatedAt";


    private TaskEntry taskEntries[] = {
            new TaskEntry("0", 1, "Java", new Date()),
            new TaskEntry("1", 2, "SQL", new Date()),
            new TaskEntry("2", 3, "Android", new Date()),
            new TaskEntry("3", 1, "Cuchbase", new Date())
    };

    private ListView mListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });

        mListView = findViewById(R.id.taskList);
        RelativeLayout emptyView = findViewById(R.id.empty_view);
        mListView.setEmptyView(emptyView);
        mListView.setOnItemClickListener(this);


        final Database database = AppDatabase.getInstance(getApplicationContext());

        showTasks(database);
        /*createList(database);
        outputContents(database);*/
        //
    }


    private void showTasks(Database database) {
        ArrayList<TaskEntry> taskList = new ArrayList<>();
        int count = (int) database.getCount();
        Log.i(TAG, "count " + count);

        for (int i = 0; i < count; i++) {
            Document document = database.
                    getDocument(String.valueOf(i));

            if (document != null) {

                String id = document.getString(ID);
                String desc = document.getString(DESCRIPTION);
                int priority = document.getInt(PRIORITY);
                Date updatedAt = document.getDate(DATE);

                taskList.add(new TaskEntry(id, priority, desc, updatedAt));
            }
        }

        ListAdapter listAdapter = new ListAdapter(this, taskList);
        mListView.setAdapter(listAdapter);
    }


    private void createList(Database database) {

        for (TaskEntry taskEntry : taskEntries) {

            MutableDocument taskDocument = new MutableDocument(
                    taskEntry.getId());

            taskDocument.setString(ID, taskEntry.getId());
            taskDocument.setString(DESCRIPTION, taskEntry.getDescription());
            taskDocument.setInt(PRIORITY, taskEntry.getPriority());
            taskDocument.setDate(DATE, taskEntry.getUpdatedAt());

            try {
                database.save(taskDocument);
            } catch (CouchbaseLiteException e) {
                Log.e(TAG, "Save Doc : " + e.toString());
            }
        }
    }


    private void outputContents(Database database) {
        ArrayList<TaskEntry> tasksFromDatabase = new ArrayList<>();

        for (TaskEntry taskEntry : taskEntries) {

            Document document = database.getDocument(taskEntry.getId());

            if (document != null) {

                String id = document.getString(ID);
                String desc = document.getString(DESCRIPTION);
                int priority = document.getInt(PRIORITY);
                Date updatedAt = document.getDate(DATE);

                tasksFromDatabase.add(new TaskEntry(id, priority, desc, updatedAt));
            } else {
                Log.i(TAG, "Document is null");
            }
        }

        ListAdapter listAdapter = new ListAdapter(this,
                tasksFromDatabase);
        mListView.setAdapter(listAdapter);


        for (TaskEntry taskEntry : tasksFromDatabase) {
            Log.i(TAG, taskEntry.toString());
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(MainActivity.this, EditorActivity.class);
        intent.putExtra(ID, String.valueOf(taskEntries[position].getId()));
        startActivity(intent);
    }


    // END
}
