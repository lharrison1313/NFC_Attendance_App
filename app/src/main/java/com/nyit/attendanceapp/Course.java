package com.nyit.attendanceapp;

public class Course {
    private String name;
    private String section;
    private String cid;

    public Course(String name, String section, String cid){
        this.name = name;
        this.section = section;
        this.cid = cid;
    }

    public String getName() {return name;}

    public String getSection() {return section;}

    public String getCid() {return cid;}


}
