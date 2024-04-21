package com.example.quanly_hssv.database;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static String DATABASE_NAME = "studentmanagement";

    private static int VERSION = 1;


    private Context mContext;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS YourTableName");
        onCreate(db);
    }

    public static SQLiteDatabase initDatabase(Activity activity, String databaseName){
        try{
            String outFileName = activity.getApplicationInfo().dataDir + "/databases/" + databaseName;
            File f = new File(outFileName);
            if(!f.exists()){
                InputStream e = activity.getAssets().open(databaseName);
                File folder = new File(activity.getApplicationInfo().dataDir + "/databases/");
                if(!folder.exists()){
                    folder.mkdir();
                }
                FileOutputStream myOutput = new FileOutputStream(outFileName);
                byte[] buffer = new byte[1024];

                int length;
                while((length = e.read(buffer)) > 0){
                    myOutput.write(buffer, 0, length);
                }

                myOutput.flush();
                myOutput.close();
                e.close();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return activity.openOrCreateDatabase(databaseName, Context.MODE_PRIVATE, null);
    }


    //cập nhật môn học
//    public boolean UpdateSubject(Subject subject, int id){
//
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//
//        values.put(SUBJECT_TITLE,subject.getSubject_title());
//        values.put(CREDITS,subject.getNumber_of_credits());
//        values.put(TIME,subject.getTime());
//        values.put(PLACE,subject.getPlace());
//
//        db.update(TABLE_SUBJECTS,values,ID_SUBJECTS+" = "+id,null);
//        Log.e("Ok : ",id+" - id "+values.get(SUBJECT_TITLE)+" + "+values.get(CREDITS)+" + "+values.get(TIME)+" + "+values.get(PLACE));
//        return true;
//    }
//    //cập nhật sinh viên
//    public boolean UpdateStudent(Student student, int id){
//
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(STUDENT_NAME,student.getStudent_name());
//        values.put(SEX,student.getSex());
//        values.put(STUDENT_CODE,student.getStudent_code());
//        values.put(DATE_OF_BIRTH,student.getDate_of_birth());
//
//
//        db.update(TABLE_STUDENT,values,ID_STUDENT+" = "+id,null);
//        Log.e("Ok : ",id+" - id "+values.get(STUDENT_NAME)+" + "+values.get(SEX)+" + "+values.get(STUDENT_CODE)+" + "+values.get(DATE_OF_BIRTH));
//        return true;
//    }
//
//    //Lấy tất mon hoc
//    public Cursor getDataSubjects(){
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res = db.rawQuery("SELECT * FROM "+TABLE_SUBJECTS,null);
//        return res;
//    }
//
//    //xóa môn học
//    public int DeleteSubject(int i){
//        SQLiteDatabase db = this.getWritableDatabase();
//        int res = db.delete(TABLE_SUBJECTS,ID_SUBJECTS+" = "+i,null);
//        return res;
//    }
//    //xóa student
//    public int DeleteSubjectStudent(int i){
//        SQLiteDatabase db = this.getWritableDatabase();
//        int res = db.delete(TABLE_STUDENT,ID_SUBJECTS+" = "+i,null);
//        return res;
//    }
//
//
//    //Insert student
//    public void AddStudent(Student student){
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        //không thể lưu trực tiếp xuống insert nên thông qua contentvalues
//        ContentValues values = new ContentValues();
//        values.put(STUDENT_NAME,student.getStudent_name());
//        values.put(SEX,student.getSex());
//        values.put(STUDENT_CODE,student.getStudent_code());
//        values.put(DATE_OF_BIRTH,student.getDate_of_birth());
//        values.put(ID_SUBJECTS,student.getId_subject());
//
//        db.insert(TABLE_STUDENT,null,values);
//        //đóng lại db cho an toàn
//        db.close();
//    }
//    //Lấy tất sinh viên thuộc môn học đó
//    public Cursor getDataStudent(int id_subject){
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res = db.rawQuery("SELECT * FROM "+TABLE_STUDENT+" WHERE "+ID_SUBJECTS+" = "+id_subject,null);
//        return res;
//    }
//
//    //xóa student
//    public int DeleteStudent(int i){
//        SQLiteDatabase db = this.getWritableDatabase();
//        int res = db.delete(TABLE_STUDENT,ID_STUDENT+" = "+i,null);
//        return res;
//    }
}
