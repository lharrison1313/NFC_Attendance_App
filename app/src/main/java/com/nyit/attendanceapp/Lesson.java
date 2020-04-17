package com.nyit.attendanceapp;

public class Lesson {
    private String lesson_name;
    private String date;
    private Course course;
    private String lid;
    private String time;

    public Lesson(String lesson_name, String date, String time, Course course, String lid){
        this.lesson_name = lesson_name;
        this.date = date;
        this.course = course;
        this.lid = lid;
        this.time = time;
    }

    public String getLessonName() {return lesson_name;}
    public String getDate()  {return date;}
    public String getLid(){return  lid;}
    public String getTime(){return time;}
    public Course getCourse(){return course;}
}
