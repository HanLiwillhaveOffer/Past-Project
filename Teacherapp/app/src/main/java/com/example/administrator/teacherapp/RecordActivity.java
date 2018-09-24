package com.example.administrator.teacherapp;

import android.os.Environment;
import android.os.IBinder;
import android.os.StatFs;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
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
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class RecordActivity extends AppCompatActivity {
    String Cid;
    String year="";
    String month="";
    String day="";
    String date;
    String filename;
    int curyear;
    int curmonth;
    int curday;
    EditText YearEditText;
    EditText MonthEditText;
    EditText DayEditText;
    String coursename;
    SharedPreferences sp;
    private ArrayList<Record> recordList = new ArrayList<Record>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        Button submit = (Button) findViewById(R.id.submit);
        Button export = (Button)findViewById(R.id.export);
        Button checkcur = (Button)findViewById(R.id.checkcur);
        YearEditText = (EditText)findViewById(R.id.year);
        MonthEditText = (EditText)findViewById(R.id.month);
        DayEditText = (EditText)findViewById(R.id.day);
        sp = this.getSharedPreferences("Tcourseinfo", Context.MODE_PRIVATE);
        coursename = sp.getString("coursename","nullcoursename");
        Cid = sp.getString("Cid","nullCid");
        TimeZone tz = TimeZone.getTimeZone("GMT");
        Calendar c = Calendar.getInstance(tz);
        curyear = c.get(Calendar.YEAR);
        curmonth = c.get(Calendar.MONTH)+1;
        curday = c.get(Calendar.DAY_OF_MONTH);
        date = curyear+"-"+curmonth+"-"+curday;
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                year = YearEditText.getText().toString();
                month = MonthEditText.getText().toString();
                day = DayEditText.getText().toString();
                if(year.equals("")||month.equals("")||day.equals("")){
                    Toast.makeText(getApplicationContext(), "Please complete date.", Toast.LENGTH_LONG).show();
                }else{
                new AsyncTask<String, Void, Void>() {
                    @Override
                    protected Void doInBackground(String... params) {
                        try {
                            recordList.clear();
                            URL url = new URL("http://172.20.10.7:8080/Attendanceserver/RecordServlet?Cid=" + URLEncoder.encode(Cid,"UTF-8")+ "&"+"year=" + URLEncoder.encode(year,"UTF-8")+"&month="+ URLEncoder.encode(month,"UTF-8")+"&day="+ URLEncoder.encode(day,"UTF-8")+"&code=notcurrent");
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
                                JSONObject record = (JSONObject) array.get(i);
                                Record r = new Record(record.getString("Sname"),record.getString("Sno"));
                                recordList.add(r);}
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        if(recordList.isEmpty()){
                            Toast toast = Toast.makeText(getApplicationContext(), "No record", Toast.LENGTH_SHORT);
                            toast.show();
                            super.onPostExecute(aVoid);
                            ListView listView = (ListView) findViewById(R.id.record);
                            RecordAdapter adapter = new RecordAdapter(RecordActivity.this, R.layout.record_item, recordList);
                            listView.setAdapter(adapter);
                        }
                        else {
                            super.onPostExecute(aVoid);
                            ListView listView = (ListView) findViewById(R.id.record);
                            RecordAdapter adapter = new RecordAdapter(RecordActivity.this, R.layout.record_item, recordList);
                            listView.setAdapter(adapter);
                        }
                    }
                }.execute();}
            }
        });
        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recordList.isEmpty()){
                    Toast toast = Toast.makeText(getApplicationContext(), "No record.", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else{
                try{
                    File dirFirstFolder = new File("sdcard/Attendance record");//
                    if(!dirFirstFolder.exists())
                    {
                        dirFirstFolder.mkdirs();
                    }
                    File file;
                    if(year.equals("")||month.equals("")||day.equals("")){
                        filename = coursename+date;
                    }else{
                    filename = coursename + year +"-"+month+"-"  +day;}
                    file = new File(dirFirstFolder, filename+".xls");
                    if (!file.exists()) {
                        file.createNewFile();

                    }
                    file.setWritable(Boolean.TRUE);
                    WritableWorkbook wwb = Workbook.createWorkbook(file);
                    WritableSheet sheet = wwb.createSheet("Attendance record", 0);
                    String[] title = { "Student number", "Student name" };
                    Label label;
                    for (int i = 0; i < title.length; i++) {
                        // Label(x,y,z) 代表单元格的第x+1列，第y+1行, 内容z
                        // 在Label对象的子对象中指明单元格的位置和内容
                        label = new Label(i, 0, title[i], getHeader());
                        // 将定义好的单元格添加到工作表中
                        sheet.addCell((WritableCell) label);
                    }
                    for (int i = 0; i < recordList.size(); i++) {
                       Record record = recordList.get(i);

                        Label Sname = new Label(0, i + 1, record.getStudentname());
                        Label Sno = new Label(1, i + 1, record.getStudentno());
                        sheet.addCell(Sname);
                        sheet.addCell(Sno);

                     }
                    wwb.write();
                    wwb.close();
                    Toast.makeText(getApplicationContext(), "Export successfully.", Toast.LENGTH_LONG).show();
                }
                catch (Exception e){
                    e.printStackTrace();
                }}


            }
        });
        checkcur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                year = "";
                month = "";
                day = "";
                new AsyncTask<String, Void, Void>() {
                        @Override
                        protected Void doInBackground(String... params) {
                            try {
                                recordList.clear();
                                URL url = new URL("http://172.20.10.7:8080/Attendanceserver/RecordServlet?Cid=" + URLEncoder.encode(Cid,"UTF-8")+ "&code=current");
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
                                    JSONObject record = (JSONObject) array.get(i);
                                    Record r = new Record(record.getString("Sname"),record.getString("Sno"));
                                    recordList.add(r);}
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            if(recordList.isEmpty()){
                                Toast toast = Toast.makeText(getApplicationContext(), "No record", Toast.LENGTH_SHORT);
                                toast.show();
                                super.onPostExecute(aVoid);
                                ListView listView = (ListView) findViewById(R.id.record);
                                RecordAdapter adapter = new RecordAdapter(RecordActivity.this, R.layout.record_item, recordList);
                                listView.setAdapter(adapter);
                            }
                            else {
                                super.onPostExecute(aVoid);
                                ListView listView = (ListView) findViewById(R.id.record);
                                RecordAdapter adapter = new RecordAdapter(RecordActivity.this, R.layout.record_item, recordList);
                                listView.setAdapter(adapter);
                            }
                        }
                    }.execute();}

        });
    }

    private static long getAvailableStorage(Context context) {
        String root = context.getExternalFilesDir(null).getPath();
        StatFs statFs = new StatFs(root);
        long blockSize = statFs.getBlockSize();
        long availableBlocks = statFs.getAvailableBlocks();
        long availableSize = blockSize * availableBlocks;
        // Formatter.formatFileSize(context, availableSize);
        return availableSize;
    }
    public static WritableCellFormat getHeader() {
        WritableFont font = new WritableFont(WritableFont.TIMES, 10,
                WritableFont.BOLD);// 定义字体
        try {
            font.setColour(Colour.BLUE);// 蓝色字体
        } catch (WriteException e1) {
            e1.printStackTrace();
        }
        WritableCellFormat format = new WritableCellFormat(font);
        try {
            format.setAlignment(jxl.format.Alignment.CENTRE);// 左右居中
            format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 上下居中
            format.setBorder(Border.ALL, BorderLineStyle.THIN,
                    Colour.BLACK);// 黑色边框
            format.setBackground(Colour.YELLOW);// 黄色背景
        } catch (WriteException e) {
            e.printStackTrace();
        }
        return format;
    }
}
