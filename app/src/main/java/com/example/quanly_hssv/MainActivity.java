
/*
        STT 		Họ và Tên			MSSV		  Lớp
        1 		Nguyễn Thanh Tân	 21CTHA0455		CCNTT21F
        2 		Nguyễn Hoài Thu		 21CTHA0377		CCNTT21F
        3 		Nguyễn Công Phu		 21CTHA0305		CCNTT21F
*/

package com.example.quanly_hssv;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnSubjects, btnTeacher, btnClass, btnStudent, btnScore,btnExit;
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSubjects = (Button) findViewById(R.id.btnSubjects);
        btnExit = (Button) findViewById(R.id.buttonExit);
        btnTeacher =(Button) findViewById(R.id.btnTeacher);
        btnStudent = (Button) findViewById(R.id.btnStudent);
        btnClass = (Button) findViewById(R.id.btnClass);
        btnScore = (Button) findViewById(R.id.btnScore);

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogExit();
            }
        });
        btnSubjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ActivitySubjects.class);
                startActivity(intent);
            }
        });
        btnTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ActivityTeacher.class);
                startActivity(intent);
            }
        });
        btnClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ActivityClass.class);
                startActivity(intent);
            }
        });
        btnStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ActivityStudent.class);
                startActivity(intent);
            }
        });
        btnScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ActivityScore.class);
                startActivity(intent);
            }
        });
    }
    //Dialog Thoát
    private void DialogExit() {

        //Tạo đối tượng cửa sổ dialog
        Dialog dialog  =  new Dialog(this);

        //Nạp layout vào
        dialog.setContentView(R.layout.dialogexit);

        //Click No mới thoát, click ngoài ko thoát
        dialog.setCanceledOnTouchOutside(false);

        //Ánh xạ
        Button btnYes = dialog.findViewById(R.id.buttonYes);
        Button btnNo = dialog.findViewById(R.id.buttonNo);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
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

    //Nhấn nút back trên android 2 lần trên activity main thì thoát ứng dụng
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        counter++;
        if (counter >= 1) {
            // Tạo sự kiện kết thúc app
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startActivity(startMain);
        }
    }
}