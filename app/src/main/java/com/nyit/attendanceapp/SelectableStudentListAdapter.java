package com.nyit.attendanceapp;

import android.content.Context;
import androidx.appcompat.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatCheckBox;

import java.util.ArrayList;
import java.util.List;

public class SelectableStudentListAdapter extends BaseAdapter {
    private Context mContext;
    private List<Student> mStudentList;
    private ArrayList<Student> selectedStudents;
    private AttendanceDbHelper db;

    SelectableStudentListAdapter(Context mContext){
        this.mContext = mContext;
        db = new AttendanceDbHelper(mContext);
        selectedStudents = new ArrayList<>();
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
            listItem = LayoutInflater.from(mContext).inflate(R.layout.selectable_student_bar,parent,false);

        //creating text view objects
        TextView name =  listItem.findViewById(R.id.selectableStudentName);
        TextView id =  listItem.findViewById(R.id.selectableStudentId);
        name.setText(mStudentList.get(position).getName());
        id.setText(mStudentList.get(position).getId());

        //creating checkbox object and listener
        AppCompatCheckBox check = listItem.findViewById(R.id.studentSelect);

        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //when box is checked it will add student to selected list
                if(isChecked){
                    selectedStudents.add(mStudentList.get(position));
                }
                //when unchecked they are removed
                else{
                    selectedStudents.remove(mStudentList.get(position));
                }
            }
        });

        return listItem;
    }

    public void populateStudentList(){
        mStudentList = db.retrieveAllStudents();
        this.notifyDataSetChanged();
    }

    public ArrayList<Student> getSelectedStudents(){
        return selectedStudents;
    }


}
