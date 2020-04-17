package com.nyit.attendanceapp;

public class Lesson {

    private int lid;
    private String date;
    private Course course;
    private String time;

    public Lesson(String date, String time, Course course, int lid){
        this.date = date;
        this.course = course;
        this.lid = lid;
        this.time = time;
    }

    public Lesson(String date, String time, Course course){
        this.date = date;
        this.course = course;
        this.time = time;
    }


    public String getDate()  {return date;}
    public int getLid(){return  lid;}
    public String getTime(){return time;}
    public Course getCourse(){return course;}
}
