package com.nyit.attendanceapp;

public class Attendance {

   private Student student;
   private int lid;
   private String paxt;




    public Attendance(Student student, int lid, String paxt ){
        this.student = student;
        this.lid = lid;
        this.paxt = paxt;
    }

    public Student getStudent(){
        return student;
    }

    public int getLid(){
        return lid;
    }

    public String getPaxt(){
        return paxt;
    }

}
