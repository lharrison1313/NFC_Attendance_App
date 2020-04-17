package com.nyit.attendanceapp;

public class AttendanceContract {

    private AttendanceContract() {

    }

    public static class StudentTable {
        // primary key SID
        public static final String TABLE_NAME = "Student";
        public static final String COLUMN_NAME_SNAME = "sName";
        public static final String COLUM_NAME_SID = "SID";

    }

    public static class Course {
        // primary key CID and Section
        public static final String TABLE_NAME = "Class";
        public static final String COLUMN_NAME_CID = "CID";
        public static final String COLUMN_NAME_CNAME = "cName";
        public static final String COLUMN_NAME_SECTION = "Section";
    }

    public static class LessonTable {
        // Primary key LID, Foreign key CID and Section
        public static final String TABLE_NAME = "Lesson";
        public static final String COLUMN_NAME_LID = "LID";
        public static final String COLUMN_NAME_CID = "CID";
        public static final String COLUMN_NAME_SECTION = "Section";
        public static final String COLUMN_NAME_LESSON = "LessonName";
        public static final String COLUMN_NAME_DATE = "Date";
        public static final String COLUMN_NAME_TIME = "Time";
    }

    public static class RosterEntryTable {
        // primary key CID, SID and Section
        public static final String TABLE_NAME = "RosterEntry";
        public static final String COLUMN_NAME_CID = "CID";
        public static final String COLUMN_NAME_SID = "SID";
        public static final String COLUMN_NAME_SECTION = "Section";
        public static final String COLUMN_NAME_PRESENT = "Present";
        public static final String COLUMN_NAME_ABSENT = "Absent";
        public static final String COLUMN_NAME_EXCUSED = "Excused";
        public static final String COLUMN_NAME_TARDY = "Tardy";
    }


    public static class AttendanceEntryTable {
        // PRIMARY key LID and SID
        public static final String TABLE_NAME = "AttendanceEntry";
        public static final String COLUMN_NAME_LID = "LID";
        public static final String COLUMN_NAME_SID = "SID";
        public static final String COLUMN_NAME_PAXT = "PAXT";
    }

}
