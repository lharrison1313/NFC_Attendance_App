package com.nyit.attendanceapp;

import java.util.ArrayList;

public class AttendanceSheet {
    private ArrayList<Attendance> attendances;
    private Course course;
    private Lesson lesson;

    public AttendanceSheet(ArrayList<Attendance> attendances, Course course, Lesson lesson ){
        this.attendances = attendances;
        this.course = course;
        this.lesson = lesson;
    }

    public ArrayList<Attendance> getAttendances() {
        return attendances;
    }

    public Course getCourse() {
        return course;
    }

    public Lesson getLesson() {return lesson; }
}
