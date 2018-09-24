package com.example.administrator.attendancev1;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class ShowcourseActivity extends AppCompatActivity {


    int tid;
    String flag = "0";
    EditText studentnumberEditText;
    EditText passwordEditText;
    double Tlatitude;
    double Tlongtitude;
    double latitude;
    double longtitude;
    String Cid;
    String code;
    private ArrayList<Course> courseList = new ArrayList<Course>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showcourse);
        Button button = (Button) findViewById(R.id.searchcourse);




        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                new AsyncTask<String, Void, Void>() {
                    @Override
                    protected Void doInBackground(String... params) {
                        try {
                            courseList.clear();
                            URL url = new URL("http://172.20.10.7:8080/Attendanceserver/CourseServlet");
                            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                            connection.setRequestMethod("GET");
                            connection.setReadTimeout(50000);
                            connection.setConnectTimeout(5000);
                            InputStream is = connection.getInputStream();
                            InputStreamReader isr = new InputStreamReader(is, "utf-8");
                            BufferedReader br = new BufferedReader(isr);
                            String line;
                            StringBuilder builder = new StringBuilder();
                            while ((line = br.readLine()) != null) {
                                builder.append(line);
                            }
                            br.close();
                            isr.close();
                            JSONArray array = new JSONArray(builder.toString());

                            for(int i=0;i<array.length();i++){
                            JSONObject course = (JSONObject) array.get(i);
                            Course c = new Course(course.getString("name"),course.getString("No"),course.getString("ID"));
                                courseList.add(c);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        if (courseList.isEmpty()){
                            Toast toast = Toast.makeText(getApplicationContext(), "No available course.", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        else{
                        ListView listView = (ListView) findViewById(R.id.showcourses);
                        CourseAdapter adapter = new CourseAdapter(ShowcourseActivity.this,R.layout.course_item,courseList);
                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Course course = courseList.get(position);
                                SharedPreferences sp = getSharedPreferences("courseinfo",Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor=sp.edit();
                                editor.putString("coursename", course.getName());
                                editor.putString("courseno", course.getNo());
                                editor.putString("Cid",course.getCid());
                                Cid = course.getCid();
                                editor.commit();
                                Intent intent = new Intent(ShowcourseActivity.this, SigninActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });}


                    }
                }.execute();
            }
        });
    }
    private double GetDistance(double Latitude1,double Longitude1,double Latitude2,double Longitude2)
    {
        float[] res=new float[1];
        Location.distanceBetween(Latitude1, Longitude1, Latitude2, Longitude2, res);
        return res[0];
    }

}