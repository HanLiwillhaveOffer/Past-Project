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

public class ChangePWActivity extends AppCompatActivity {
    EditText usernameEditText;
    EditText oldpasswordEditText;
    EditText newpasswordEditText;
    String username;
    String oldpassword;
    String newpassword;
    boolean isUser = false;
    String flag = "0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pw);
        Button button = (Button) findViewById(R.id.submit);
        usernameEditText = (EditText) findViewById(R.id.username);
        oldpasswordEditText = (EditText) findViewById(R.id.oldpassword);
        newpasswordEditText = (EditText) findViewById(R.id.newpassword);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = usernameEditText.getText().toString();
                oldpassword = oldpasswordEditText.getText().toString();
                newpassword = newpasswordEditText.getText().toString();

                new AsyncTask<String, Void, Void>() {
                    @Override
                    protected Void doInBackground(String... params) {
                        try {
                            URL url = new URL("http://172.20.10.7:8080/Attendanceserver/ChangePWServlet?username=" + URLEncoder.encode(username,"UTF-8")+ "&"+"oldpassword=" + URLEncoder.encode(oldpassword,"UTF-8")+"&newpassword="+URLEncoder.encode(newpassword,"UTF-8"));
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
                            Toast toast = Toast.makeText(getApplicationContext(), "Change succeefully.", Toast.LENGTH_SHORT);
                            toast.show();
                            Intent intent = new Intent(ChangePWActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(), "Fail to change, please try again.", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                }.execute();
            }
        });
    }
}
