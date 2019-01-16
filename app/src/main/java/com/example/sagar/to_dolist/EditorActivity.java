package com.example.sagar.to_dolist;

import android.content.Intent;
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
import com.couchbase.lite.Document;
import com.couchbase.lite.MutableDocument;

import java.util.Date;


public class EditorActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = EditorActivity.class.getSimpleName();

    // Constants for priority
    public static final int PRIORITY_HIGH = 1;
    public static final int PRIORITY_MEDIUM = 2;
    public static final int PRIORITY_LOW = 3;

    // Constants for fields
    private final String ID = "id";
    private final String DESCRIPTION = "description";
    private final String PRIORITY = "priority";
    private final String DATE = "updatedAt";

    // views
    private EditText mEditText;
    private RadioGroup mRadioGroup;
    private Button mButton;

    private static String mTaskId;
    private Database mDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mDb = AppDatabase.getInstance(getApplicationContext());

        mEditText = findViewById(R.id.editTextTaskDescription);
        mRadioGroup = findViewById(R.id.radioGroup);
        mButton = findViewById(R.id.saveButton);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mTaskId = bundle.getString(ID);
            setEditorTitle(1);
            populateUi(mTaskId);
        } else {
            setEditorTitle(0);
        }

        mButton.setOnClickListener(this);

        //
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
        Document document = mDb.getDocument(docId);

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

        if (mTaskId != null) {
            Document document = mDb.getDocument(mTaskId);

            if (document != null) {
                try {
                    MutableDocument updateDoc = document.toMutable();

                    updateDoc.setString(ID, mTaskId);
                    updateDoc.setString(DESCRIPTION, mEditText.getText().toString().trim());
                    updateDoc.setInt(PRIORITY, getPriorityFromViews());
                    updateDoc.setDate(DATE, new Date());

                    mDb.save(updateDoc);
                    Toast.makeText(this, "Task Updated!", Toast.LENGTH_SHORT).show();
                  //  gotoHome();

                } catch (CouchbaseLiteException e) {
                    Log.e(TAG, "Edit task : " + e.toString());
                }
            }
        } else {
            int count = (int) mDb.getCount();
            Log.i(TAG, "count : " + count);

            try {

                MutableDocument addDoc = new MutableDocument(String.valueOf(count));

                addDoc.setString(ID, String.valueOf(count));
                addDoc.setString(DESCRIPTION, mEditText.getText().toString().trim());
                addDoc.setInt(PRIORITY, getPriorityFromViews());
                addDoc.setDate(DATE, new Date());

                mDb.save(addDoc);
                Toast.makeText(this, "New Task Added.", Toast.LENGTH_SHORT).show();
                //gotoHome();
            } catch (CouchbaseLiteException e) {
                Log.e(TAG, "Add task : " + e.toString());
            }
        }

        // end
    }


    private void gotoHome() {
        Intent intent = new Intent(EditorActivity.this, MainActivity.class);
        startActivity(intent);
    }

// END

}
