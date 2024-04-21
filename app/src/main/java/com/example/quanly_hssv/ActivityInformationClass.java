package com.example.quanly_hssv;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.quanly_hssv.database.DatabaseHelper;

public class ActivityInformationClass extends AppCompatActivity {

    final String DATABASE_NAME = "studentmanagement";
    TextView txtName, txtCourse, txtTeacher, txtCount;
    Button btnExit;
    int id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_class);
        addControl();
        addEvent();
        initUI();
    }

    private void initUI() {
        Intent i = getIntent();
        id = i.getIntExtra("ID", -1);
        SQLiteDatabase database = DatabaseHelper.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT a.name, a.course, b.name,  count(c.studentid) " +
                "FROM class a, teacher b, student c where a.classid = b.classid and a.classid = c.classid and a.classid = ?",
                new String[]{id+""});
        cursor.moveToFirst();
        String classname = cursor.getString(0);
        int course = cursor.getInt(1);
        String studentname = cursor.getString(2);
        int count = cursor.getInt(3);

        txtName.setText(classname);
        txtCourse.setText(id+"");
        txtTeacher.setText(studentname);
        txtCount.setText(count+"");
    }
    private void addControl() {
        txtName = (TextView) findViewById(R.id.txtNameCI);
        txtCourse = (TextView) findViewById(R.id.txtCourseCI);
        txtCount = (TextView) findViewById(R.id.txtCountCI);
        txtTeacher = (TextView) findViewById(R.id.txtTeacherCI);
        btnExit = (Button) findViewById(R.id.btnExitInformation);
    }

    private void addEvent() {
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