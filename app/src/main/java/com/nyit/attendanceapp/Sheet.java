package com.nyit.attendanceapp;

public class Sheet {
    private String name;
    private String date;

    public Sheet(String name, String date){
        this.name = name;
        this.date = date;
    }

    public String getName() {return name;}
    public String getDate()  {return date;}
}
