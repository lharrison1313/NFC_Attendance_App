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
    private ArrayList<Integer> patx;
    private int lid;
    private AttendanceDbHelper db;

    //Constructor
    public AttendanceListAdapter(Context m, int lid)
    {
        this.mContext = m;
        this.lid = lid;
        db = new AttendanceDbHelper(mContext);
        patx = new ArrayList<>();
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

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                String paxt;
                if(checkedId == absent.getId()){
                    paxt = "Absent";
                    patx.set(position,2);
                }
                else if(checkedId == present.getId()){
                    paxt = "Present";
                    patx.set(position,1);
                }
                else if(checkedId == tardy.getId()){
                    paxt = "Tardy";
                    patx.set(position,3);
                }
                else{
                    paxt = "Excused";
                    patx.set(position,4);
                }

                String sid = mAttendanceList.get(position).getStudent().getId();
                int lid = mAttendanceList.get(position).getLid();
                db.updateAttendance(sid,lid,paxt);
            }
        });

        switch(patx.get(position)){
            case 2:
                absent.setChecked(true);
                break;
            case 1:
                present.setChecked(true);
                break;
            case 3:
                tardy.setChecked(true);
                break;
            case 4:
                excused.setChecked(true);
                break;
        }



        return listItem;


    }

    public void populateAttendanceList(){
        mAttendanceList = db.retrieveAllAttendance(lid);
        this.notifyDataSetChanged();
        patx.clear();
        for(Attendance a: mAttendanceList){
            switch(a.getPaxt()){
                case "Absent":
                    patx.add(2);
                    break;
                case "Present":
                    patx.add(1);
                    break;
                case "Tardy":
                    patx.add(3);
                    break;
                case "Excused":
                    patx.add(4);
                    break;
            }
        }
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
