package com.example.quanly_hssv;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.quanly_hssv.database.DatabaseHelper;

public class ActivityUpdateClass extends AppCompatActivity {

    final String DATABASE_NAME = "studentmanagement";

    int id = -1;
    EditText edtName, edtCourse;
    Button btnUpdate;
    ImageButton ibtnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_class);

        addControls();
        addEvent();
        initUI();
    }

    private void initUI() {
        Intent i = getIntent();
        id = i.getIntExtra("ClassID", -1);
        SQLiteDatabase database = DatabaseHelper.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM class where classid = ?", new String[]{id+""});
        cursor.moveToFirst();
        String name = cursor.getString(1);
        int course = cursor.getInt(2);

        edtName.setText(name);
        edtCourse.setText(course+"");
    }

    private void addEvent() {
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUpdate(id);
            }
        });
        ibtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });
    }
    private void cancel() {
        Intent i = new Intent();
        setResult(Activity.RESULT_OK, i);
        finish();
    }

    private void addControls() {
        edtName = (EditText) findViewById(R.id.edtUpdateClassName);
        edtCourse = (EditText) findViewById(R.id.edtUpdateCourse);
        btnUpdate = (Button) findViewById(R.id.btnUpdateClass);
        ibtnBack = (ImageButton) findViewById(R.id.ibtnBack);
    }
    private void DialogUpdate(int id) {

        //Tạo đối tượng cửa sổ dialog
        Dialog dialog  =  new Dialog(this);

        //Nạp layout vào
        dialog.setContentView(R.layout.dialogupdate);

        //Click No mới thoát, click ngoài ko thoát
        dialog.setCanceledOnTouchOutside(false);

        //Ánh xạ
        Button btnYes = dialog.findViewById(R.id.buttonYesUpdateSubject);
        Button btnNo = dialog.findViewById(R.id.buttonNoUpdateSubject);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = edtName.getText().toString();
                String course = edtCourse.getText().toString();


                if(name.equals("") || course.equals("")){
                    Toast.makeText(ActivityUpdateClass.this,"Chưa nhập đủ thông tin",Toast.LENGTH_SHORT).show();
                }
                else {
                    update();
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
    private void update(){
        String name = edtName.getText().toString();
        int course = Integer.parseInt(edtCourse.getText().toString());

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("course", course);

        SQLiteDatabase database = DatabaseHelper.initDatabase(this, DATABASE_NAME);
        database.update("class",  contentValues, "classid = ?", new String[]{id+""});
        Intent intent = new Intent(ActivityUpdateClass.this,ActivityClass.class);
        setResult(Activity.RESULT_OK, intent);
        finish();
        Toast.makeText(ActivityUpdateClass.this,"Sửa Thành Công",Toast.LENGTH_SHORT).show();
    }
}