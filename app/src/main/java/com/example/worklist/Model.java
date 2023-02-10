package com.example.worklist;

public class Model {
    String title, date ;
    int check,id,complete,uid;

    public Model(String title, String date, int check, int id,int complete,int uid) {
        this.title = title;
        this.date = date;
        this.check = check;
        this.id = id;
        this.complete=complete;
        this.uid=uid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getComplete() {
        return complete;
    }

    public void setComplete(int complete) {
        this.complete = complete;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }
}
