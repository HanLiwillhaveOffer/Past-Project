package com.example.administrator.attendancev1;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.view.LayoutInflater;

import java.util.List;
import android.content.Context;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/3/1.
 */
public class CourseAdapter extends ArrayAdapter<Course> {
    private int resourceId;
    public CourseAdapter(Context context,int textViewResourceID,List<Course> objects){
        super(context,textViewResourceID,objects);
        resourceId = textViewResourceID;
    }
    @Override
public View getView(int position, View converView, ViewGroup parent){
    Course course = getItem(position);
    View view = LayoutInflater.from(getContext()).inflate(resourceId,null);
    TextView coursename = (TextView)view.findViewById(R.id.coursename);
    TextView courseNo = (TextView)view.findViewById(R.id.courseNo);
    coursename.setText(course.getName());
    courseNo.setText(course.getNo());
    return view;
}
}
