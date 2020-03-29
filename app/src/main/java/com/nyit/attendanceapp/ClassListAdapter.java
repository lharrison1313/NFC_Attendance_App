package com.nyit.attendanceapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;




public class ClassListAdapter<mContext> extends BaseAdapter {


    private Context mContext;
    private List<Class> mClassList;

    //Constructor
    public ClassListAdapter (Context m, List<Class> mList )
    {
        this.mContext = m;
        this.mClassList = mList;
    }

    @Override
    //getCount
    public int getCount()
    {
        return mClassList.size();
    }

    //getItem
    public Object getItem(int position)
    {
        return mClassList.get(position);
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

    public View getView (int position, View convertView, ViewGroup parent)
    {
        View listItem;
        listItem= convertView;

        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.class_bar,parent,false);

        TextView name =  listItem.findViewById(R.id.className);
        TextView id =  listItem.findViewById(R.id.classSection);

        name.setText(mClassList.get(position).getName());
        id.setText(mClassList.get(position).getSection());

        return listItem;


    }
}
