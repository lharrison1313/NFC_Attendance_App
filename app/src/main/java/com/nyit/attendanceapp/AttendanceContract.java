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

//    public static class ClassTable{
//        //primary key CID and Section
//        public static final String TABLE_NAME = "Class";
//        public static final String COLUMN_NAME_CID = "CID";
//        public static final String COLUMN_NAME_CNAME = "cName";
//        public static final String COLUMN_NAME_SECTION = "Section";
//    }
//
//    public static class StudentTakesTable{
//        public static final String TABLE_NAME = "Student_Takes";
//        public static final String COLUMN_NAME_SID = "SID";
//        public static final String COLUMN_NAME_CID = "CID";
//        public static final String COLUMN_NAME_SECTION = "Section";
//    }
//
//    public static class SheetTable{
//        public static final String TABLE_NAME = "Sheet";
//        public static final String COLUMN_NAME_SHEETID = "SheetID";
//        public static final String COLUMN_NAME_Date = "Date";
//        public static final String COLUMN_NAME_CID = "CID";
//        public static final String COLUMN_NAME_SECTION = "Section";
//    }

}
