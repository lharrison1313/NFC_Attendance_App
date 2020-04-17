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
            "CREATE TABLE %s (%s TEXT PRIMARY KEY, %s TEXT PRIMARY KEY, %s TEXT)",
            AttendanceContract.ClassTable.TABLE_NAME, AttendanceContract.ClassTable.COLUMN_NAME_CID,
            AttendanceContract.ClassTable.COLUMN_NAME_CNAME, AttendanceContract.ClassTable.COLUMN_NAME_SECTION);

    private static final String SQL_DELETE_CLASS = String.format("DROP TABLE IF EXISTS %s",
            AttendanceContract.ClassTable.TABLE_NAME);

    private static final String SQL_CREATE_LESSON = String.format(
            "CREATE TABLE %s (%s TEXT PRIMARY KEY,%s TEXT FOREIGN KEY,%s TEXT FOREIGN KEY, %s TEXT, %s TEXT, %s TEXT)",
            AttendanceContract.LessonTable.TABLE_NAME, AttendanceContract.LessonTable.COLUMN_NAME_LID,
            AttendanceContract.LessonTable.COLUM_NAME_CID, AttendanceContract.LessonTable.COLUMN_NAME_SECTION,
            AttendanceContract.LessonTable.COLUMN_NAME_LESSON);

    private static final String SQL_DELETE_LESSON = String.format("DROP TABLE IF EXISTS %s",
            AttendanceContract.LessonTable.TABLE_NAME);

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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_STUDENTS);
        db.execSQL(SQL_DELETE_CLASS);
        db.execSQL(SQL_DELETE_LESSON);
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
    public void addClass(Class c) {
        SQLiteDatabase db = this.getWritableDatabase();

        // loading getting values from class object
        ContentValues values = new ContentValues();
        values.put(AttendanceContract.ClassTable.COLUMN_NAME_CID, c.getCID());
        values.put(AttendanceContract.ClassTable.COLUMN_NAME_CNAME, c.getName());
        values.put(AttendanceContract.ClassTable.COLUMN_NAME_SECTION, c.getSection());

        // inserting class into database
        db.insert(AttendanceContract.ClassTable.TABLE_NAME, null, values);
    }

    public void deleteClass(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = AttendanceContract.ClassTable.COLUMN_NAME_CID + "=" + id;
        db.delete(AttendanceContract.ClassTable.TABLE_NAME, where, null);
    }

    public void removeAllClasses() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(AttendanceContract.StudentTable.TABLE_NAME, null, null);
    }

    public ArrayList<Class> retrieveAllClasses() {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {       AttendanceContract.ClassTable.COLUMN_NAME_CID,
            AttendanceContract.ClassTable.COLUMN_NAME_CNAME,
          AttendanceContract.ClassTable.COLUMN_NAME_SECTION
            };

        String sorting = AttendanceContract.ClassTable.COLUMN_NAME_CNAME + " ASC";

        Cursor c = db.query(AttendanceContract.ClassTable.TABLE_NAME, projection, null, null, null, null, sorting);
        ArrayList<Class> classes = new ArrayList<>();
        while (c.moveToNext()) {

            String id = c.getString(c.getColumnIndex(AttendanceContract.ClassTable.COLUMN_NAME_CID));
            String name = c.getString(c.getColumnIndex(AttendanceContract.ClassTable.COLUMN_NAME_CNAME));
            String section = c.getString(c.getColumnIndex(AttendanceContract.ClassTable.COLUMN_NAME_SECTION));
            classes.add(new Student(id,name, section));
        }
        c.close();
        return classes;
    }





    // Lesson Table helpers
    public void addLesson(Lesson l) {
        SQLiteDatabase db = this.getWritableDatabase();

        // loading getting values from lesson object
        ContentValues values = new ContentValues();
        values.put(AttendanceContract.LessonTable.COLUMN_NAME_LID, l.getLID());
        values.put(AttendanceContract.LessonTable.COLUMN_NAME_CID, l.getCID());
        values.put(AttendanceContract.LessonTable.COLUMN_NAME_SECTION, l.getSection());
        values.put(AttendanceContract.LessonTable.COLUMN_NAME_LESSON, l.getLesson());
        values.put(AttendanceContract.LessonTable.COLUMN_NAME_DATE, l.getDate());
        values.put(AttendanceContract.LessonTable.COLUMN_NAME_TIME, l.getTime());

        // inserting class lesson database
        db.insert(AttendanceContract.LessonTable.TABLE_NAME, null, values);
    }

    public void deleteLesson(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = AttendanceContract.LessonTable.COLUM_NAME_LID + "=" + id;
        db.delete(AttendanceContract.LessonTable.TABLE_NAME, where, null);
    }

    public void removeAllLessons() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(AttendanceContract.LessonTable.TABLE_NAME, null, null);

    }



    public ArrayList<Class> retrieveAllLessons() {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {         AttendanceContract.LessonTable.COLUMN_NAME_LID,
        AttendanceContract.LessonTable.COLUMN_NAME_CID, 
        AttendanceContract.LessonTable.COLUMN_NAME_SECTION, 
        AttendanceContract.LessonTable.COLUMN_NAME_LESSON, 
        AttendanceContract.LessonTable.COLUMN_NAME_DATE,
        AttendanceContract.LessonTable.COLUMN_NAME_TIME, 
            };

        String sorting = AttendanceContract.LessonTable.COLUMN_NAME_CNAME + " ASC";

        Cursor c = db.query(AttendanceContract.LessonTable.TABLE_NAME, projection, null, null, null, null, sorting);
        ArrayList<Class> lessons = new ArrayList<>();
        while (c.moveToNext()) {

            String id = c.getString(c.getColumnIndex(AttendanceContract.Lessonable.COLUM_NAME_CID));
            String section = c.getString(c.getColumnIndex(AttendanceContract.LessonTable.COLUMN_NAME_SECTION));
            String lesson= c.getString(c.getColumnIndex(AttendanceContract.LessonTable.COLUMN_NAME_LESSON));
            String date= c.getString(c.getColumnIndex(AttendanceContract.LessonTable.COLUMN_NAME_DATE));
            String time= c.getString(c.getColumnIndex(AttendanceContract.LessonTable.COLUMN_NAME_TIME));


            classes.add(new Student(id,section,lesson,date,time));
        }
        c.close();
        return lessons;
    }
}