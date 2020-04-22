package com.nyit.attendanceapp;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.textfield.TextInputEditText;


public class AddCourseActivity extends Activity {

    private SelectableStudentListAdapter adapter;
    private AttendanceDbHelper db;
    private TextInputEditText courseNameInput;
    private TextInputEditText courseSectionInput;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_class_dialog);
        db = new AttendanceDbHelper(this);
        courseNameInput = findViewById(R.id.nameField);
        courseSectionInput = findViewById(R.id.sectionField);
        configureBackButton();
        configureStudentList();
        configureDoneButton();
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
        adapter = new SelectableStudentListAdapter(this);
        lv.setAdapter(adapter);
    }

    private void configureDoneButton(){
        AppCompatButton acb = findViewById((R.id.submit_button_class));
        acb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = courseNameInput.getText().toString();
                String section = courseSectionInput.getText().toString();

                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(section)){
                    courseNameInput.setError("Name field cannot be empty");
                    courseSectionInput.setError("ID field cannot be empty");
                }
                else {
                    Course c = new Course(name,section);
                    db.addCourse(c);
                    db.addMultipleRosterEntry(adapter.getSelectedStudents(),c);
                    finish();
                }
            }
        });
    }
}
