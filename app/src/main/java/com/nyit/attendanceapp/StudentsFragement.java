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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Adding students to list view
        students = new ArrayList<>();
        students.add(new Student("Luke","1204951"));
        students.add(new Student("Jim","1205141"));
        students.add(new Student("Joe","1203273"));
        students.add(new Student("Eric","1265436"));
        students.add(new Student("Bob","1255268"));
        students.add(new Student("Stefan","1266905"));
        students.add(new Student("Maria","1203321"));


        return inflater.inflate(R.layout.fragment_students,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //creating student list
        lv = getActivity().findViewById(R.id.studentList);
        StudentListAdapter adapter = new StudentListAdapter(getActivity(),students);
        lv.setAdapter(adapter);

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

}
