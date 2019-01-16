package com.example.sagar.to_dolist;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.DatabaseChange;
import com.couchbase.lite.DatabaseChangeListener;
import com.couchbase.lite.Document;
import com.couchbase.lite.MutableDocument;

import java.util.Date;


public class EditorActivity extends AppCompatActivity implements View.OnClickListener, DatabaseChangeListener {

    private final String TAG = EditorActivity.class.getSimpleName();
    private static final String DEFAULT_TASK_ID="-1";

    // Constants for priority
    public static final int PRIORITY_HIGH = 1;
    public static final int PRIORITY_MEDIUM = 2;
    public static final int PRIORITY_LOW = 3;

    // Constants for fields
    private final String ID = "id";
    private final String DESCRIPTION = "description";
    private final String PRIORITY = "priority";
    private final String DATE = "updatedAt";

    private EditText mEditText;
    private static String mTaskId=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mEditText = findViewById(R.id.editTextTaskDescription);
        Button mButton = findViewById(R.id.saveButton);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mTaskId = bundle.getString(ID);
            setEditorTitle(1);
            populateUi(mTaskId);
        } else {
            mTaskId=DEFAULT_TASK_ID;
            setEditorTitle(0);
        }

        mButton.setOnClickListener(this);

        getDb().addChangeListener(this);
        //
    }

    private Database getDb() {
        return AppDatabase.getInstance(
                getApplicationContext());
    }


    private void setEditorTitle(int flag) {
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            if (flag == 0) {
                actionBar.setTitle(getString
                        (R.string.editor_activity_title_new_task));
            } else {
                actionBar.setTitle(getString(R.string.
                        editor_activity_title_edit_task));
            }
        }

    }


    private void populateUi(String docId) {
        Document document = getDb().getDocument(docId);

        if (document != null) {
            mEditText.setText(document.getString(DESCRIPTION));
            setPriorityInViews(document.getInt(PRIORITY));
        }
    }


    public void setPriorityInViews(int priority) {
        switch (priority) {
            case PRIORITY_HIGH:
                ((RadioGroup) findViewById(R.id.radioGroup)).check(R.id.radButton1);
                break;
            case PRIORITY_MEDIUM:
                ((RadioGroup) findViewById(R.id.radioGroup)).check(R.id.radButton2);
                break;
            case PRIORITY_LOW:
                ((RadioGroup) findViewById(R.id.radioGroup)).check(R.id.radButton3);
        }
    }


    public int getPriorityFromViews() {
        int priority = 1;
        int checkedId = ((RadioGroup) findViewById(R.id.radioGroup)).getCheckedRadioButtonId();

        switch (checkedId) {
            case R.id.radButton1:
                priority = PRIORITY_HIGH;
                break;
            case R.id.radButton2:
                priority = PRIORITY_MEDIUM;
                break;
            case R.id.radButton3:
                priority = PRIORITY_LOW;
        }
        return priority;
    }


    @Override
    public void onClick(View v) {
        if (!mTaskId.equals(DEFAULT_TASK_ID)) {
            Log.i(TAG,"1111>>>>> "+mTaskId);
            updateDocument(getDb());
        } else {
            Log.i(TAG,"2222>>>>> "+null);
            addDocument(getDb());
        }
    }


    private void addDocument(Database database) {
        int count = (int) database.getCount();
        Log.i(TAG, "count : " + count);

        try {
            MutableDocument addDoc = new MutableDocument(
                    String.valueOf(count));

            addDoc.setString(ID, String.valueOf(count));
            addDoc.setString(DESCRIPTION, mEditText.getText().toString().trim());
            addDoc.setInt(PRIORITY, getPriorityFromViews());
            addDoc.setDate(DATE, new Date());

            database.save(addDoc);
            Toast.makeText(this, "New Task Added.", Toast.LENGTH_SHORT).show();
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Add task : " + e.toString());
        }
    }



    private void updateDocument(Database database) {
        Document document = database.getDocument(mTaskId);

        if (document != null) {
            try {
                MutableDocument updateDoc = document.toMutable();

                updateDoc.setString(ID, mTaskId);
                updateDoc.setString(DESCRIPTION, mEditText.getText().toString().trim());
                updateDoc.setInt(PRIORITY, getPriorityFromViews());
                updateDoc.setDate(DATE, new Date());

                database.save(updateDoc);
                Toast.makeText(this, "Task Updated!", Toast.LENGTH_SHORT).show();

            } catch (CouchbaseLiteException e) {
                Log.e(TAG, "Edit task : " + e.toString());
            }
        }
    }


    @Override
    public void changed(DatabaseChange change) {
        Intent intent = new Intent(
                EditorActivity.this, MainActivity.class);
        startActivity(intent);
    }

// END

}
