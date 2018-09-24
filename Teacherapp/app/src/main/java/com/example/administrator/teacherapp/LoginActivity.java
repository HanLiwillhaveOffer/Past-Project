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

public class LoginActivity extends AppCompatActivity {

    boolean isUser = false;
    String Tid;
    String Teachername;
    String flag = "0";
    String password;
    SharedPreferences sp;
    EditText TeachernameEditText;
    EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button submit = (Button) findViewById(R.id.submit);
        Button change = (Button)findViewById(R.id.change);
        sp = this.getSharedPreferences("Tid", Context.MODE_PRIVATE);
        TeachernameEditText = (EditText) findViewById(R.id.Terchername);
        passwordEditText = (EditText) findViewById(R.id.password);
        TeachernameEditText.setText(sp.getString("Tname",""));
        passwordEditText.setText(sp.getString("password",""));
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ChangePWActivity.class);
                startActivity(intent);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Teachername = TeachernameEditText.getText().toString();
                password = passwordEditText.getText().toString();

                new AsyncTask<String, Void, Void>() {
                    @Override
                    protected Void doInBackground(String... params) {
                        try {
                            URL url = new URL("http://172.20.10.7:8080/Attendanceserver/TeacherLoginServlet?Teachername=" + URLEncoder.encode(Teachername,"UTF-8")+ "&"+"password=" + URLEncoder.encode(password,"UTF-8"));
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
                            JSONObject f = array.getJSONObject(0);
                            JSONObject Teacher = array.getJSONObject(1);
                            flag = f.getString("flag");
                            Tid = Teacher.getString("Tid");
                            if (flag.equals("1")) {
                                isUser = true;
                            } else {
                                isUser = false;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        if (isUser) {
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("TID", Tid);
                            editor.putString("Tname",Teachername);
                            editor.putString("password",password);
                            editor.commit();
                            Intent intent = new Intent(LoginActivity.this, FunctionActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(), "Fail to log in.", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                }.execute();
            }
        });
    }
}
