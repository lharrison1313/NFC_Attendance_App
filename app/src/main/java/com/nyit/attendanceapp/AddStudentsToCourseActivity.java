package com.nyit.attendanceapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import java.util.ArrayList;

public class AddStudentsToCourseActivity extends Activity {

    SelectableStudentListAdapter adapter;
    AttendanceDbHelper db;
    Course c;
    ArrayList<Student> students;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_student_to_course);
        String name = getIntent().getStringExtra("name");
        String section = getIntent().getStringExtra("section");
        c = new Course(name,section);
        students = (ArrayList<Student>) getIntent().getSerializableExtra("student_list");
        db = new AttendanceDbHelper(this);
        configureCancelButton();
        configureDoneButton();
        configureStudentList();

    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.populateStudentList();
        adapter.removeStudentsFromList(students);
    }

    public void configureStudentList(){
        adapter = new SelectableStudentListAdapter(this);
        adapter.removeStudentsFromList(students);
        ListView lv = findViewById(R.id.student_edit_roster_list);
        lv.setAdapter(adapter);

    }

    public void configureDoneButton(){
        AppCompatButton acb = findViewById(R.id.student_roster_Done);
        final Toast t = Toast.makeText(this,"no students selected",Toast.LENGTH_SHORT);
        acb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Student> students = adapter.getSelectedStudents();
                if(!students.isEmpty()) {
                    db.addMultipleRosterEntry(students, c);
                    finish();
                }
                else{
                    t.show();
                }
            }
        });

    }

    public void configureCancelButton(){
        AppCompatButton acb = findViewById(R.id.student_roster_cancel);
        acb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
