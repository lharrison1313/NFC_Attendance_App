package com.nyit.attendanceapp;

public class Course {
    private String name;
    private String section;
    private int cid;

    public Course(String name, String section, int cid){
        this.name = name;
        this.section = section;
        this.cid = cid;
    }

    public Course(String name, String section){
        this.name = name;
        this.section = section;
    }

    public String getName() {return name;}

    public String getSection() {return section;}

    public int getCid() {return cid;}


}
