package com.nyit.attendanceapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class AttendanceDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 17;
    public static final String DATABASE_NAME = "Attendance.db";

    // SQL create and delete database commands
    private static final String SQL_CREATE_STUDENTS = String.format("CREATE TABLE %s (%s TEXT PRIMARY KEY, %s TEXT)",
            AttendanceContract.StudentTable.TABLE_NAME, AttendanceContract.StudentTable.COLUM_NAME_SID,
            AttendanceContract.StudentTable.COLUMN_NAME_SNAME);

    private static final String SQL_DELETE_STUDENTS = String.format("DROP TABLE IF EXISTS %s",
            AttendanceContract.StudentTable.TABLE_NAME);

    private static final String SQL_CREATE_CLASS = String.format(
            "CREATE TABLE %s (%s TEXT, %s TEXT, PRIMARY KEY (%s, %s))",
            AttendanceContract.Course.TABLE_NAME, AttendanceContract.Course.COLUMN_NAME_SECTION,
            AttendanceContract.Course.COLUMN_NAME_CNAME,AttendanceContract.Course.COLUMN_NAME_SECTION,
            AttendanceContract.Course.COLUMN_NAME_CNAME);

    private static final String SQL_DELETE_CLASS = String.format("DROP TABLE IF EXISTS %s",
            AttendanceContract.Course.TABLE_NAME);

    private static final String SQL_CREATE_LESSON = String.format(
            "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, UNIQUE(%s,%s,%s,%s))",
            AttendanceContract.LessonTable.TABLE_NAME, AttendanceContract.LessonTable.COLUMN_NAME_LID,
            AttendanceContract.LessonTable.COLUMN_NAME_ClassName, AttendanceContract.LessonTable.COLUMN_NAME_ClassSection,
            AttendanceContract.LessonTable.COLUMN_NAME_DATE, AttendanceContract.LessonTable.COLUMN_NAME_TIME,
            AttendanceContract.LessonTable.COLUMN_NAME_ClassName, AttendanceContract.LessonTable.COLUMN_NAME_ClassSection,
            AttendanceContract.LessonTable.COLUMN_NAME_DATE, AttendanceContract.LessonTable.COLUMN_NAME_TIME);

    private static final String SQL_DELETE_LESSON = String.format("DROP TABLE IF EXISTS %s",
            AttendanceContract.LessonTable.TABLE_NAME);

    private static final String SQL_CREATE_ROSTER_ENTRY = String.format(
            "Create Table %s (%s TEXT, %s TEXT, %s TEXT, PRIMARY KEY(%s, %s, %s))",
            AttendanceContract.RosterEntryTable.TABLE_NAME, AttendanceContract.RosterEntryTable.COLUMN_NAME_ClassName,
            AttendanceContract.RosterEntryTable.COLUMN_NAME_ClassSection, AttendanceContract.RosterEntryTable.COLUMN_NAME_SID,
            AttendanceContract.RosterEntryTable.COLUMN_NAME_ClassName, AttendanceContract.RosterEntryTable.COLUMN_NAME_ClassSection,
            AttendanceContract.RosterEntryTable.COLUMN_NAME_SID);

    private static final String SQL_DELETE_ROSTER_ENTRY = String.format("DROP TABLE IF EXISTS %s",
            AttendanceContract.RosterEntryTable.TABLE_NAME);

    private static final String SQL_CREATE_ATTENDANCE_ENTRY = String.format(
            "Create Table %s (%s INTEGER, %s Text, %s Text, PRIMARY KEY(%s,%s))",
            AttendanceContract.AttendanceEntryTable.TABLE_NAME, AttendanceContract.AttendanceEntryTable.COLUMN_NAME_LID,
            AttendanceContract.AttendanceEntryTable.COLUMN_NAME_SID, AttendanceContract.AttendanceEntryTable.COLUMN_NAME_PAXT,
            AttendanceContract.AttendanceEntryTable.COLUMN_NAME_LID, AttendanceContract.AttendanceEntryTable.COLUMN_NAME_SID);

    private static final String SQL_DELETE_ATTENDANCE_ENTRY = String.format("DROP TABLE IF EXISTS %s",
            AttendanceContract.AttendanceEntryTable.TABLE_NAME);

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
        db.execSQL(SQL_CREATE_ATTENDANCE_ENTRY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_STUDENTS);
        db.execSQL(SQL_DELETE_CLASS);
        db.execSQL(SQL_DELETE_LESSON);
        db.execSQL(SQL_DELETE_ROSTER_ENTRY);
        db.execSQL(SQL_DELETE_ATTENDANCE_ENTRY);
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
        String where = AttendanceContract.StudentTable.COLUM_NAME_SID + "=?";
        String[] args = {id};
        db.delete(AttendanceContract.StudentTable.TABLE_NAME, where, args);
        deleteStudentFromAllRosters(id);
        deleteStudentFromAllAttendanceEntries(id);
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

    public void deleteCourse(Course c) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = AttendanceContract.Course.COLUMN_NAME_CNAME + "=?" + " AND " +
                AttendanceContract.Course.COLUMN_NAME_SECTION + "=?" ;
        String[] args = {c.getName(),c.getSection()};
        db.delete(AttendanceContract.Course.TABLE_NAME, where,args );
        //deleting all attendance entries for that course
        deleteAllAttendanceEntriesForCourse(c);
        //deleting all lessons along for that course
        deleteAllCourseLessons(c);
        //deleting roster along with course
        deleteEntireRoster(c);

    }

    public void removeAllCourse() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(AttendanceContract.StudentTable.TABLE_NAME, null, null);
    }

    public Course retrieveSingleCourse(Course course){
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
            AttendanceContract.Course.COLUMN_NAME_CNAME,
            AttendanceContract.Course.COLUMN_NAME_SECTION
        };

        String where = AttendanceContract.Course.COLUMN_NAME_CNAME + "=" + course.getName() + " AND " +
                AttendanceContract.Course.COLUMN_NAME_SECTION + "=" + course.getSection();

        Cursor c = db.query(AttendanceContract.Course.TABLE_NAME, projection, where, null, null, null, null);
        c.moveToNext();

        String name = c.getString(c.getColumnIndex(AttendanceContract.Course.COLUMN_NAME_CNAME));
        String section = c.getString(c.getColumnIndex(AttendanceContract.Course.COLUMN_NAME_SECTION));

        return new Course(name,section);
    }

    public ArrayList<Course> retrieveAllCourses() {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
            AttendanceContract.Course.COLUMN_NAME_CNAME,
            AttendanceContract.Course.COLUMN_NAME_SECTION
        };

        String sorting = AttendanceContract.Course.COLUMN_NAME_CNAME + " ASC";

        Cursor c = db.query(AttendanceContract.Course.TABLE_NAME, projection, null, null, null, null, sorting);
        ArrayList<Course> courses = new ArrayList<>();
        while (c.moveToNext()) {

            String name = c.getString(c.getColumnIndex(AttendanceContract.Course.COLUMN_NAME_CNAME));
            String section = c.getString(c.getColumnIndex(AttendanceContract.Course.COLUMN_NAME_SECTION));
            courses.add(new Course(name, section));
        }
        c.close();
        return courses;
    }

    // Lesson Table helpers
    public void addLesson(Lesson l){

        SQLiteDatabase db = this.getWritableDatabase();

        // loading getting values from lesson object
        ContentValues values = new ContentValues();
        values.put(AttendanceContract.LessonTable.COLUMN_NAME_ClassName, l.getCourse().getName());
        values.put(AttendanceContract.LessonTable.COLUMN_NAME_ClassSection, l.getCourse().getSection());
        values.put(AttendanceContract.LessonTable.COLUMN_NAME_DATE, l.getDate());
        values.put(AttendanceContract.LessonTable.COLUMN_NAME_TIME, l.getTime());

        // inserting class lesson database
        db.insert(AttendanceContract.LessonTable.TABLE_NAME, null, values);
    }

    public Lesson retrieveSingleLesson(Lesson l){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = String.format("SELECT * FROM %s WHERE %s=? AND %s=? AND %s=? AND %s=?",
                AttendanceContract.LessonTable.TABLE_NAME, AttendanceContract.LessonTable.COLUMN_NAME_ClassName,
                AttendanceContract.LessonTable.COLUMN_NAME_ClassSection, AttendanceContract.LessonTable.COLUMN_NAME_DATE,
                AttendanceContract.LessonTable.COLUMN_NAME_TIME);

        String[] args = {l.getCourse().getName(), l.getCourse().getSection(), l.getDate(), l.getTime()};

        Cursor c = db.rawQuery(query,args);
        c.moveToNext();

        String className = c.getString(c.getColumnIndex(AttendanceContract.LessonTable.COLUMN_NAME_ClassName));
        String classSection = c.getString(c.getColumnIndex(AttendanceContract.LessonTable.COLUMN_NAME_ClassSection));
        int lid = c.getInt(c.getColumnIndex(AttendanceContract.LessonTable.COLUMN_NAME_LID));
        String date= c.getString(c.getColumnIndex(AttendanceContract.LessonTable.COLUMN_NAME_DATE));
        String time= c.getString(c.getColumnIndex(AttendanceContract.LessonTable.COLUMN_NAME_TIME));
        Course course = new Course(className,classSection);

        return new Lesson(date,time,course,lid);
    }

    public void deleteLesson(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = AttendanceContract.LessonTable.COLUMN_NAME_LID + "=" + id;
        db.delete(AttendanceContract.LessonTable.TABLE_NAME, where, null);
        deleteAllLessonAttendanceEntries(id);
    }

    public void deleteAllCourseLessons(Course c){
        SQLiteDatabase db = this.getWritableDatabase();
        String where = AttendanceContract.LessonTable.COLUMN_NAME_ClassName + "=?" + " AND "+
                AttendanceContract.LessonTable.COLUMN_NAME_ClassSection + "=?";
        String[] args = {c.getName(),c.getSection()};
        db.delete(AttendanceContract.LessonTable.TABLE_NAME, where, args);
    }

    public void removeAllLessons() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(AttendanceContract.LessonTable.TABLE_NAME, null, null);

    }

    public ArrayList<Lesson> retrieveAllLessons() {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                AttendanceContract.LessonTable.COLUMN_NAME_LID,
                AttendanceContract.LessonTable.COLUMN_NAME_ClassName,
                AttendanceContract.LessonTable.COLUMN_NAME_ClassSection,
                AttendanceContract.LessonTable.COLUMN_NAME_DATE,
                AttendanceContract.LessonTable.COLUMN_NAME_TIME,
        };

        String sorting = AttendanceContract.LessonTable.COLUMN_NAME_LID + " DESC";

        Cursor c = db.query(AttendanceContract.LessonTable.TABLE_NAME, projection, null, null, null, null, sorting);
        ArrayList<Lesson> lessons = new ArrayList<>();
        while (c.moveToNext()) {

            String className = c.getString(c.getColumnIndex(AttendanceContract.LessonTable.COLUMN_NAME_ClassName));
            String classSection = c.getString(c.getColumnIndex(AttendanceContract.LessonTable.COLUMN_NAME_ClassSection));
            int lid = c.getInt(c.getColumnIndex(AttendanceContract.LessonTable.COLUMN_NAME_LID));
            String date= c.getString(c.getColumnIndex(AttendanceContract.LessonTable.COLUMN_NAME_DATE));
            String time= c.getString(c.getColumnIndex(AttendanceContract.LessonTable.COLUMN_NAME_TIME));
            Course course = new Course(className,classSection);

            lessons.add(new Lesson(date,time,course,lid));
        }
        c.close();
        return lessons;
    }

    public void addRosterEntry(String sid, Course c){
        SQLiteDatabase db = this.getWritableDatabase();

        // loading getting values from lesson object
        ContentValues values = new ContentValues();
        values.put(AttendanceContract.RosterEntryTable.COLUMN_NAME_SID,sid);
        values.put(AttendanceContract.RosterEntryTable.COLUMN_NAME_ClassName,c.getName());
        values.put(AttendanceContract.RosterEntryTable.COLUMN_NAME_ClassSection,c.getSection());

        // inserting class lesson database
        db.insert(AttendanceContract.RosterEntryTable.TABLE_NAME, null, values);
    }

    public void addMultipleRosterEntry(ArrayList<Student> students, Course c){
        for(Student s: students){
            addRosterEntry(s.getId(),c);
        }
    }

    public ArrayList<Student> retrieveClassRoster(Course course){
        SQLiteDatabase db = this.getReadableDatabase();

        String myQuery = String.format("SELECT s.%s, s.%s  FROM %s s INNER JOIN  %s r ON s.%s=r.%s WHERE r.%s=? AND r.%s=?",
                AttendanceContract.StudentTable.COLUM_NAME_SID, AttendanceContract.StudentTable.COLUMN_NAME_SNAME,
                AttendanceContract.StudentTable.TABLE_NAME, AttendanceContract.RosterEntryTable.TABLE_NAME,
                AttendanceContract.StudentTable.COLUM_NAME_SID, AttendanceContract.RosterEntryTable.COLUMN_NAME_SID,
                AttendanceContract.RosterEntryTable.COLUMN_NAME_ClassName, AttendanceContract.RosterEntryTable.COLUMN_NAME_ClassSection);
        String[] props =  {course.getName(), course.getSection()};

        Cursor c = db.rawQuery(myQuery, props);

        ArrayList<Student> students = new ArrayList<>();
        while (c.moveToNext()) {
            String id = c.getString(c.getColumnIndex(AttendanceContract.StudentTable.COLUM_NAME_SID));
            String name = c.getString(c.getColumnIndex(AttendanceContract.StudentTable.COLUMN_NAME_SNAME));
            students.add(new Student(name, id));
        }
        c.close();
        return students;
    }

    public void deleteEntireRoster(Course c){
        SQLiteDatabase db = this.getWritableDatabase();
        String where = AttendanceContract.RosterEntryTable.COLUMN_NAME_ClassName + "=?" + " AND "
                + AttendanceContract.RosterEntryTable.COLUMN_NAME_ClassSection + "=?";
        String[] args = {c.getName(),c.getSection()};
        db.delete(AttendanceContract.RosterEntryTable.TABLE_NAME, where, args);
    }

    public void deleteStudentFromRoster(String id, Course c){
        SQLiteDatabase db = this.getWritableDatabase();
        String where = AttendanceContract.RosterEntryTable.COLUMN_NAME_ClassName + "=?" + " AND "
                + AttendanceContract.RosterEntryTable.COLUMN_NAME_ClassSection + "=?" + " AND "
                + AttendanceContract.RosterEntryTable.COLUMN_NAME_SID + "=?";
        String[] args = {c.getName(),c.getSection(),id};
        db.delete(AttendanceContract.RosterEntryTable.TABLE_NAME, where, args);
    }

    public void deleteStudentFromAllRosters(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        String where = AttendanceContract.RosterEntryTable.COLUMN_NAME_SID + "=?";
        String[] args = {id};
        db.delete(AttendanceContract.RosterEntryTable.TABLE_NAME, where, args);
    }

    public void addMultipleStudentAttendanceEntry(Lesson l){

        ArrayList<Student> students = retrieveClassRoster(l.getCourse());

        for(Student s: students){
            addStudentAttendanceEntry(s,l);
        }

    }

    public void addStudentAttendanceEntry(Student s, Lesson l){
        SQLiteDatabase db = this.getWritableDatabase();

        // loading getting values from lesson object
        ContentValues values = new ContentValues();
        values.put(AttendanceContract.AttendanceEntryTable.COLUMN_NAME_LID, l.getLid());
        values.put(AttendanceContract.AttendanceEntryTable.COLUMN_NAME_SID,s.getId());
        values.put(AttendanceContract.AttendanceEntryTable.COLUMN_NAME_PAXT, "Absent");

        // inserting class lesson database
        db.insert(AttendanceContract.AttendanceEntryTable.TABLE_NAME, null, values);
    }

    public ArrayList<Attendance> retrieveAllAttendance(int lid){
        SQLiteDatabase db = this.getReadableDatabase();

        String query = String.format("SELECT * FROM %s s INNER JOIN %s a ON s.%s=a.%s WHERE a.%s=?",
                AttendanceContract.StudentTable.TABLE_NAME,AttendanceContract.AttendanceEntryTable.TABLE_NAME,
                AttendanceContract.StudentTable.COLUM_NAME_SID, AttendanceContract.AttendanceEntryTable.COLUMN_NAME_SID,
                AttendanceContract.AttendanceEntryTable.COLUMN_NAME_LID);

        String[] args = {Integer.toString(lid)};

        Cursor c = db.rawQuery(query,args);
        ArrayList<Attendance> attendanceList = new ArrayList<>();
        while(c.moveToNext()){
            String name = c.getString(c.getColumnIndex(AttendanceContract.StudentTable.COLUMN_NAME_SNAME));
            String sid =  c.getString(c.getColumnIndex(AttendanceContract.StudentTable.COLUM_NAME_SID));
            String paxt = c.getString(c.getColumnIndex(AttendanceContract.AttendanceEntryTable.COLUMN_NAME_PAXT));

            Student s = new Student(name,sid);
            attendanceList.add(new Attendance(s,lid,paxt));
        }
        c.close();
        return attendanceList;
    }

    public void updateAttendance(String sid, int lid, String paxt){
        SQLiteDatabase db = this.getWritableDatabase();

        // loading getting values from lesson object
        ContentValues values = new ContentValues();
        values.put(AttendanceContract.AttendanceEntryTable.COLUMN_NAME_PAXT, paxt);

        String where = String.format("%s=? AND %s=?",
                AttendanceContract.AttendanceEntryTable.COLUMN_NAME_SID, AttendanceContract.AttendanceEntryTable.COLUMN_NAME_LID);

        String[] args = {sid,Integer.toString(lid)};


        // inserting class lesson database
        db.update(AttendanceContract.AttendanceEntryTable.TABLE_NAME,values,where,args);
        db.close();
    }

    public void deleteAllLessonAttendanceEntries(int lid){
        SQLiteDatabase db = this.getWritableDatabase();
        String where = AttendanceContract.AttendanceEntryTable.COLUMN_NAME_LID+ "=" + lid;
        db.delete(AttendanceContract.AttendanceEntryTable.TABLE_NAME, where, null);
    }

    public void deleteStudentFromAllAttendanceEntries(String sid){
        SQLiteDatabase db = this.getWritableDatabase();
        String where = AttendanceContract.AttendanceEntryTable.COLUMN_NAME_SID+ "=?";
        String[] args = {sid};
        db.delete(AttendanceContract.AttendanceEntryTable.TABLE_NAME, where, args);
    }

    public void deleteAllAttendanceEntriesForCourse(Course c){

        SQLiteDatabase db  = this.getReadableDatabase();
        String query = String.format("SELECT %s FROM %s WHERE %s=? AND %s=?",
                AttendanceContract.LessonTable.COLUMN_NAME_LID, AttendanceContract.LessonTable.TABLE_NAME,
                AttendanceContract.LessonTable.COLUMN_NAME_ClassName, AttendanceContract.LessonTable.COLUMN_NAME_ClassSection);
        String[] args = {c.getName(),c.getSection()};
        Cursor cursor = db.rawQuery(query,args);
        ArrayList<Integer> list = new ArrayList<>();
        while(cursor.moveToNext()){
           list.add(cursor.getInt(cursor.getColumnIndex(AttendanceContract.LessonTable.COLUMN_NAME_LID)));
        }

        for(int id: list){
            deleteAllLessonAttendanceEntries(id);
        }


    }

    public int[] getAttendanceStatusCounts(Student s){

        int[] results = new int[4];

        SQLiteDatabase db = this.getReadableDatabase();
        String query = String.format("SELECT * FROM %s WHERE %s=? AND %s=?",
                AttendanceContract.AttendanceEntryTable.TABLE_NAME, AttendanceContract.AttendanceEntryTable.COLUMN_NAME_SID,
                AttendanceContract.AttendanceEntryTable.COLUMN_NAME_PAXT);
        String[] args1 = {s.getId(),"Present"};
        String[] args2 = {s.getId(),"Absent"};
        String[] args3 = {s.getId(),"Excused"};
        String[] args4 = {s.getId(),"Tardy"};

        Cursor c1 = db.rawQuery(query,args1);
        Cursor c2 = db.rawQuery(query,args2);
        Cursor c3 = db.rawQuery(query,args3);
        Cursor c4 = db.rawQuery(query,args4);
        c1.moveToNext();
        c2.moveToNext();
        c3.moveToNext();
        c4.moveToNext();

        results[0] = c1.getCount();
        results[1] = c2.getCount();
        results[2] = c3.getCount();
        results[3] = c4.getCount();

        return results;

    }

    public ArrayList<Lesson> retrieveAllLessonsForCourse(Course c){
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                AttendanceContract.LessonTable.COLUMN_NAME_LID,
                AttendanceContract.LessonTable.COLUMN_NAME_ClassName,
                AttendanceContract.LessonTable.COLUMN_NAME_ClassSection,
                AttendanceContract.LessonTable.COLUMN_NAME_DATE,
                AttendanceContract.LessonTable.COLUMN_NAME_TIME,
        };

        String sorting = AttendanceContract.LessonTable.COLUMN_NAME_LID + " ASC";
        String selection = String.format("%s=? AND %s=?",AttendanceContract.LessonTable.COLUMN_NAME_ClassName, AttendanceContract.LessonTable.COLUMN_NAME_ClassSection);
        String[] args = {c.getName(),c.getSection()};
        Cursor cursor = db.query(AttendanceContract.LessonTable.TABLE_NAME, projection, selection, args, null, null, sorting);
        ArrayList<Lesson> lessons = new ArrayList<>();
        while (cursor.moveToNext()) {

            String className = cursor.getString(cursor.getColumnIndex(AttendanceContract.LessonTable.COLUMN_NAME_ClassName));
            String classSection = cursor.getString(cursor.getColumnIndex(AttendanceContract.LessonTable.COLUMN_NAME_ClassSection));
            int lid = cursor.getInt(cursor.getColumnIndex(AttendanceContract.LessonTable.COLUMN_NAME_LID));
            String date= cursor.getString(cursor.getColumnIndex(AttendanceContract.LessonTable.COLUMN_NAME_DATE));
            String time= cursor.getString(cursor.getColumnIndex(AttendanceContract.LessonTable.COLUMN_NAME_TIME));
            Course course = new Course(className,classSection);

            lessons.add(new Lesson(date,time,course,lid));
        }
        cursor.close();
        return lessons;
    }

    public ArrayList<AttendanceSheet> retrieveAllAttendanceSheets(Course c){
        ArrayList<AttendanceSheet> sheets = new ArrayList<>();
        ArrayList<Lesson> lessons = retrieveAllLessonsForCourse(c);
        for(Lesson l : lessons){
            sheets.add(new AttendanceSheet(retrieveAllAttendance(l.getLid()),c,l));
        }
        return sheets;
    }



}
