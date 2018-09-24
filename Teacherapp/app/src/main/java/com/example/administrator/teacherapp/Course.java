package com.example.administrator.teacherapp;

/**
 * Created by Administrator on 2017/3/15.
 */
public class Course {
    private String name;
    private String No;
    private String Cid;
    private String Tid;
    private String state;
    public Course(String name, String No,String id,String State){
        this.name = name;
        this.No = No;
        this.Cid = id;
        this.state = State;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNo(String no) {
        No = no;
    }

    public String getName() {
        return name;
    }

    public String getNo() {
        return No;
    }

    public String getCid() {
        return Cid;
    }

    public void setCid(String cid) {
        Cid = cid;
    }

    public String getTid() {
        return Tid;
    }

    public void setTid(String tid) {
        Tid = tid;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
