package com.example.quanly_hssv;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanly_hssv.database.DatabaseHelper;

public class ActivityInformationStudent extends AppCompatActivity {

    final String DATABASE_NAME = "studentmanagement";
    TextView txtName,txtCode, txtDob, txtGender, txtAddress, txtClass, txtCourse;
    Button btnExit;
    int id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_student);
        addControl();
        addEvent();
        initUI();
    }
    private void initUI(){
        Intent i = getIntent();
        id = i.getIntExtra("ID", -1);
        SQLiteDatabase database = DatabaseHelper.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT a.code, a.name, a.dob, a.gender, a.address, b.name, b.course " +
                "FROM student a, class b where a.classid = b.classid and a.studentid = ?", new String[]{id+""});
        cursor.moveToFirst();
        String code = cursor.getString(0);
        String name = cursor.getString(1);
        String dob = cursor.getString(2);
        String gender = cursor.getString(3);
        String address = cursor.getString(4);
        String classname = cursor.getString(5);
        String course = cursor.getString(6);

        txtCode.setText(code);
        txtName.setText(name);
        txtDob.setText(dob);
        txtGender.setText(gender);
        txtAddress.setText(address);
        txtClass.setText(classname);
        txtCourse.setText(course);
    }
    private void addControl(){
        txtName = (TextView) findViewById(R.id.txtNameSI);
        txtCode = (TextView) findViewById(R.id.txtCodeSI);
        txtDob = (TextView) findViewById(R.id.txtDobSI);
        txtGender = (TextView) findViewById(R.id.txtGenderSI);
        txtAddress = (TextView) findViewById(R.id.txtAddressSI);
        txtCourse = (TextView) findViewById(R.id.txtCourseSI);
        txtClass = (TextView) findViewById(R.id.txtClassSI);
        btnExit = (Button) findViewById(R.id.btnExitInformation);
    }
    private void addEvent(){
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                setResult(Activity.RESULT_OK, i);
                finish();
            }
        });
    }
}