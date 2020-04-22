package com.nyit.attendanceapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

public class AddCourseActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_class_dialog);
        configureBackButton();
        configureStudentList();
    }

    private void configureBackButton(){
        AppCompatButton acb = findViewById(R.id.cancelClass);
        acb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void configureStudentList(){
        ListView lv = findViewById(R.id.createClassStudentList);
        SelectableStudentListAdapter adapter = new SelectableStudentListAdapter(this);
        lv.setAdapter(adapter);
    }
}
