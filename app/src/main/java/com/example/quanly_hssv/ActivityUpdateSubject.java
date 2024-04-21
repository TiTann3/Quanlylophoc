package com.example.quanly_hssv;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
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

public class ActivityUpdateSubject extends AppCompatActivity {

    final String DATABASE_NAME = "studentmanagement";

    int id = -1;
    EditText edtName, edtDecription, edtCredit, edtTime;
    Spinner  spnTeacher;
    Button buttonUpdate;
    ImageButton ibtnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_subject);

        addControls();
        addEvents();
        initUI();
    }
    private void initUI(){
        Intent intent = getIntent();
        id = intent.getIntExtra("SubjectID", -1);
        SQLiteDatabase database = DatabaseHelper.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM subject where subjectid = ?", new String[]{id+""});
        cursor.moveToFirst();
        String name =cursor.getString(1);
        String decription = cursor.getString(2);
        int credits = cursor.getInt(3);
        int time = cursor.getInt(4);
        int idteacher = cursor.getInt(5);

        edtName.setText(name);
        edtDecription.setText(decription);
        edtCredit.setText(credits+"");
        edtTime.setText(time+"");
        spnTeacher.setSelection(idteacher-1);
    }

    private void addControls(){
        edtName =(EditText) findViewById(R.id.edtSubjectName);
        edtDecription =(EditText) findViewById(R.id.edtDecription);
        edtCredit =(EditText) findViewById(R.id.edtCredits);
        edtTime =(EditText) findViewById(R.id.edtUpdateTime);
        spnTeacher =(Spinner) findViewById(R.id.spnUpdateTeacher);
        buttonUpdate =(Button) findViewById(R.id.btnUpdateSubject);
        ibtnBack = (ImageButton) findViewById(R.id.ibtnBack);
    }
    private void addEvents(){
        List<Teacher> danhSachTeacher = getAllTeacher();
        ArrayAdapter<Teacher> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, danhSachTeacher);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTeacher.setAdapter(adapter);
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    //Dialog Update
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
                String description = edtDecription.getText().toString();
                String credits = edtCredit.getText().toString();
                String time = edtTime.getText().toString();
                String teacher = spnTeacher.getSelectedItem().toString();


                if(name.equals("") || description.equals("") || credits.equals("") || time.equals("") || teacher.equals("")){
                    Toast.makeText(ActivityUpdateSubject.this,"Chưa nhập đủ thông tin",Toast.LENGTH_SHORT).show();
                }
                else {
                    updatesubject();
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
    private void updatesubject(){

        String name  = edtName.getText().toString();
        String decription  = edtDecription.getText().toString();
        int credits = Integer.parseInt(edtCredit.getText().toString());
        int time = Integer.parseInt(edtTime.getText().toString());
        int teacherid = getIdTeacherFromSpinner(spnTeacher);

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("decription", decription);
        contentValues.put("credits", credits);
        contentValues.put("time", time);
        contentValues.put("teacherid", teacherid);
        SQLiteDatabase database = DatabaseHelper.initDatabase(this, DATABASE_NAME);
        database.update("subject", contentValues, "subjectid = ?", new String[]{id+""});
        Intent intent = new Intent(ActivityUpdateSubject.this,ActivitySubjects.class);
        setResult(Activity.RESULT_OK, intent);
        finish();
        Toast.makeText(ActivityUpdateSubject.this,"Cập Nhật Thành Công",Toast.LENGTH_SHORT).show();

    }
}