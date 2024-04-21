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
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanly_hssv.database.DatabaseHelper;
import com.example.quanly_hssv.model.Subject;
import com.example.quanly_hssv.model.Teacher;

import java.util.ArrayList;
import java.util.List;

public class ActivityAddSubjects extends AppCompatActivity {

    private static String DATABASE_NAME = "studentmanagement";
    Button btnAddSubjects;
    EditText edtAddSubjectName, edtAddDescription, edtAddCredit, edtAddTime;
    Spinner spnAddTeacher;
    ImageButton ibtnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subjects);
        addControls();
        addEvent();

    }

    private void addControls(){
        btnAddSubjects = findViewById(R.id.btnAddSubjects);
        edtAddSubjectName = findViewById(R.id.edtAddNameSubject);
        edtAddDescription = findViewById(R.id.edtAddDecription);
        edtAddCredit = findViewById(R.id.edtAddCredit);
        edtAddTime = findViewById(R.id.edtAddTime);
        spnAddTeacher = findViewById(R.id.spnAddTeacher);
        ibtnBack = (ImageButton) findViewById(R.id.ibtnBack);
    }
    private void addEvent(){
        List<Teacher> danhSachTeacher = getAllTeacher();
        ArrayAdapter<Teacher> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, danhSachTeacher);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnAddTeacher.setAdapter(adapter);
        btnAddSubjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    //Dialog Add
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
                String subjectName = edtAddSubjectName.getText().toString();
                String description = edtAddDescription.getText().toString();
                String credits = edtAddCredit.getText().toString();
                String time = edtAddTime.getText().toString();

                if(subjectName.equals("") || description.equals("")|| credits.equals("") || time.equals("") ){
                    Toast.makeText(ActivityAddSubjects.this,"Chưa nhập đủ thông tin",Toast.LENGTH_SHORT).show();
                }
                else {
                    insert();
                    //Khởi tạo lại activity main
                    Intent intent = new Intent(ActivityAddSubjects.this,ActivitySubjects.class);
                    //finish();
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                    Toast.makeText(ActivityAddSubjects.this,"Thêm thành công",Toast.LENGTH_SHORT).show();
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

        String subjectName = edtAddSubjectName.getText().toString();
        String description = edtAddDescription.getText().toString();
        int credits = Integer.parseInt(edtAddCredit.getText().toString());
        int time = Integer.parseInt(edtAddTime.getText().toString());
        int idteacher =getIdTeacherFromSpinner(spnAddTeacher);

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", subjectName);
        contentValues.put("decription", description);
        contentValues.put("credits" ,credits);
        contentValues.put("time", time);
        contentValues.put("teacherid", idteacher);

        SQLiteDatabase database = DatabaseHelper.initDatabase(this, DATABASE_NAME);
        database.insert("subject", null, contentValues);

    }
    public List<Teacher> getAllTeacher() {
        List<Teacher> teacherList = new ArrayList<>();
        SQLiteDatabase db = DatabaseHelper.initDatabase(this, DATABASE_NAME);
        Cursor cursor = db.rawQuery("SELECT teacherid, name FROM teacher", null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int teacherID = cursor.getInt(cursor.getColumnIndex("teacherid"));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                Teacher teacher = new Teacher(teacherID, name);
                teacherList.add(teacher);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return teacherList;
    }

    public int getIdTeacherFromSpinner(Spinner spinner) {
        // Lấy vị trí của item đã chọn trong Spinner
        int selectedPosition = spinner.getSelectedItemPosition();

        // Lấy dữ liệu từ Spinner theo vị trí đã chọn
        Teacher selectedHoten = (Teacher) spinner.getItemAtPosition(selectedPosition);

        // Trả về id của CLB đã chọn từ Spinner
        return selectedHoten.getTeacherid();
    }

}