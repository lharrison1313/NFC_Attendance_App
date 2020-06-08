package com.nyit.attendanceapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ClassInfoActivity extends Activity {
    private String name;
    private String section;
    private DeleteableStudentListAdapter adapter;
    private Course course;
    private AttendanceDbHelper db;
    private Dialog deleteDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.class_info);
        db = new AttendanceDbHelper(this);
        configureClassHeader();
        configureBackButton();
        course = new Course(name,section);
        configureStudentList();
        configureDeleteDialog();
        configureDeleteButton();
        configureAddStudentButton();
        configureExportButton();

    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.populateStudentList();
    }

    private void configureClassHeader(){
        name = getIntent().getStringExtra("name");
        section = getIntent().getStringExtra("section");

        TextView className = findViewById(R.id.infoClassName);
        TextView classSection = findViewById(R.id.infoClassSection);
        className.setText(name);
        classSection.setText(section);
    }

    private void configureBackButton(){
        AppCompatButton acb = findViewById(R.id.classInfoDone);
        acb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void configureStudentList(){
        ListView lv = findViewById(R.id.classInfoStudentList);
        adapter = new DeleteableStudentListAdapter(this,course);
        lv.setAdapter(adapter);
    }

    private void configureDeleteButton(){
        AppCompatImageButton acib = findViewById(R.id.infoDeleteClass);
        acib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.show();
            }
        });
    }

    private void configureDeleteDialog(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Confirm Delete").setMessage("Are you sure you want to delete this Class?");
        builder1.setPositiveButton("confirm",new Dialog.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db.deleteCourse(course);
                finish();

            }
        });
        builder1.setNegativeButton("cancel",new Dialog.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        deleteDialog = builder1.create();
    }

    private void configureAddStudentButton(){
        AppCompatButton acb = findViewById(R.id.addStudentButton);
        final Intent i = new Intent(this, AddStudentsToCourseActivity.class);
        acb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.putExtra("student_list",adapter.getStudentList());
                i.putExtra("name",name);
                i.putExtra("section",section);
                startActivity(i);
            }
        });
    }

    private void configureExportButton(){
        AppCompatButton acb = findViewById(R.id.exportAllAttendance);
        acb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportAttendanceSheet();
            }
        });
    }

    private void exportAttendanceSheet(){
        String fileName = String.format("%s_%s.csv",name,section);
        StringBuilder sb = new StringBuilder();
        ArrayList<AttendanceSheet> sheets = db.retrieveAllAttendanceSheets(course);
        sb.append("SID");
        //adding class dates
        for(AttendanceSheet sheet: sheets){
            sb.append(String.format(",%s %s",sheet.getLesson().getDate(),sheet.getLesson().getTime()));
        }
        //getting list of student ids
        ArrayList<String> IDList = extractSID(sheets);
        //appending all students attendance record by id
        for(String id : IDList){
            //loops through student ID
            sb.append(String.format("\n%s",id));
            for(AttendanceSheet sheet: sheets){
                String paxt = ",NA";
                for(Attendance a: sheet.getAttendances()) {
                    if (id.equals(a.getStudent().getId())) {
                        paxt = ","+a.getPaxt();
                        break;
                    }
                }
                sb.append(paxt);
            }
        }

        try{
            //saving file
            FileOutputStream out = openFileOutput(fileName, Context.MODE_PRIVATE);
            out.write(sb.toString().getBytes());
            out.close();

            //exporting file
            Context context = getApplicationContext();
            File file = new File(getFilesDir(),fileName);
            Uri path = FileProvider.getUriForFile(context,"com.nyit.attendanceapp.fileprovider",file);
            Intent fileIntent = new Intent(Intent.ACTION_SEND);
            fileIntent.setType("text/csv");
            fileIntent.putExtra(Intent.EXTRA_SUBJECT,"Attendance Sheet: "+course.getName()+" "+course.getSection());
            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            fileIntent.putExtra(Intent.EXTRA_STREAM,path);
            startActivity(fileIntent);
        }
        catch (Exception e){
            System.out.println(e);
        }

    }

    private ArrayList<String> extractSID(ArrayList<AttendanceSheet> sheets){
        ArrayList<String> SIDs = new ArrayList<>();
        for(AttendanceSheet sheet: sheets){
            for(Attendance a: sheet.getAttendances()){
                if(!SIDs.contains(a.getStudent().getId())){
                    SIDs.add(a.getStudent().getId());
                }
            }
        }
        SIDs.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return new BigDecimal(o1).compareTo(new BigDecimal(o2));
            }
        });
        return SIDs;
    }



}
