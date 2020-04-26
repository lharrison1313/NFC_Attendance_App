package com.nyit.attendanceapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;

import java.util.List;

public class ClassInfoActivity extends Activity {
    private String name;
    private String section;
    private DeleteableStudentListAdapter adapter;
    private Course course;
    private AttendanceDbHelper db;
    private Dialog deleteDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.class_info);
        db = new AttendanceDbHelper(this);
        configureClassHeader();
        configureBackButton();
        course = new Course(name,section);
        configureStudentList();
        configureDeleteDialog();
        configureDeleteButton();
        configureAddStudentButton();


    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.populateStudentList();
    }

    private void configureClassHeader(){
        name = getIntent().getStringExtra("name");
        section = getIntent().getStringExtra("section");

        TextView className = findViewById(R.id.infoClassName);
        TextView classSection = findViewById(R.id.infoClassSection);
        className.setText(name);
        classSection.setText(section);
    }

    private void configureBackButton(){
        AppCompatButton acb = findViewById(R.id.classInfoDone);
        acb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void configureStudentList(){
        ListView lv = findViewById(R.id.classInfoStudentList);
        adapter = new DeleteableStudentListAdapter(this,course);
        lv.setAdapter(adapter);
    }

    private void configureDeleteButton(){
        AppCompatImageButton acib = findViewById(R.id.infoDeleteClass);
        acib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.show();
            }
        });
    }

    private void configureDeleteDialog(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Confirm Delete").setMessage("Are you sure you want to delete this Class?");
        builder1.setPositiveButton("confirm",new Dialog.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db.deleteCourse(course);
                finish();

            }
        });
        builder1.setNegativeButton("cancel",new Dialog.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        deleteDialog = builder1.create();
    }

    private void configureAddStudentButton(){
        AppCompatButton acb = findViewById(R.id.addStudentButton);
        final Intent i = new Intent(this, AddStudentsToCourseActivity.class);
        acb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.putExtra("student_list",adapter.getStudentList());
                i.putExtra("name",name);
                i.putExtra("section",section);
                startActivity(i);
            }
        });
    }




}
