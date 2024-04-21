package com.example.quanly_hssv.model;

import androidx.annotation.NonNull;

public class Teacher {
    public int teacherid;
    public String teachername;
    public String dob;
    public String gender;

    public Teacher(int teacherid, String teachername, String dob, String gender) {
        this.teacherid = teacherid;
        this.teachername = teachername;
        this.dob = dob;
        this.gender = gender;
    }

    public Teacher(int teacherid, String teachername) {
        this.teacherid = teacherid;
        this.teachername = teachername;
    }

    public int getTeacherid() {
        return teacherid;
    }

    public void setTeacherid(int teacherid) {
        this.teacherid = teacherid;
    }

    public String getTeachername() {
        return teachername;
    }

    public void setTeachername(String teachername) {
        this.teachername = teachername;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @NonNull
    @Override
    public String toString() {
        return teachername;
    }
}
