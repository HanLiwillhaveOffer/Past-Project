package com.example.administrator.attendancev1;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
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

import java.io.LineNumberReader;
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
import java.util.List;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
public class SigninActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    Boolean locFlag;
    int isSignin;
    String flag = "0";
    String studentno;
    String Coursename;
    String Courseno;
    String Cid;
    double dis;
    String MAC;
    String code;
    JSONObject result2;
    double latitude;
    double longtitude;
    double Tlatitude;
    double Tlongtitude;
    private static final int LOCATION= 2;
    private static final int GET_TLOCATION=1;
    public LocationClient locationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case GET_TLOCATION:
                    new AsyncTask<String, Void, Void>() {
                        @Override
                        protected Void doInBackground(String... params) {
                            try {
                                URL url2 = new URL("http://172.20.10.7:8080/Attendanceserver/GetLocServlet?Cid="+URLEncoder.encode(Cid,"UTF-8"));
                                HttpURLConnection connection2 = (HttpURLConnection)url2.openConnection();
                                connection2.setRequestMethod("GET");
                                connection2.setReadTimeout(50000);
                                connection2.setConnectTimeout(5000);
                                InputStream is2 = connection2.getInputStream();
                                InputStreamReader isr2 = new InputStreamReader(is2, "utf-8");
                                BufferedReader br2 = new BufferedReader(isr2);
                                String line2;
                                StringBuilder builder2 = new StringBuilder();
                                while ((line2 = br2.readLine()) != null) {
                                    builder2.append(line2);
                                }
                                br2.close();
                                isr2.close();
                                result2 = new JSONObject(builder2.toString());
                                Tlatitude = Double.valueOf(result2.getString("latitude"));
                                Tlongtitude = Double.valueOf(result2.getString("longtitude"));

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                        }
                    }.execute();
                    break;
                case LOCATION:
                    if(Tlatitude==0.0||Tlongtitude==0.0){
                        progressBar.setVisibility(View.GONE);
                        Message message = new Message();
                        message.what = GET_TLOCATION;
                        handler.sendMessage(message);
                        Toast toast = Toast.makeText(getApplicationContext(), "Please try again", Toast.LENGTH_SHORT);
                        toast.show();
                    }else{
                    new AsyncTask<String, Void, Void>() {
                        @Override
                        protected Void doInBackground(String... params) {
                            try {


                                dis = GetDistance(latitude,longtitude,Tlatitude,Tlongtitude);
                                if(code.equals("161")){
                                    URL url = new URL("http://172.20.10.7:8080/Attendanceserver/SigninServlet?studentnumber=" + URLEncoder.encode(studentno,"UTF-8")+ "&"+"coursename=" + URLEncoder.encode(Coursename,"UTF-8")+"&"+"courseno="+ URLEncoder.encode(Courseno,"UTF-8")+"&"+"Cid="+URLEncoder.encode(Cid,"UTF-8")+"&MAC="+URLEncoder.encode(MAC,"UTF-8")+"&dis="+URLEncoder.encode(Double.toString(dis),"UTF-8"));
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
                                        isSignin = 1;
                                    } else if(flag.equals("0")) {
                                        isSignin = 0;
                                    }else if(flag.equals("2")){
                                        isSignin = 2;
                                    }else if(flag.equals("3")){
                                        isSignin = 3;
                                    }else if(flag.equals("4")){
                                        isSignin = 4;
                                    }
                                }
                                else{
                                    isSignin = 10;
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
                            if (isSignin==1) {
                                Toast toast = Toast.makeText(getApplicationContext(), "Sign in successfully."+Double.toString(dis), Toast.LENGTH_SHORT);
                                toast.show();
                            } else if(isSignin ==0){
                                Toast toast = Toast.makeText(getApplicationContext(), "Please move closer to the teacher and try again."+ Double.toString(dis), Toast.LENGTH_SHORT);
                                toast.show();
                            }else if(isSignin ==2){
                                Toast toast = Toast.makeText(getApplicationContext(), "Your phone has already signed in this course today.", Toast.LENGTH_SHORT);
                                toast.show();
                            }else if(isSignin==3){
                                Toast toast = Toast.makeText(getApplicationContext(), "The course is already closed.", Toast.LENGTH_SHORT);
                                toast.show();
                            }else if(isSignin==10){
                                Toast toast = Toast.makeText(getApplicationContext(), "Please make sure you have granted location permission for the app and try again.", Toast.LENGTH_SHORT);
                                toast.show();
                            }else if(isSignin==4){
                                Toast toast = Toast.makeText(getApplicationContext(), "You have already signed in this course today.", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                    }.execute();}
                    break;
            }

        };
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        progressBar = (ProgressBar)findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        MAC = getMac();
        TextView coursename = (TextView)findViewById(R.id.coursename);
        TextView courseno = (TextView)findViewById(R.id.courseno);
        Button button = (Button) findViewById(R.id.signin);
        Button back = (Button)findViewById(R.id.back);
        SharedPreferences sp = getSharedPreferences("courseinfo", Context.MODE_PRIVATE);
        SharedPreferences sp2 = getSharedPreferences("userInfo",Context.MODE_PRIVATE);
        Coursename = sp.getString("coursename","defaultname");
        Courseno = sp.getString("courseno","defaultno");
        Cid = sp.getString("Cid","null");
        studentno = sp2.getString("STUDENTNO","nobody");
        coursename.setText(sp.getString("coursename","defaultname"));
        courseno.setText(sp.getString("courseno","defaultno"));
        locationClient = new LocationClient(getApplicationContext());
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setCoorType("bd09ll");
        option.setProdName("Teacherapp");
        locationClient.setLocOption(option);
        locationClient.registerLocationListener(myListener);

        Message message = new Message();
        message.what = GET_TLOCATION;
        handler.sendMessage(message);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                locFlag=true;
                if (Build.VERSION.SDK_INT>=23){
                    showContacts();
                    Log.i("1", ">23");
                }else {
                    Log.i("2", "<23");
                    locationClient.start();
                    locationClient.requestLocation();

                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SigninActivity.this, ShowcourseActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private String getMac()
    {
        String macSerial = null;
        String str = "";
        try
        {
            Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            for (; null != str;)
            {
                str = input.readLine();
                if (str != null)
                {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return macSerial;
    }
    private double GetDistance(double Latitude1,double Longitude1,double Latitude2,double Longitude2)
    {
        float[] res=new float[1];
        Location.distanceBetween(Latitude1, Longitude1, Latitude2, Longitude2, res);
        return res[0];
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
            if(locFlag==true){
            latitude=location.getLatitude();
            longtitude = location.getLongitude();
            Log.i("BaiduLocationApiDem", sb.toString());
            Message message = new Message();
            message.what = LOCATION;
            handler.sendMessage(message);
                locFlag=false;}


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
            Toast.makeText(getApplicationContext(),"Please grant location permission.",Toast.LENGTH_SHORT).show();
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            ActivityCompat.requestPermissions(SigninActivity.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE}, 100);
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
