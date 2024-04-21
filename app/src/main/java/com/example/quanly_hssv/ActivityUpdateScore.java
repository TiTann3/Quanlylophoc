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
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanly_hssv.database.DatabaseHelper;

public class ActivityUpdateScore extends AppCompatActivity {

    final String DATABASE_NAME = "studentmanagement";

    int id = -1;
    int idstudent = -1;
    TextView txtSubjectName;
    EditText edtScoreValue;
    Button btnUpdate;
    ImageButton ibtnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_score);
        addControl();
        addEvent();
        initUI();
    }
    private void initUI() {
        Intent i = getIntent();
        id = i.getIntExtra("ID", -1);
        idstudent = i.getIntExtra("IDStudent", -1);
        SQLiteDatabase database = DatabaseHelper.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("select a.name, b.scorevalue from subject a, scores b " +
                "where a.subjectid = b.subjectid and b.studentid = ? AND b.subjectid = ?", new String[]{idstudent+"", id+""});
        cursor.moveToFirst();
        String name = cursor.getString(0);
        float scorValue = cursor.getFloat(1);

        txtSubjectName.setText(name);
        edtScoreValue.setText(scorValue+"");
    }

    private void addControl() {
        edtScoreValue = (EditText) findViewById(R.id.edtScoreValue);
        txtSubjectName = (TextView) findViewById(R.id.txtSubjectNameScore);
        btnUpdate = (Button) findViewById(R.id.btnUpdateScore);
        ibtnBack = (ImageButton) findViewById(R.id.ibtnBack);
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

                String scoreValue = edtScoreValue.getText().toString();


                if(scoreValue.equals("")){
                    Toast.makeText(ActivityUpdateScore.this,"Chưa nhập đủ thông tin",Toast.LENGTH_SHORT).show();
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

        float scoreValue  = Float.parseFloat(edtScoreValue.getText().toString());

        ContentValues contentValues = new ContentValues();
        contentValues.put("scorevalue", scoreValue);
        SQLiteDatabase database = DatabaseHelper.initDatabase(this, DATABASE_NAME);
        database.update("scores", contentValues, "subjectid = ? AND studentid = ?", new String[]{id+"", idstudent+""});
        Intent intent = new Intent(ActivityUpdateScore.this,ActivityInformationScore.class);
        intent.putExtra("ID", idstudent);
        setResult(Activity.RESULT_OK, intent);
        finish();
        Toast.makeText(ActivityUpdateScore.this,"Cập Nhật Thành Công",Toast.LENGTH_SHORT).show();
    }
}