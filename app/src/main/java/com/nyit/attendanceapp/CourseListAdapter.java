package com.nyit.attendanceapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;




public class CourseListAdapter<mContext> extends BaseAdapter {


    private Context mContext;
    private List<Course> mCourseList;
    private AttendanceDbHelper db;

    //Constructor
    public CourseListAdapter(Context m)
    {
        this.mContext = m;
        db = new AttendanceDbHelper(mContext);
        populateCourseList();
    }

    @Override
    //getCount
    public int getCount()
    {
        return mCourseList.size();
    }

    //getItem
    public Object getItem(int position)
    {
        return mCourseList.get(position);
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
            listItem = LayoutInflater.from(mContext).inflate(R.layout.class_bar,parent,false);

        TextView name =  listItem.findViewById(R.id.className);
        TextView id =  listItem.findViewById(R.id.classSection);

        name.setText(mCourseList.get(position).getName());
        id.setText(mCourseList.get(position).getSection());

        //setting onclick listener
//        listItem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mContext, );
//                intent.putExtra("name",mCourseList.get(position).getName());
//                intent.putExtra("section",mCourseList.get(position).getSection());
//                mContext.startActivity(intent);
//            }
//        });

        return listItem;


    }

    public void populateCourseList(){
        mCourseList = db.retrieveAllCourses();
        this.notifyDataSetChanged();
    }

}
