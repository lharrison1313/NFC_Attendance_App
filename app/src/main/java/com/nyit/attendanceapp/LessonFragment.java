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

public class LessonFragment extends Fragment{

    FloatingActionButton fab;
    LessonListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sheets,container,false);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.populateLessonList();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //creating add sheets button
        fab = getActivity().findViewById(R.id.floatingActionButtonSheets);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent (getActivity(), AddSheetActivity.class);
                getActivity().startActivity(intent);
            }
        });

        ListView lv = getActivity().findViewById(R.id.SheetList);
        adapter = new LessonListAdapter(getContext());
        lv.setAdapter(adapter);




    }


}
