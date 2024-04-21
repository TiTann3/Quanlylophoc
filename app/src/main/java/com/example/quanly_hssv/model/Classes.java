package com.example.quanly_hssv.model;

import androidx.annotation.NonNull;

public class Classes {
    public int classId;
    public String className;
    public String classCourse;

    public Classes(int classId, String className, String classCourse) {
        this.classId = classId;
        this.className = className;
        this.classCourse = classCourse;
    }

    public Classes(int classId, String className) {
        this.classId = classId;
        this.className = className;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassCourse() {
        return classCourse;
    }

    public void setClassCourse(String classCourse) {
        this.classCourse = classCourse;
    }

    @NonNull
    @Override
    public String toString() {
        return className;
    }
}
