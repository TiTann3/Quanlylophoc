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

public class ActivityAddStudent extends AppCompatActivity {

    final String DATABASE_NAME = "studentmanagement";
    Button btnAdd;
    EditText edtCode, edtName, edtDob, edtAddress;
    RadioButton rbtnMale,rbtnFemale;
    Spinner spnIdClass;
    ImageButton ibtnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        addControls();
        addEvent();
    }

    private void addEvent() {
        List<Classes> danhSachClass = getAllClass();
        ArrayAdapter<Classes> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, danhSachClass);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnIdClass.setAdapter(adapter);
        btnAdd.setOnClickListener(new View.OnClickListener() {
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
    private void cancel() {
        Intent i = new Intent();
        setResult(Activity.RESULT_OK, i);
        finish();
    }

    private void addControls() {
        edtCode = (EditText) findViewById(R.id.edtAddStudentCode);
        edtName = (EditText) findViewById(R.id.edtAddStudentName);
        edtDob = (EditText) findViewById(R.id.edtAddStudentDob);
        edtAddress = (EditText) findViewById(R.id.edtAddStudentAddress);
        rbtnMale = (RadioButton) findViewById(R.id.rbtnMale);
        rbtnFemale = (RadioButton) findViewById(R.id.rbtnFemale);
        spnIdClass = (Spinner) findViewById(R.id.spnAddClassStudent);
        btnAdd = (Button) findViewById(R.id.buttonAddStudent);
        ibtnBack = (ImageButton) findViewById(R.id.ibtnBack);
    }

    //Dialog Add
    private void DialogAdd() {

        //Tạo đối tượng cửa sổ dialog
        Dialog dialog  =  new Dialog(this);

        //Nạp layout vào
        dialog.setContentView(R.layout.dialogaddstudent);

        //Click No mới thoát, click ngoài ko thoát
        dialog.setCanceledOnTouchOutside(false);

        //Ánh xạ
        Button btnYes = dialog.findViewById(R.id.buttonYesAddStudent);
        Button btnNo = dialog.findViewById(R.id.buttonNoAddStudent);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = edtName.getText().toString();
                String code = edtCode.getText().toString();
                String dob = edtDob.getText().toString();
                String address = edtAddress.getText().toString();
                String gender="";
                if(rbtnMale.isChecked()){
                    gender =  "Male";
                }
                else if(rbtnFemale.isChecked()){
                    gender = "Female";
                }

                if(name.equals("") || code.equals("") || dob.equals("") || gender.equals("") || address.equals("")){
                    Toast.makeText(ActivityAddStudent.this,"Chưa nhập đủ thông tin",Toast.LENGTH_SHORT).show();
                }
                else {
                    insert();
                    //Khởi tạo lại activity main
                    Intent intent = new Intent(ActivityAddStudent.this,ActivityStudent.class);
                    //finish();
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                    Toast.makeText(ActivityAddStudent.this,"Thêm thành công",Toast.LENGTH_SHORT).show();
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
    private void insert(){
        String code = edtCode.getText().toString();
        String name = edtName.getText().toString();
        String dob = edtDob.getText().toString();
        String gender="";
        if(rbtnMale.isChecked()){
            gender =  "Male";
        }
        else if(rbtnFemale.isChecked()){
            gender = "Female";
        }
        String address = edtAddress.getText().toString();
        int classid = getIdClassFromSpinner(spnIdClass);

        ContentValues contentValues = new ContentValues();
        contentValues.put("code", code);
        contentValues.put("name", name);
        contentValues.put("dob", dob);
        contentValues.put("gender", gender);
        contentValues.put("address", address);
        contentValues.put("classid", classid);

        SQLiteDatabase database = DatabaseHelper.initDatabase(this, DATABASE_NAME);
        database.insert("student", null, contentValues);
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