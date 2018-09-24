package com.example.administrator.teacherapp;

/**
 * Created by Administrator on 2017/4/6.
 */
public class Record {
    private String studentno;
    private String studentname;

    public Record(String studentname,String studentno) {
        this.studentname = studentname;
        this.studentno = studentno;
    }

    public String getStudentno() {
        return studentno;
    }

    public void setStudentno(String studentno) {
        this.studentno = studentno;
    }

    public String getStudentname() {
        return studentname;
    }

    public void setStudentname(String studentname) {
        this.studentname = studentname;
    }
}
