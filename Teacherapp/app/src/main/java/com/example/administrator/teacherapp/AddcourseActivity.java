package com.example.administrator.teacherapp;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.teacherapp.FunctionActivity;
import com.example.administrator.teacherapp.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;



import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
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

public class AddcourseActivity extends AppCompatActivity {

    boolean ADD_FLAG = false;
    String Tid;
    String coursename;
    String flag = "0";
    String courseno;

    EditText CoursenameEditText;
    EditText CourseNoEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcourse);
        Button add = (Button) findViewById(R.id.add);
        SharedPreferences sp = getSharedPreferences("Tid", Context.MODE_PRIVATE);
        Tid = sp.getString("TID","1");
        CoursenameEditText = (EditText) findViewById(R.id.coursename);
        CourseNoEditText = (EditText) findViewById(R.id.courseno);



        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                coursename = CoursenameEditText.getText().toString();
                courseno = CourseNoEditText.getText().toString();

                new AsyncTask<String, Void, Void>() {
                    @Override
                    protected Void doInBackground(String... params) {
                        try {
                            URL url = new URL("http://172.20.10.7:8080/Attendanceserver/AddCourseServlet?coursename=" + URLEncoder.encode(coursename,"UTF-8")+ "&"+"courseno=" + URLEncoder.encode(courseno,"UTF-8")+"&"+"Tid="+URLEncoder.encode(Tid,"UTF-8"));
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
                            flag = builder.toString();
                            if (flag.equals("1")) {
                                ADD_FLAG = true;
                            } else {
                                ADD_FLAG = false;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        if (ADD_FLAG) {
                            Toast toast = Toast.makeText(getApplicationContext(), "Successfully added!", Toast.LENGTH_SHORT);
                            toast.show();
                            Intent intent = new Intent(AddcourseActivity.this, FunctionActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(), "fail to add", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                }.execute();
            }
        });
    }
}

