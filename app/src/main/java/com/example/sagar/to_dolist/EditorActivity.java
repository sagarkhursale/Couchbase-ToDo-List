package com.example.sagar.to_dolist;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class EditorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            int mId = bundle.getInt("Id");
            setEditorTitle(1);


        } else {
            setEditorTitle(0);

        }

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

// END
}
