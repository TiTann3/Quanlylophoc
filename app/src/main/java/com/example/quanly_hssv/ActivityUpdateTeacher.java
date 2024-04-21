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

import java.util.ArrayList;
import java.util.List;

public class ActivityUpdateTeacher extends AppCompatActivity {

    final String DATABASE_NAME = "studentmanagement";
    EditText edtName, edtDOB, edtGender;
    Button btnUpdate;
    Spinner spnClassId;
    ImageButton ibtnBack;
    RadioButton rbtnMale,rbtnFemale;
    int id=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_teacher);

        addControls();
        addEvent();
        initUI();

    }

    private void initUI() {
        Intent i = getIntent();
        id = i.getIntExtra("TeacherID", -1);
        SQLiteDatabase database = DatabaseHelper.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM teacher where teacherid = ?", new String[]{id+""});
        cursor.moveToFirst();
        String name = cursor.getString(1);
        String dob = cursor.getString(2);
        String gender = cursor.getString(3);
        int idClass = cursor.getInt(4);

        edtName.setText(name);
        edtDOB.setText(dob);
        if(gender.equals("Male")){
            rbtnMale.setChecked(true);
        }else {
            rbtnFemale.setChecked(true);
        }
        spnClassId.setSelection(idClass-1);
    }

    private void addEvent() {
        List<Classes> danhSachClass = getAllClass();
        ArrayAdapter<Classes> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, danhSachClass);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnClassId.setAdapter(adapter);
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

    private void addControls() {
        edtName = (EditText) findViewById(R.id.edtUpdateTeacherName);
        edtDOB = (EditText) findViewById(R.id.edtUpdateDOBTeacher);
        rbtnMale = (RadioButton) findViewById(R.id.rbtnMale);
        rbtnFemale = (RadioButton) findViewById(R.id.rbtnFemale);
        btnUpdate = (Button) findViewById(R.id.btnUpdateTeacher);
        spnClassId = (Spinner) findViewById(R.id.spnUpdateClass);
        ibtnBack = (ImageButton) findViewById(R.id.ibtnBack);
    }
    private void update(){
        String name = edtName.getText().toString();
        String dob = edtDOB.getText().toString();
        String gender="";
        if(rbtnMale.isChecked()){
            gender =  "Male";
        }
        else {
            gender = "Female";
        }
        int classid = getIdClassFromSpinner(spnClassId);

        if (isClassIdExist(classid)) {
            Toast.makeText(ActivityUpdateTeacher.this, "Lớp đã có GVCN", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("dob", dob);
        contentValues.put("gender", gender);
        contentValues.put("classid", classid);
        SQLiteDatabase database = DatabaseHelper.initDatabase(this, DATABASE_NAME);
        long result = database.update("teacher", contentValues, "teacherid = ?", new String[]{id+""});
        if (result == -1) {
            Toast.makeText(ActivityUpdateTeacher.this, "Không thể cập nhật dữ liệu", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(ActivityUpdateTeacher.this, ActivityTeacher.class);
            setResult(Activity.RESULT_OK, intent);
            finish();
            Toast.makeText(ActivityUpdateTeacher.this, "Cập Nhật Thành Công", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isClassIdExist(int idclass) {
        SQLiteDatabase database = DatabaseHelper.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM teacher WHERE classid = ?", new String[]{String.valueOf(idclass)});
        boolean exist = cursor.getCount() > 0;
        cursor.close();
        return exist;
    }

    private void DialogUpdate() {

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
                String dob = edtDOB.getText().toString();
                String gender = edtGender.getText().toString();


                if(name.equals("") || dob.equals("") || gender.equals("")){
                    Toast.makeText(ActivityUpdateTeacher.this,"Chưa nhập đủ thông tin",Toast.LENGTH_SHORT).show();
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