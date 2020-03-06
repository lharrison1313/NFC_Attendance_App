package com.nyit.attendanceapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class StudentListAdapter extends BaseAdapter {
    private Context mContext;
    private List<Student> mStudentList;

    StudentListAdapter(Context mContext, List<Student> mStudentList){
        this.mContext = mContext;
        this.mStudentList = mStudentList;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.student_bar,parent,false);

        TextView name =  listItem.findViewById(R.id.studentName);
        TextView id =  listItem.findViewById(R.id.studentId);

        name.setText(mStudentList.get(position).getName());
        id.setText(mStudentList.get(position).getId());

        
        return listItem;
    }
}
