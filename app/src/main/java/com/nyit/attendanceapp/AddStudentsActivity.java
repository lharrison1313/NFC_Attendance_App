package com.nyit.attendanceapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.textfield.TextInputEditText;

public class AddStudentsActivity extends Activity {

    private AttendanceDbHelper db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_student_dialog);
        db = new AttendanceDbHelper(this);
        configureBackButton();
        configureDoneButton();
    }

    private void configureBackButton(){
        AppCompatButton acb = findViewById(R.id.cancelStudents);
        acb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void configureDoneButton(){
        AppCompatButton acb = findViewById(R.id.doneStudents);
        acb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputEditText nameField = findViewById(R.id.studentNameInput);
                TextInputEditText idField = findViewById(R.id.studentIdInput);
                createStudent(new Student(nameField.getText().toString(),idField.getText().toString()));
                finish();
            }
        });
    }

    private void createStudent(Student s){
        db.addStudent(s);
    }



}
