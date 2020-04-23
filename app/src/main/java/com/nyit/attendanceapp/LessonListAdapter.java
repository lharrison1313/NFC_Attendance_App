package com.nyit.attendanceapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;


public class LessonListAdapter<mContext> extends BaseAdapter {


    private Context mContext;
    private List<Lesson> mLessonList;
    private AttendanceDbHelper db;

    //Constructor
    public LessonListAdapter(Context m)
    {
        this.mContext = m;
        db = new AttendanceDbHelper(mContext);
        populateLessonList();
    }

    @Override
    //getCount
    public int getCount()
    {
        return mLessonList.size();
    }

    //getItem
    public Object getItem(int position)
    {
        return mLessonList.get(position);
    }

    //getID

    public long getID(int position)
    {
        return position;
    }


    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    //getView

    public View getView (final int position, View convertView, ViewGroup parent)
    {
        View listItem;
        listItem= convertView;

        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.sheet_bar,parent,false);

        TextView name =  listItem.findViewById(R.id.SheetBarClassName);
        TextView section =  listItem.findViewById(R.id.SheetBarSection);
        TextView datetime = listItem.findViewById(R.id.SheetBarDate);

        name.setText(mLessonList.get(position).getCourse().getName());
        section.setText(mLessonList.get(position).getCourse().getSection());
        String date = mLessonList.get(position).getDate();
        String time = mLessonList.get(position).getTime();
        datetime.setText(String.format("%s %s", date, time));

        //setting onclick listener


        return listItem;


    }

    public void populateLessonList(){
        mLessonList = db.retrieveAllLessons();
        this.notifyDataSetChanged();
    }

}
