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
    private static final String ID = "id";
    private static final String DESCRIPTION = "description";
    private static final String PRIORITY = "priority";
    private static final String DATE = "updatedAt";

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
            }
        });

        mListView = findViewById(R.id.taskList);
        RelativeLayout emptyView = findViewById(R.id.empty_view);
        mListView.setEmptyView(emptyView);
        mListView.setOnItemClickListener(this);


        final Database database = AppDatabase.getInstance(getApplicationContext());

        showTasks(database);
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


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(MainActivity.this, EditorActivity.class);
        intent.putExtra(ID, String.valueOf(position));
        startActivity(intent);
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


    // END
}
