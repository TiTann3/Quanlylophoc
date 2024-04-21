package com.example.quanly_hssv;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanly_hssv.database.DatabaseHelper;

public class ActivityInformationSubject extends AppCompatActivity {

    final String DATABASE_NAME = "studentmanagement";
    TextView txtName,txtCredit,txtTime,txtDescription, txtTeacher;
    Button btnExit;
    int id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_subject);
        addControls();
        addEvent();
        initUI();

    }
    private void initUI(){
        Intent i = getIntent();
        id = i.getIntExtra("SubjectID", -1);
        SQLiteDatabase database = DatabaseHelper.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT a.name, a.decription, a.credits, a.time, b.name " +
                "FROM subject a, teacher b WHERE a.teacherid = b.teacherid and a.subjectid = ?", new String[]{id+""});
        cursor.moveToFirst();
        String nameSubject = cursor.getString(0);
        String description = cursor.getString(1);
        int credit = cursor.getInt(2);
        int time = cursor.getInt(3);
        String nameTeacher = cursor.getString(4);

        txtName.setText(nameSubject);
        txtDescription.setText(description);
        txtCredit.setText(credit+"");
        txtTime.setText(time+"");
        txtTeacher.setText(nameTeacher);
    }
    private void addControls(){
        txtCredit = findViewById(R.id.txtCreditInformation);
        txtDescription = findViewById(R.id.txtDescriptionInformation);
        txtTime = findViewById(R.id.txtTimeInforomation);
        txtName = findViewById(R.id.txtSubjectNameInformation);
        txtTeacher = findViewById(R.id.txtTeacherInforomation);
        btnExit = findViewById(R.id.btnExitInformation);
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