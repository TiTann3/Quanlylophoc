package com.example.quanly_hssv.model;

public class Student {
    public int studentid;
    public String studentcode;
    public String studentname;
    public String studentdob;
    public String studentgender;
    public String studentaddress;

    public int subjetid;
    public int idscore;
    public float scorevalue;
    public String nameSubject;

    public Student(String nameSubject, float scorevalue, int subjetid, int studentid) {
        this.nameSubject = nameSubject;
        this.scorevalue = scorevalue;
        this.subjetid = subjetid;
        this.studentid = studentid;
    }

    public Student(int studentid, String studentcode, String studentname, String studentdob, String studentgender, String studentaddress) {
        this.studentid = studentid;
        this.studentcode = studentcode;
        this.studentname = studentname;
        this.studentdob = studentdob;
        this.studentgender = studentgender;
        this.studentaddress = studentaddress;
    }

    public Student(int studentid, String studentcode, String studentname) {
        this.studentid = studentid;
        this.studentcode = studentcode;
        this.studentname = studentname;
    }

    public int getStudentid() {
        return studentid;
    }

    public void setStudentid(int studentid) {
        this.studentid = studentid;
    }

    public String getStudentcode() {
        return studentcode;
    }

    public void setStudentcode(String studentcode) {
        this.studentcode = studentcode;
    }

    public String getStudentname() {
        return studentname;
    }

    public void setStudentname(String studentname) {
        this.studentname = studentname;
    }

    public String getStudentdob() {
        return studentdob;
    }

    public void setStudentdob(String studentdob) {
        this.studentdob = studentdob;
    }

    public String getStudentgender() {
        return studentgender;
    }

    public void setStudentgender(String studentgender) {
        this.studentgender = studentgender;
    }

    public String getStudentaddress() {
        return studentaddress;
    }

    public void setStudentaddress(String studentaddress) {
        this.studentaddress = studentaddress;
    }

    public int getSubjetid() {
        return subjetid;
    }

    public void setSubjetid(int subjetid) {
        this.subjetid = subjetid;
    }

    public int getIdscore() {
        return idscore;
    }

    public void setIdscore(int idscore) {
        this.idscore = idscore;
    }

    public float getScorevalue() {
        return scorevalue;
    }

    public void setScorevalue(float scorevalue) {
        this.scorevalue = scorevalue;
    }

    public String getNameSubject() {
        return nameSubject;
    }

    public void setNameSubject(String nameSubject) {
        this.nameSubject = nameSubject;
    }
}
