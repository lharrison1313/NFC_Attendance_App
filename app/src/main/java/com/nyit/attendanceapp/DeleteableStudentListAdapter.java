package com.nyit.attendanceapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageButton;

import java.util.List;

public class DeleteableStudentListAdapter extends BaseAdapter {
    private Context mContext;
    private List<Student> mStudentList;
    private AttendanceDbHelper db;
    private Course course;

    DeleteableStudentListAdapter(Context mContext, Course c ){
        this.mContext = mContext;
        db = new AttendanceDbHelper(mContext);
        course = c;
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
            listItem = LayoutInflater.from(mContext).inflate(R.layout.deleteable_student_bar,parent,false);

        //creating text view objects
        TextView name =  listItem.findViewById(R.id.deletableStudentName);
        TextView id =  listItem.findViewById(R.id.deletableStudentId);
        name.setText(mStudentList.get(position).getName());
        id.setText(mStudentList.get(position).getId());

        //creating delete dialog
        AlertDialog.Builder builder1 = new AlertDialog.Builder(parent.getContext());
        builder1.setTitle("Confirm Delete").setMessage("Are you sure you want to remove this student from the class roster?");
        builder1.setPositiveButton("confirm",new Dialog.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db.deleteStudentFromRoster(mStudentList.get(position).getId(),course);
                populateStudentList();
                dialog.dismiss();
            }
        });
        builder1.setNegativeButton("cancel",new Dialog.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final Dialog deleteDialog = builder1.create();

        //creating delete button and listener
        AppCompatImageButton acib = listItem.findViewById(R.id.deleteStudentFromRoster);
        acib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.show();
            }
        });


        return listItem;
    }

    public void populateStudentList(){
        mStudentList = db.retrieveClassRoster(course);
        this.notifyDataSetChanged();
    }



}
