package com.nyit.attendanceapp;

public class AttendanceContract {

    private AttendanceContract(){

    }

    public static class StudentTable{
        //primary key SID
        public static final String TABLE_NAME = "Student";
        public static final String COLUMN_NAME_SNAME = "sName";
        public static final String COLUM_NAME_SID = "SID";


    }

}
