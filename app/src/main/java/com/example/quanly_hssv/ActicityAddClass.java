package com.example.quanly_hssv;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.quanly_hssv.database.DatabaseHelper;

public class ActicityAddClass extends AppCompatActivity {

    final String DATABASE_NAME = "studentmanagement";
    ImageButton ibtnBack;
    EditText edtName, edtCourse;
    Button btnAddClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acticity_add_class);
        addControl();
        addEvent();
    }

    private void addControl() {
        edtName = (EditText) findViewById(R.id.edtAddNameClass);
        edtCourse = (EditText) findViewById(R.id.edtAddCourse);
        btnAddClass = (Button) findViewById(R.id.btnAddClass);
        ibtnBack = (ImageButton) findViewById(R.id.ibtnBack);
    }

    private void addEvent() {
        btnAddClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogAdd();
            }
        });
        ibtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });
    }


    private void DialogAdd() {
        //Tạo đối tượng cửa sổ dialog
        Dialog dialog  =  new Dialog(this);

        //Nạp layout vào
        dialog.setContentView(R.layout.dialogadd);

        //Click No mới thoát, click ngoài ko thoát
        dialog.setCanceledOnTouchOutside(false);

        //Ánh xạ
        Button btnYes = dialog.findViewById(R.id.buttonYesAddSubject);
        Button btnNo = dialog.findViewById(R.id.buttonNoAddSubject);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name =edtName.getText().toString();
                String course = edtCourse.getText().toString();

                if(name.equals("") || course.equals("")){
                    Toast.makeText(ActicityAddClass.this,"Chưa nhập đủ thông tin",Toast.LENGTH_SHORT).show();
                }
                else {
                    insert();
                    //Khởi tạo lại activity main
                    Intent intent = new Intent(ActicityAddClass.this,ActivityClass.class);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                    Toast.makeText(ActicityAddClass.this,"Thêm thành công",Toast.LENGTH_SHORT).show();
                }
            }
        });
        //Nếu no thì đóng dialog
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        //show dialog lên activity
        dialog.show();
    }

    private void insert() {
        String name = edtName.getText().toString();
        String course = edtCourse.getText().toString();

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("course", course);

        SQLiteDatabase database = DatabaseHelper.initDatabase(this, DATABASE_NAME);
        database.insert("class", null, contentValues);
    }
    private void cancel() {
        Intent i = new Intent();
        setResult(Activity.RESULT_OK, i);
        finish();
    }
}