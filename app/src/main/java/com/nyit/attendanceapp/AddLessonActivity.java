package com.nyit.attendanceapp;

import android.app.Activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;

import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;

import java.util.ArrayList;

public class AddLessonActivity extends Activity {

    private AttendanceDbHelper db;
    private AppCompatSpinner spinner;
    private ArrayList<Course>  courses;
    private AppCompatButton dateButton, timeButton;
    private DatePickerDialog dateDialog;
    private TimePickerDialog timeDialog;
    private String date;
    private String time;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_timesheet_dialog);
        db = new AttendanceDbHelper(this);
        courses = db.retrieveAllCourses();
        configureBackButton();
        configureClassSpinner();
        configureDateDialog();
        configureDateButton();
        configureTimeDialog();
        configureTimeButton();
        configureDoneButton();
    }

    private void configureClassSpinner(){
        spinner = findViewById(R.id.class_spinner);
        ArrayAdapter<Course> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,courses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void configureDateDialog(){
        dateDialog = new DatePickerDialog(this);
        dateDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                date = String.format("%d/%d/%d", month+1, dayOfMonth, year);
                dateButton.setText(date);
            }
        });

    }

    private String formatTime(int hour, int minute){
        String minuteFormatted = Integer.toString(minute);
        String hourFormatted = Integer.toString(hour);
        String ampm = "am";
        if(minute >= 0 && minute <= 9){
            minuteFormatted = "0"+minute;
        }

        if(hour == 0){
            hourFormatted = "12";
        }
        else if(hour >= 12 && hour <= 24){
            hourFormatted = Integer.toString((hour==12?hour:hour-12));
            ampm = "pm";
        }
        return String.format("%s:%s%s", hourFormatted,minuteFormatted,ampm);
    }

    private void configureTimeDialog(){
        timeDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                time = formatTime(hourOfDay,minute);
                timeButton.setText(time);
            }
        },12,0,false);

    }

    private void configureDateButton(){
        dateButton = findViewById(R.id.date_button);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateDialog.show();
            }
        });
    }

    private void configureTimeButton(){
        timeButton = findViewById(R.id.time_button);
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeDialog.show();
            }
        });
    }

    private void configureBackButton(){
        AppCompatButton efg = findViewById(R.id.cancelSheets);
        efg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void configureDoneButton(){
        final Toast t1 = Toast.makeText(this,"One or more fields are missing selections.",Toast.LENGTH_SHORT);
        final Toast t2 = Toast.makeText(this,"The selected Date, Time, Class combination is already used.",Toast.LENGTH_SHORT);
        AppCompatButton acb =  findViewById(R.id.lessonDone);
        acb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(date != null && time != null && spinner.getSelectedItem() != null) {
                        Lesson l = new Lesson(date, time, (Course) spinner.getSelectedItem());
                        db.addLesson(l);
                        db.addMultipleStudentAttendanceEntry(db.retrieveSingleLesson(l));
                        finish();

                }
                else{
                    t1.show();
                }
            }
        });
    }
}
