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

    //SQL create and delete database commands
    private static final String SQL_CREATE_STUDENTS =
            String.format("CREATE TABLE %s (%s TEXT PRIMARY KEY, %s TEXT)", AttendanceContract.StudentTable.TABLE_NAME, AttendanceContract.StudentTable.COLUM_NAME_SID, AttendanceContract.StudentTable.COLUMN_NAME_SNAME);

    private static final String SQL_DELETE_STUDENTS = String.format("DROP TABLE IF EXISTS %s", AttendanceContract.StudentTable.TABLE_NAME);


    //Constructor
    public AttendanceDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //override helper methods
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_STUDENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_STUDENTS);
        onCreate(db);
    }

    public void addStudent(Student s){
        SQLiteDatabase db = this.getWritableDatabase();

        //loading getting values from student object
        ContentValues values = new ContentValues();
        values.put(AttendanceContract.StudentTable.COLUMN_NAME_SNAME,s.getName());
        values.put(AttendanceContract.StudentTable.COLUM_NAME_SID,s.getId());

        //inserting student into database
        db.insert(AttendanceContract.StudentTable.TABLE_NAME, null, values);
    }

    public ArrayList<Student> retrieveAllStudents(){
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
            AttendanceContract.StudentTable.COLUM_NAME_SID,
            AttendanceContract.StudentTable.COLUMN_NAME_SNAME
        };

        String sorting = AttendanceContract.StudentTable.COLUMN_NAME_SNAME + " ASC";

        Cursor c = db.query(AttendanceContract.StudentTable.TABLE_NAME,projection,null,null,null,null,sorting);
        ArrayList<Student> students = new ArrayList<>();
        while(c.moveToNext()){
            String id = c.getString(c.getColumnIndex(AttendanceContract.StudentTable.COLUM_NAME_SID));
            String name = c.getString(c.getColumnIndex(AttendanceContract.StudentTable.COLUMN_NAME_SNAME));
            students.add(new Student(name,id));
        }
        c.close();
        return students;
    }



}
