package com.nyit.attendanceapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageButton;

import java.util.List;

public class StudentListAdapter extends BaseAdapter {
    private Context mContext;
    private List<Student> mStudentList;
    private AttendanceDbHelper db;

    StudentListAdapter(Context mContext){
        this.mContext = mContext;
        db = new AttendanceDbHelper(mContext);
        populateStudentList();
    }

    @Override
    public int getCount() {
       return mStudentList.size();
    }

    @Override
    public Object getItem(int position) {
        return mStudentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.student_bar,parent,false);

        //creating text view objects
        TextView name =  listItem.findViewById(R.id.studentName);
        TextView id =  listItem.findViewById(R.id.studentId);
        name.setText(mStudentList.get(position).getName());
        id.setText(mStudentList.get(position).getId());

        //setting onclick listener
        listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, StudentInfoActivity.class);
                intent.putExtra("name",mStudentList.get(position).getName());
                intent.putExtra("id",mStudentList.get(position).getId());
                mContext.startActivity(intent);
            }
        });

//        //setting up delete dialog
//        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
//        builder1.setTitle("Confirm Delete").setMessage("Are you sure you want to delete this student?");
//        builder1.setPositiveButton("confirm",new Dialog.OnClickListener(){
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                AttendanceDbHelper db = new AttendanceDbHelper(mContext);
//                db.deleteStudent(mStudentList.get(position).getId());
//                populateStudentList();
//            }
//        });
//        builder1.setNegativeButton("cancel",new Dialog.OnClickListener(){
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        final AlertDialog deleteDialog = builder1.create();

        //setting up edit dialog

//        //setting delete student button onclick
//        AppCompatImageButton deleteButton = listItem.findViewById(R.id.deleteStudent);
//        deleteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                deleteDialog.show();
//            }
//        });

        
        return listItem;
    }

    public void populateStudentList(){
        mStudentList = db.retrieveAllStudents();
        this.notifyDataSetChanged();
    }


}
