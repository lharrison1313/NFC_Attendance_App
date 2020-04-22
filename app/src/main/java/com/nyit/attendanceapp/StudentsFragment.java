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


public class StudentsFragment extends Fragment {

    FloatingActionButton fab;
    ListView lv;
    StudentListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_students,container,false);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.populateStudentList();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lv = getActivity().findViewById(R.id.studentList);
        adapter = new StudentListAdapter(getActivity());
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
