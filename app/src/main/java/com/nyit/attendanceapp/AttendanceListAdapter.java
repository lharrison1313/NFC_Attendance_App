package com.nyit.attendanceapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AttendanceListAdapter<mContext> extends BaseAdapter {


    private Context mContext;
    private ArrayList<Attendance> mAttendanceList;
    private int lid;
    private AttendanceDbHelper db;

    //Constructor
    public AttendanceListAdapter(Context m, int lid)
    {
        this.mContext = m;
        this.lid = lid;
        db = new AttendanceDbHelper(mContext);
        populateAttendanceList();
    }

    @Override
    //getCount
    public int getCount()
    {
        return mAttendanceList.size();
    }

    //getItem
    public Object getItem(int position)
    {
        return mAttendanceList.get(position);
    }

    //getID

    public long getID(int position)
    {
        return position;
    }


    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    //getView

    public View getView (final int position, View convertView, ViewGroup parent)
    {
        View listItem;
        listItem= convertView;

        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.attendance_student_bar,parent,false);

        TextView name = listItem.findViewById(R.id.studentNameAttendance);
        TextView sid = listItem.findViewById(R.id.studentIdAttendance);

        name.setText(mAttendanceList.get(position).getStudent().getName());
        sid.setText(mAttendanceList.get(position).getStudent().getId());

        RadioGroup rg = listItem.findViewById(R.id.radio_group);
        final RadioButton absent = listItem.findViewById(R.id.Absent);
        final RadioButton present = listItem.findViewById(R.id.Present);
        final RadioButton tardy = listItem.findViewById(R.id.Tardy);
        final RadioButton excused = listItem.findViewById(R.id.Excused);

        switch(mAttendanceList.get(position).getPaxt()){
            case "Absent":
                absent.setChecked(true);
                break;
            case "Present":
                present.setChecked(true);
                break;
            case "Tardy":
                tardy.setChecked(true);
                break;
            case "Excused":
                excused.setChecked(true);
                break;
        }

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                String paxt;
                if(checkedId == absent.getId()){
                    paxt = "Absent";
                }
                else if(checkedId == present.getId()){
                    paxt = "Present";
                }
                else if(checkedId == tardy.getId()){
                    paxt = "Tardy";
                }
                else{
                    paxt = "Excused";
                }

                String sid = mAttendanceList.get(position).getStudent().getId();
                int lid = mAttendanceList.get(position).getLid();
                db.updateAttendance(sid,lid,paxt);
            }
        });



        return listItem;


    }

    public void populateAttendanceList(){
        mAttendanceList = db.retrieveAllAttendance(lid);
        this.notifyDataSetChanged();
    }

    public boolean setPresent(String sid){
        for(Attendance a: mAttendanceList){
            if(a.getStudent().getId().equals(sid)){
                db.updateAttendance(sid,lid,"Present");
                populateAttendanceList();
                return true;
            }
        }
        return false;
    }

}
