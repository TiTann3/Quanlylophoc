package com.example.quanly_hssv.model;

import androidx.annotation.NonNull;

public class Subject {
    public int subjectid;
    public String subjectname;
    public String decription;
    public int credits;
    public int time;

    public Subject(int subjectid, String subjectname, String decription, int credits, int time) {
        this.subjectid = subjectid;
        this.subjectname = subjectname;
        this.decription = decription;
        this.credits = credits;
        this.time = time;
    }

    public Subject(String subjectname, String decription, int credits, int time) {
        this.subjectname = subjectname;
        this.decription = decription;
        this.credits = credits;
        this.time = time;
    }

    public Subject(int subjectid, String subjectname) {
        this.subjectid = subjectid;
        this.subjectname = subjectname;
    }

    public int getSubjectid() {
        return subjectid;
    }

    public void setSubjectid(int subjectid) {
        this.subjectid = subjectid;
    }

    public String getSubjectname() {
        return subjectname;
    }

    public void setSubjectname(String subjectname) {
        this.subjectname = subjectname;
    }

    public String getDecription() {
        return decription;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @NonNull
    @Override
    public String toString() {
        return subjectname;
    }
}
