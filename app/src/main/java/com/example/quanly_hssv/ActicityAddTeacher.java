package com.example.quanly_hssv;

import androidx.appcompat.app.AppCompatActivity;

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

import com.example.quanly_hssv.database.DatabaseHelper;
import com.example.quanly_hssv.model.Classes;
import com.example.quanly_hssv.model.Teacher;

import java.util.ArrayList;
import java.util.List;

public class ActicityAddTeacher extends AppCompatActivity {

    final String DATABASE_NAME = "studentmanagement";
    EditText edtName, edtDob;
    RadioButton rbtnMale, rbtnFemale;
    Spinner spnIdclass;
    Button btnAddTeacher;
    ImageButton ibtnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teacher);
        addControls();
        addEvent();
    }

    private void addEvent() {
        List<Classes> danhSachClass = getAllClass();
        ArrayAdapter<Classes> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, danhSachClass);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnIdclass.setAdapter(adapter);
        btnAddTeacher.setOnClickListener(new View.OnClickListener() {
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
        edtName = (EditText) findViewById(R.id.edtAddNameTeacher);
        edtDob = (EditText) findViewById(R.id.edtAddDOBTeacher);
        rbtnMale = (RadioButton) findViewById(R.id.rbtnMale);
        rbtnFemale = (RadioButton) findViewById(R.id.rbtnFemale);
        spnIdclass = (Spinner) findViewById(R.id.spnAddClass);
        btnAddTeacher = (Button) findViewById(R.id.btnAddTeacher);
        ibtnBack = (ImageButton) findViewById(R.id.ibtnBack);
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
                String teacherName = edtName.getText().toString();
                String teacherDob =edtDob.getText().toString();
                String teacherGender="";
                if(rbtnMale.isChecked()){
                    teacherGender =  "Male";
                }
                else if(rbtnFemale.isChecked()){
                    teacherGender = "Female";
                }

                if(teacherName.equals("") || teacherDob.equals("")|| teacherGender.equals("")){
                    Toast.makeText(ActicityAddTeacher.this,"Chưa nhập đủ thông tin",Toast.LENGTH_SHORT).show();
                }
                else {
                    insert();
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

    private void insert(){
        String teacherName = edtName.getText().toString();
        String teacherDob = edtDob.getText().toString();
        String teacherGender="";
        if(rbtnMale.isChecked()){
            teacherGender =  "Male";
        }
        else if(rbtnFemale.isChecked()){
            teacherGender = "Female";
        }
        int idclass = getIdClassFromSpinner(spnIdclass);

        if (isClassIdExist(idclass)) {
            Toast.makeText(ActicityAddTeacher.this, "Lớp đã có GVCN", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", teacherName);
        contentValues.put("dob", teacherDob);
        contentValues.put("gender", teacherGender);
        contentValues.put("classid", idclass);

        SQLiteDatabase database = DatabaseHelper.initDatabase(this, DATABASE_NAME);
        long result = database.insert("teacher", null, contentValues);
        if (result == -1) {
            Toast.makeText(ActicityAddTeacher.this, "Không thể thêm dữ liệu", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(ActicityAddTeacher.this,ActivityTeacher.class);
            setResult(Activity.RESULT_OK, intent);
            finish();
            Toast.makeText(ActicityAddTeacher.this,"Thêm thành công",Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isClassIdExist(int idclass) {
        SQLiteDatabase database = DatabaseHelper.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM teacher WHERE classid = ?", new String[]{String.valueOf(idclass)});
        boolean exist = cursor.getCount() > 0;
        cursor.close();
        return exist;
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