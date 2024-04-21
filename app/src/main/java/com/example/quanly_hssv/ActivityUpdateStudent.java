package com.example.quanly_hssv;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanly_hssv.database.DatabaseHelper;
import com.example.quanly_hssv.model.Classes;
import com.example.quanly_hssv.model.Student;

import java.util.ArrayList;
import java.util.List;

public class ActivityUpdateStudent extends AppCompatActivity {

    final String DATABASE_NAME = "studentmanagement";
    Button btnUpdate;
    EditText edtCode, edtName, edtDob, edtAddress;
    RadioButton rbtnMale,rbtnFemale;
    Spinner spnIdClass;
    ImageButton ibtnBack;

    int id = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_student);
        addControls();
        addEvent();
        initUI();
    }

    private void initUI(){
        Intent i = getIntent();
        id = i.getIntExtra("ID", -1);
        SQLiteDatabase database = DatabaseHelper.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM student where studentid = ?", new String[]{id+""});
        cursor.moveToFirst();
        String code = cursor.getString(1);
        String name = cursor.getString(2);
        String dob = cursor.getString(3);
        String gender = cursor.getString(4);
        String address = cursor.getString(5);
        int idclass = cursor.getInt(6);

        edtCode.setText(code);
        edtName.setText(name);
        edtDob.setText(dob);
        if(gender.equals("Male")){
            rbtnMale.setChecked(true);
        }else {
            rbtnFemale.setChecked(true);
        }
        edtAddress.setText(address);
        spnIdClass.setSelection(idclass-1);
    }

    private void addControls(){
        edtCode = (EditText) findViewById(R.id.edtUpdateStudentCode);
        edtName = (EditText) findViewById(R.id.edtUpdateStudentName);
        edtDob = (EditText) findViewById(R.id.edtUpdateStudentDob);
        edtAddress = (EditText) findViewById(R.id.edtUpdateStudentAddress);
        rbtnMale = (RadioButton) findViewById(R.id.rbtnMale);
        rbtnFemale = (RadioButton) findViewById(R.id.rbtnFemale);
        btnUpdate = (Button) findViewById(R.id.buttonUpdateStudent);
        spnIdClass = (Spinner) findViewById(R.id.spnUpdateClassStudent);
        ibtnBack = (ImageButton) findViewById(R.id.ibtnBack);
    }
    private void addEvent(){
        List<Classes> danhSachClass = getAllClass();
        ArrayAdapter<Classes> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, danhSachClass);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnIdClass.setAdapter(adapter);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUpdate();
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

    private void DialogUpdate() {
    //Tạo đối tượng cửa sổ dialog
    Dialog dialog  =  new Dialog(this);

    //Nạp layout vào
        dialog.setContentView(R.layout.dialogupdatestudent);

    //Click No mới thoát, click ngoài ko thoát
        dialog.setCanceledOnTouchOutside(false);

    //Ánh xạ
    Button btnYes = dialog.findViewById(R.id.buttonYesUpdateStudent);
    Button btnNo = dialog.findViewById(R.id.buttonNoUpdateStudent);

        btnYes.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String name = edtName.getText().toString();
            String code = edtCode.getText().toString();
            String dob = edtDob.getText().toString();
            String address = edtAddress.getText().toString();

            if(name.equals("") || code.equals("") || dob.equals("") || address.equals("")){
                Toast.makeText(ActivityUpdateStudent.this,"Chưa nhập đủ thông tin",Toast.LENGTH_SHORT).show();
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
    //Create Subject
    private void update(){
        String code = edtCode.getText().toString();
        String name = edtName.getText().toString();
        String dob = edtDob.getText().toString();
        String address = edtAddress.getText().toString();
        String gender="";
        if(rbtnMale.isChecked()){
            gender =  "Male";
        }
        else {
            gender = "Female";
        }
        int idclass = getIdClassFromSpinner(spnIdClass);

        ContentValues contentValues = new ContentValues();
        contentValues.put("code", code);
        contentValues.put("name", name);
        contentValues.put("dob", dob);
        contentValues.put("gender", gender);
        contentValues.put("address", address);
        contentValues.put("classid", idclass);
        SQLiteDatabase database = DatabaseHelper.initDatabase(this, DATABASE_NAME);
        database.update("student", contentValues, "studentid = ?", new String[]{id+""});
        Intent i = new Intent(ActivityUpdateStudent.this, ActivityStudent.class);
        setResult(Activity.RESULT_OK, i);
        finish();
        Toast.makeText(ActivityUpdateStudent.this,"Cập Nhật Thành Công",Toast.LENGTH_SHORT).show();
    }
    public List<Classes> getAllClass() {
        List<Classes> classList = new ArrayList<>();
        SQLiteDatabase db = DatabaseHelper.initDatabase(this, DATABASE_NAME);
        Cursor cursor = db.rawQuery("SELECT classid, name FROM class", null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int classID = cursor.getInt(cursor.getColumnIndex("classid"));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                Classes classes = new Classes(classID, name);
                classList.add(classes);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return classList;
    }

    public int getIdClassFromSpinner(Spinner spinner) {
        // Lấy vị trí của item đã chọn trong Spinner
        int selectedPosition = spinner.getSelectedItemPosition();

        // Lấy dữ liệu từ Spinner theo vị trí đã chọn
        Classes selectedTen = (Classes) spinner.getItemAtPosition(selectedPosition);

        // Trả về id của CLB đã chọn từ Spinner
        return selectedTen.getClassId();
    }
}