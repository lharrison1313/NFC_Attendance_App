package com.nyit.attendanceapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class StudentsFragement extends Fragment {

    FloatingActionButton fab;
    ListView lv;
    ArrayList<Student> students;
    AttendanceDbHelper db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        db = new AttendanceDbHelper(getContext());
        students = db.retrieveAllStudents();
        return inflater.inflate(R.layout.fragment_students,container,false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("resumed");
        students = db.retrieveAllStudents();
        updatStudentList();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        updatStudentList();

        //creating add student button
        fab = getActivity().findViewById(R.id.floatingActionButtonStudents);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddStudentsActivity.class);
                getActivity().startActivity(intent);
            }
        });
    }


    public void updatStudentList(){
        if(students != null && students.size() != 0 ) {
            lv = getActivity().findViewById(R.id.studentList);
            StudentListAdapter adapter = new StudentListAdapter(getActivity(), students);
            lv.setAdapter(adapter);
        }
    }

}
