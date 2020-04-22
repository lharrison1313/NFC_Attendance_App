package com.nyit.attendanceapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class AttendanceDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Attendance.db";

    // SQL create and delete database commands
    private static final String SQL_CREATE_STUDENTS = String.format("CREATE TABLE %s (%s TEXT PRIMARY KEY, %s TEXT)",
            AttendanceContract.StudentTable.TABLE_NAME, AttendanceContract.StudentTable.COLUM_NAME_SID,
            AttendanceContract.StudentTable.COLUMN_NAME_SNAME);

    private static final String SQL_DELETE_STUDENTS = String.format("DROP TABLE IF EXISTS %s",
            AttendanceContract.StudentTable.TABLE_NAME);

    private static final String SQL_CREATE_CLASS = String.format(
            "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, )",
            AttendanceContract.Course.TABLE_NAME, AttendanceContract.Course.COLUMN_NAME_CID,
            AttendanceContract.Course.COLUMN_NAME_SECTION, AttendanceContract.Course.COLUMN_NAME_CNAME);

    private static final String SQL_DELETE_CLASS = String.format("DROP TABLE IF EXISTS %s",
            AttendanceContract.Course.TABLE_NAME);

    private static final String SQL_CREATE_LESSON = String.format(
            "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s TEXT, %s TEXT)",
            AttendanceContract.LessonTable.TABLE_NAME, AttendanceContract.LessonTable.COLUMN_NAME_LID,
            AttendanceContract.LessonTable.COLUMN_NAME_CID, AttendanceContract.LessonTable.COLUMN_NAME_DATE,
            AttendanceContract.LessonTable.COLUMN_NAME_TIME);

    private static final String SQL_DELETE_LESSON = String.format("DROP TABLE IF EXISTS %s",
            AttendanceContract.LessonTable.TABLE_NAME);

    private static final String SQL_CREATE_ROSTER_ENTRY = String.format(
            "Create Table %s (%s INTEGER, %s TEXT, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, PRIMARY KEY(%s, %s))",
            AttendanceContract.RosterEntryTable.COLUMN_NAME_CID, AttendanceContract.RosterEntryTable.COLUMN_NAME_SID,
            AttendanceContract.RosterEntryTable.COLUMN_NAME_PRESENT, AttendanceContract.RosterEntryTable.COLUMN_NAME_ABSENT,
            AttendanceContract.RosterEntryTable.COLUMN_NAME_EXCUSED, AttendanceContract.RosterEntryTable.COLUMN_NAME_TARDY,
            AttendanceContract.RosterEntryTable.COLUMN_NAME_CID, AttendanceContract.RosterEntryTable.COLUMN_NAME_SID);

    private static final String SQL_DELETE_ROSTER_ENTRY = String.format("DROP TABLE IF EXISTS %s",
            AttendanceContract.RosterEntryTable.TABLE_NAME);

    // Constructor
    public AttendanceDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // override helper methods
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_STUDENTS);
        db.execSQL(SQL_CREATE_CLASS);
        db.execSQL(SQL_CREATE_LESSON);
        db.execSQL(SQL_CREATE_ROSTER_ENTRY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_STUDENTS);
        db.execSQL(SQL_DELETE_CLASS);
        db.execSQL(SQL_DELETE_LESSON);
        db.execSQL(SQL_DELETE_ROSTER_ENTRY);
        onCreate(db);
    }

    // Student Table helpers
    public void addStudent(Student s) {
        SQLiteDatabase db = this.getWritableDatabase();

        // loading getting values from student object
        ContentValues values = new ContentValues();
        values.put(AttendanceContract.StudentTable.COLUMN_NAME_SNAME, s.getName());
        values.put(AttendanceContract.StudentTable.COLUM_NAME_SID, s.getId());

        // inserting student into database
        db.insert(AttendanceContract.StudentTable.TABLE_NAME, null, values);
    }
    public void deleteStudent(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = AttendanceContract.StudentTable.COLUM_NAME_SID + "=" + id;
        db.delete(AttendanceContract.StudentTable.TABLE_NAME, where, null);
    }

    public void removeAllStudents() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(AttendanceContract.StudentTable.TABLE_NAME, null, null);
    }

    public ArrayList<Student> retrieveAllStudents() {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = { AttendanceContract.StudentTable.COLUM_NAME_SID,
                AttendanceContract.StudentTable.COLUMN_NAME_SNAME };

        String sorting = AttendanceContract.StudentTable.COLUMN_NAME_SNAME + " ASC";

        Cursor c = db.query(AttendanceContract.StudentTable.TABLE_NAME, projection, null, null, null, null, sorting);
        ArrayList<Student> students = new ArrayList<>();
        while (c.moveToNext()) {
            String id = c.getString(c.getColumnIndex(AttendanceContract.StudentTable.COLUM_NAME_SID));
            String name = c.getString(c.getColumnIndex(AttendanceContract.StudentTable.COLUMN_NAME_SNAME));
            students.add(new Student(name, id));
        }
        c.close();
        return students;
    }

    // Class Table helpers
    public void addCourse(Course c) {
        SQLiteDatabase db = this.getWritableDatabase();

        // loading getting values from class object
        ContentValues values = new ContentValues();
        values.put(AttendanceContract.Course.COLUMN_NAME_CNAME, c.getName());
        values.put(AttendanceContract.Course.COLUMN_NAME_SECTION, c.getSection());

        // inserting class into database
        db.insert(AttendanceContract.Course.TABLE_NAME, null, values);
    }

    public void deleteCourse(String cid) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = AttendanceContract.Course.COLUMN_NAME_CID + "=" + cid;
        db.delete(AttendanceContract.Course.TABLE_NAME, where, null);
    }

    public void removeAllCourse() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(AttendanceContract.StudentTable.TABLE_NAME, null, null);
    }

    public Course retrieveSingleCourse(int cid){
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
            AttendanceContract.Course.COLUMN_NAME_CID,
            AttendanceContract.Course.COLUMN_NAME_CNAME,
            AttendanceContract.Course.COLUMN_NAME_SECTION
        };

        String where = AttendanceContract.Course.COLUMN_NAME_CID + "=" + cid;

        Cursor c = db.query(AttendanceContract.Course.TABLE_NAME, projection, where, null, null, null, null);
        c.moveToNext();

        int id = c.getInt(c.getColumnIndex(AttendanceContract.Course.COLUMN_NAME_CID));
        String name = c.getString(c.getColumnIndex(AttendanceContract.Course.COLUMN_NAME_CNAME));
        String section = c.getString(c.getColumnIndex(AttendanceContract.Course.COLUMN_NAME_SECTION));

        return new Course(name,section,id);
    }

    public ArrayList<Course> retrieveAllCourses() {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
            AttendanceContract.Course.COLUMN_NAME_CID,
            AttendanceContract.Course.COLUMN_NAME_CNAME,
            AttendanceContract.Course.COLUMN_NAME_SECTION
        };

        String sorting = AttendanceContract.Course.COLUMN_NAME_CNAME + " ASC";

        Cursor c = db.query(AttendanceContract.Course.TABLE_NAME, projection, null, null, null, null, sorting);
        ArrayList<Course> courses = new ArrayList<>();
        while (c.moveToNext()) {

            int id = c.getInt(c.getColumnIndex(AttendanceContract.Course.COLUMN_NAME_CID));
            String name = c.getString(c.getColumnIndex(AttendanceContract.Course.COLUMN_NAME_CNAME));
            String section = c.getString(c.getColumnIndex(AttendanceContract.Course.COLUMN_NAME_SECTION));
            courses.add(new Course(name, section, id));
        }
        c.close();
        return courses;
    }

    // Lesson Table helpers
    public void addLesson(Lesson l) {
        SQLiteDatabase db = this.getWritableDatabase();

        // loading getting values from lesson object
        ContentValues values = new ContentValues();
        values.put(AttendanceContract.LessonTable.COLUMN_NAME_CID, l.getCourse().getCid());
        values.put(AttendanceContract.LessonTable.COLUMN_NAME_DATE, l.getDate());
        values.put(AttendanceContract.LessonTable.COLUMN_NAME_TIME, l.getTime());

        // inserting class lesson database
        db.insert(AttendanceContract.LessonTable.TABLE_NAME, null, values);
    }

    public void deleteLesson(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = AttendanceContract.LessonTable.COLUMN_NAME_LID + "=" + id;
        db.delete(AttendanceContract.LessonTable.TABLE_NAME, where, null);
    }

    public void removeAllLessons() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(AttendanceContract.LessonTable.TABLE_NAME, null, null);

    }

    public ArrayList<Lesson> retrieveAllLessons() {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                AttendanceContract.LessonTable.COLUMN_NAME_LID,
                AttendanceContract.LessonTable.COLUMN_NAME_CID,
                AttendanceContract.LessonTable.COLUMN_NAME_DATE,
                AttendanceContract.LessonTable.COLUMN_NAME_TIME,
        };

        String sorting = AttendanceContract.LessonTable.COLUMN_NAME_CID + "DESC";

        Cursor c = db.query(AttendanceContract.LessonTable.TABLE_NAME, projection, null, null, null, null, sorting);
        ArrayList<Lesson> lessons = new ArrayList<>();
        while (c.moveToNext()) {

            int cid = c.getInt(c.getColumnIndex(AttendanceContract.LessonTable.COLUMN_NAME_CID));
            int lid = c.getInt(c.getColumnIndex(AttendanceContract.LessonTable.COLUMN_NAME_LID));
            String date= c.getString(c.getColumnIndex(AttendanceContract.LessonTable.COLUMN_NAME_DATE));
            String time= c.getString(c.getColumnIndex(AttendanceContract.LessonTable.COLUMN_NAME_TIME));
            Course course = retrieveSingleCourse(cid);

            lessons.add(new Lesson(date,time,course,lid));
        }
        c.close();
        return lessons;
    }
}
