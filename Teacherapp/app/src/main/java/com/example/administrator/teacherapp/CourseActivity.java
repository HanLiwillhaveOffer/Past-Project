package com.example.administrator.teacherapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Timer;
import java.util.TimerTask;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;


public class CourseActivity extends AppCompatActivity {
    public LocationClient locationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    boolean processflag = false;
    boolean loc = true;
    private ProgressBar progressBar;
    String code;
    String flag = "0";
    String studentno;
    String Coursename;
    String Courseno;
    String Cid;
    String state;
    String Tid;
    Double latitude;
    Double longtitude;
    TextView coursename;
    TextView courseno;
    TextView coursestate;
    private static final int UPDATE_OEPN = 1;
    private static final int UPDATE_CLOSE = 0;
    private static final int SEND_LOCATION = 2;
    Timer timer;

    private void PlayTimer() {
        timer= new Timer();
        timer.schedule(new TimerTask() {
        @Override
        public void run () {
            {   if(state.equals("Open for attendance")){
                Message message = new Message();
                message.what = UPDATE_CLOSE;
                handler.sendMessage(message);}
            }

        }
    },120000);
}



    //private TextView loc = (TextView)findViewById(R.id.location);
    Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case UPDATE_OEPN:
                    coursestate.setText("Open for attendance");
                    timer = new Timer();
                    timer.cancel();
                    PlayTimer();
                    break;
                case UPDATE_CLOSE:
                    progressBar.setVisibility(View.VISIBLE);
                    new AsyncTask<String, Void, Void>() {
                        @Override
                        protected Void doInBackground(String... params) {
                            try {
                                URL url = new URL("http://172.20.10.7:8080/Attendanceserver/ProcessCourseServlet?Cid=" + URLEncoder.encode(Cid,"UTF-8")+ "&"+"signal=0");
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
                                //JSONObject root = new JSONObject(builder.toString());
                                //JSONArray array = root.getJSONArray("result");
                                //JSONObject object = array.getJSONObject(0);
                                flag = builder.toString();
                                if (flag.equals("close successfully")) {
                                    processflag = true;
                                } else {
                                    processflag = false;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            progressBar.setVisibility(View.GONE);
                            super.onPostExecute(aVoid);
                            if (processflag) {

                                Toast toast = Toast.makeText(getApplicationContext(), flag, Toast.LENGTH_SHORT);
                                toast.show();
                                coursestate.setText("Closed for attendance");
                                state = "Closed for attendance";
                            } else {
                                Toast toast = Toast.makeText(getApplicationContext(), flag, Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                    }.execute();
                    break;
                case SEND_LOCATION:
                    if(code.equals("161")){
                    new AsyncTask<String, Void, Void>() {




                        @Override
                        protected Void doInBackground(String... params) {
                            try {
                                URL url = new URL("http://172.20.10.7:8080/Attendanceserver/ProcessCourseServlet?Cid=" + URLEncoder.encode(Cid,"UTF-8")+ "&signal=1"+"&Tid="+URLEncoder.encode(Tid,"UTF-8")+"&latitude="+URLEncoder.encode(Double.toString(latitude),"UTF-8")+"&longtitude="+URLEncoder.encode(Double.toString(longtitude),"UTF-8"));
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
                                //JSONObject root = new JSONObject(builder.toString());
                                //JSONArray array = root.getJSONArray("result");
                                //JSONObject object = array.getJSONObject(0);
                                flag = builder.toString();
                                if (flag.equals("open successfully")) {
                                    processflag = true;
                                } else {
                                    processflag = false;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            progressBar.setVisibility(View.GONE);
                            super.onPostExecute(aVoid);
                            if (processflag) {

                                Message message = new Message();
                                message.what = UPDATE_OEPN;
                                handler.sendMessage(message);
                                state = "Open for attendance";
                                Toast toast = Toast.makeText(getApplicationContext(), flag, Toast.LENGTH_SHORT);
                                toast.show();
                            } else {
                                Toast toast = Toast.makeText(getApplicationContext(), flag, Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                    }.execute();}
                    else{
                        progressBar.setVisibility(View.GONE);
                        Toast toast = Toast.makeText(getApplicationContext(), "The system didn't get you location,please try again.", Toast.LENGTH_SHORT);
                        toast.show();
                    }
            }

        };
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        progressBar=(ProgressBar)findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);
        locationClient = new LocationClient(getApplicationContext());
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setCoorType("bd09ll");
        option.setScanSpan(50000000);
        option.setProdName("Teacherapp");
        locationClient.setLocOption(option);
        locationClient.registerLocationListener(myListener);
        SharedPreferences sp1 = getSharedPreferences("Tid", Context.MODE_PRIVATE);
        Tid = sp1.getString("TID","1");
        Button open = (Button) findViewById(R.id.open);
        Button close = (Button) findViewById(R.id.close);
        Button record = (Button)findViewById(R.id.record);
        Button back = (Button)findViewById(R.id.back);
        coursename = (TextView)findViewById(R.id.coursename);
        courseno = (TextView)findViewById(R.id.courseno);
        coursestate = (TextView)findViewById(R.id.coursestate);
        SharedPreferences sp = getSharedPreferences("Tcourseinfo", Context.MODE_PRIVATE);
        Coursename = sp.getString("coursename","defaultname");
        Courseno = sp.getString("courseno","defaultno");
        Cid = sp.getString("Cid","null");
        state = sp.getString("state","null state");
        coursename.setText(sp.getString("coursename","defaultname"));
        courseno.setText(sp.getString("courseno","defaultno"));
        coursestate.setText(sp.getString("state","defaultstate"));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseActivity.this, FunctionActivity.class);
                startActivity(intent);
            }
        });
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                loc=true;
                if (Build.VERSION.SDK_INT>=23){
                    showContacts();
                }else{
                    locationClient.start();
                    locationClient.requestLocation();
                }

            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.what = UPDATE_CLOSE;
                handler.sendMessage(message);
            }
        });
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseActivity.this, RecordActivity.class);
                startActivity(intent);
            }
        });
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {

            //获取定位结果
            StringBuffer sb = new StringBuffer(256);

            sb.append("time : ");
            sb.append(location.getTime());    //获取定位时间

            sb.append("\nerror code : ");
            sb.append(location.getLocType());    //获取类型类型
            code = Integer.toString(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());    //获取纬度信息

            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());    //获取经度信息

            sb.append("\nradius : ");
            sb.append(location.getRadius());    //获取定位精准度

            if (location.getLocType() == BDLocation.TypeGpsLocation){

                // GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());    // 单位：公里每小时

                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());    //获取卫星数

                sb.append("\nheight : ");
                sb.append(location.getAltitude());    //获取海拔高度信息，单位米

                sb.append("\ndirection : ");
                sb.append(location.getDirection());    //获取方向信息，单位度

                sb.append("\naddr : ");
                sb.append(location.getAddrStr());    //获取地址信息

                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){

                // 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());    //获取地址信息

                sb.append("\noperationers : ");
                sb.append(location.getOperators());    //获取运营商信息

                sb.append("\ndescribe : ");
                sb.append("网络定位成功");

            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {

                // 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");

            } else if (location.getLocType() == BDLocation.TypeServerError) {

                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");

            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {

                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");

            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {

                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");

            }

            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());    //位置语义化信息

            List<Poi> list = location.getPoiList();    // POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            if(loc==true){
            latitude=location.getLatitude();
            longtitude = location.getLongitude();
            Log.i("BaiduLocationApiDem", sb.toString());
            Message message = new Message();
            message.what = SEND_LOCATION;
            handler.sendMessage(message);
            loc=false;}
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
        public void onRecievePoi(BDLocation location){

        }
    }
    public void showContacts(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(),"Please grant permission manually.",Toast.LENGTH_SHORT).show();
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            ActivityCompat.requestPermissions(CourseActivity.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE}, 100);
        }else{
            locationClient.start();
            locationClient.requestLocation();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case 100:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获取到权限，作相应处理（调用定位SDK应当确保相关权限均被授权，否则可能引起定位失败）
                    locationClient.start();
                } else {
                    // 没有获取到权限，做特殊处理
                    Toast.makeText(getApplicationContext(), "获取位置权限失败，请手动开启", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}


