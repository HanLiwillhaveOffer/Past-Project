package com.example.administrator.teacherapp;

/**
 * Created by Administrator on 2017/4/6.
 */
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.view.LayoutInflater;

import java.util.List;
import android.content.Context;
import android.widget.TextView;

import org.w3c.dom.Text;

public class RecordAdapter extends ArrayAdapter<Record> {
    private int resourceId;
    public RecordAdapter(Context context,int textViewResourceID,List<Record> objects){
        super(context,textViewResourceID,objects);
        resourceId = textViewResourceID;
    }
    @Override
    public View getView(int position, View converView, ViewGroup parent){
        Record record = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,null);
        TextView studentname = (TextView)view.findViewById(R.id.studentname);
        TextView studentno = (TextView)view.findViewById(R.id.studentno);
        studentname.setText(record.getStudentname());
        studentno.setText(record.getStudentno());
        return view;
    }
}