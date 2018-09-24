package com.example.administrator.attendancev1;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    boolean isUser = false;
    String studentnumber;
    String flag = "0";
    String password;
    SharedPreferences sp;
    EditText studentnumberEditText;
    EditText passwordEditText;
    final ArrayList<String> blacklist = new ArrayList<String>();
    ArrayList<String> list = new ArrayList<String>();
    StringBuilder warning = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        blacklist.add("bulb.virtual");
        blacklist.add("com.kollway.android.mocklocation");
        blacklist.add("net.superliujian.mockgps");
        blacklist.add("com.templa.mockloc");
        blacklist.add("com.virtualdroid.loc");
        blacklist.add("com.wechathelper");
        blacklist.add("com.lexa.fakegps");
        blacklist.add("com.yunyou");
        blacklist.add("com.blogspot.newapphorizons.fakegps");
        blacklist.add("com.huichongzi.locationmocker");
        blacklist.add("com.hawkmobile.locationmonitor");
        blacklist.add("com.wifi99.android.locationcheater");
        blacklist.add("com.wechatanywhere");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button submit = (Button) findViewById(R.id.submit);
        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        studentnumberEditText = (EditText) findViewById(R.id.studentnumber);
        passwordEditText = (EditText) findViewById(R.id.password);
        studentnumberEditText.setText(sp.getString("STUDENTNO", ""));
        passwordEditText.setText(sp.getString("PASSWORD", ""));

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<MyAppInfo> appInfos =ApkTool.scanLocalInstallAppList(LoginActivity.this.getPackageManager()); ;
                list.clear();
                appInfos.clear();
                appInfos = ApkTool.scanLocalInstallAppList(LoginActivity.this.getPackageManager());
                warning.delete(0,warning.length());
                for(int i=0;i<appInfos.size();i++){
                    for(int a=0;a<blacklist.size();a++){
                        if(appInfos.get(i).getAppName().equals(blacklist.get(a))){
                            list.add(appInfos.get(i).getAppName());
                        }
                    }
                }
                for(int i=0;i<list.size();i++){
                    warning.append(list.get(i)+"\n");
                }
                studentnumber = studentnumberEditText.getText().toString();
                password = passwordEditText.getText().toString();
                if(!warning.toString().equals("")){
                    Toast toast = Toast.makeText(getApplicationContext(), "Please uninstall following illegal location app and try again.\n"+warning.toString(), Toast.LENGTH_SHORT);
                    toast.show();
                }else{
                new AsyncTask<String, Void, Void>() {
                    @Override
                    protected Void doInBackground(String... params) {
                        try {
                            URL url = new URL("http://172.20.10.7:8080/Attendanceserver/loginServlet?studentnumber=" + URLEncoder.encode(studentnumber,"UTF-8")+ "&"+"password=" + URLEncoder.encode(password,"UTF-8"));
                            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                            connection.setRequestMethod("GET");
                            connection.setReadTimeout(50000);
                            connection.setConnectTimeout(50000);
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
                            //JSONObject root = new JSONObject(builder.toString());
                            //JSONArray array = root.getJSONArray("result");
                            //JSONObject object = array.getJSONObject(0);
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
                            SharedPreferences sp2 = getSharedPreferences("location",Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor2=sp2.edit();
                            editor2.putString("code","0");
                            editor2.commit();
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("STUDENTNO", studentnumber);
                            editor.putString("PASSWORD", password);
                            editor.commit();
                            Intent intent = new Intent(LoginActivity.this, ShowcourseActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(), "Fail to login.", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                }.execute();}
            }
        });
    }
}
