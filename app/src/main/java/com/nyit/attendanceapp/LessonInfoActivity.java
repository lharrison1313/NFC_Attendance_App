package com.nyit.attendanceapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

public class LessonInfoActivity extends Activity {

    private String name, section, date, time;
    private int id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sheet_info);
        configureBackButton();
        configureLessonHeader();
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
}
