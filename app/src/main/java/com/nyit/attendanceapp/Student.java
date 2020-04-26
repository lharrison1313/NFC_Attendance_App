package com.nyit.attendanceapp;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class Student implements Serializable {
    private String name;
    private String id;

    public Student(String name, String id){
        this.name = name;
        this.id = id;
    }

    public String getName(){
        return name;
    }
    public String getId(){
        return id;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        Student s = (Student) obj;
        String name = s.getName();
        String id = s.getId();
        if(this.id.equals(id) && this.name.equals(name)){
            return true;
        }
        else{
            return false;
        }
    }
}
