package com.nyit.attendanceapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

public class LessonInfoActivity extends Activity {

    private String name, section, date, time;
    private AttendanceListAdapter adapter;
    private int id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sheet_info);
        configureBackButton();
        configureLessonHeader();
        configureAttendanceList();
    }


    private void configureBackButton(){
        AppCompatButton acb = findViewById(R.id.sheetDoneButton);
        acb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.populateAttendanceList();
    }

    private void configureLessonHeader(){

        name = getIntent().getStringExtra("class_name");
        section = getIntent().getStringExtra("class_section");
        date = getIntent().getStringExtra("date");
        time = getIntent().getStringExtra("time");
        id = getIntent().getIntExtra("id",0);


        TextView lessonDate = findViewById(R.id.SheetInfoDate);
        TextView className = findViewById(R.id.SheetInfoClassName);
        TextView classSection = findViewById(R.id.SheetInfoSection);

        lessonDate.setText(date + " " + time);
        className.setText(name);
        classSection.setText(section);

    }

    private void configureAttendanceList(){
        ListView lv = findViewById(R.id.SheetInfoStudentList);
        adapter = new AttendanceListAdapter(this,id);
        lv.setAdapter(adapter);
    }



}
