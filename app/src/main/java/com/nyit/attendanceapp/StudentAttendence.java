package com.nyit.attendanceapp;

public class StudentAttendence {
    private String student;
    private String course;
    private int present;
    private int absent;
    private int excused;
    private int tardy;

    public StudentAttendence( String student, String course, int present, int absent, int excused, int tardy)
    {
        this.student = student;
        this.course = course;
        this.present = present;
        this.absent = absent;
        this.excused = excused;
        this.tardy = tardy;
    }
    public String getStudent () {return student;}
    public String getCourse () {return course;}
    public int getPresent () {return present;}
    public int getAbsent () {return absent;}
    public int getExcused () {return excused;}
    public int getTardy () {return tardy;}
}
