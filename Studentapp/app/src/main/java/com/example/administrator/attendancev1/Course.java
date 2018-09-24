package com.example.administrator.attendancev1;

/**
 * Created by Administrator on 2017/3/1.
 */
public class Course {
    private String name;
    private String No;
    private String Cid;
    public Course(String name, String No,String id){
        this.name = name;
        this.No = No;
        this.Cid = id;
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
}
