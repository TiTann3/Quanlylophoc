
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
import android.widget.Spinner;
import android.widget.Toast;

import com.example.quanly_hssv.database.DatabaseHelper;
import com.example.quanly_hssv.model.Classes;
import com.example.quanly_hssv.model.Subject;

import java.util.ArrayList;
import java.util.List;

public class ActivityAddScore extends AppCompatActivity {
    private static String DATABASE_NAME = "studentmanagement";
    Button btnAdd;
    EditText edtScoreValue;
    Spinner spnSubject;
    ImageButton ibtnBack;
    int id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_score);
        addControls();
        addEvent();
    }

    private void addControls() {
        edtScoreValue = (EditText) findViewById(R.id.edtScoreValue);
        btnAdd = (Button) findViewById(R.id.btnAddScore);
        spnSubject = (Spinner) findViewById(R.id.spnSubjectNameScore);
        ibtnBack = (ImageButton) findViewById(R.id.ibtnBack);
    }

    private void addEvent() {
        List<Subject> danhSachSubject = getAllSubject();
        ArrayAdapter<Subject> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, danhSachSubject);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSubject.setAdapter(adapter);
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

    private void DialogAdd() {
        // Tạo đối tượng cửa sổ dialog
        Dialog dialog = new Dialog(this);

        // Nạp layout vào
        dialog.setContentView(R.layout.dialogaddstudent);

        // Click No mới thoát, click ngoài ko thoát
        dialog.setCanceledOnTouchOutside(false);

        // Ánh xạ
        Button btnYes = dialog.findViewById(R.id.buttonYesAddStudent);
        Button btnNo = dialog.findViewById(R.id.buttonNoAddStudent);
        // Bắt sự kiện khi nhấn nút Yes
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String scoreValue = edtScoreValue.getText().toString().trim();
                if (scoreValue.isEmpty()) {
                    Toast.makeText(ActivityAddScore.this, "Chưa nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    float scoreCheck = Float.parseFloat(scoreValue);
                    if (scoreCheck < 0.0 || scoreCheck > 10.0) {
                        Toast.makeText(ActivityAddScore.this, "Điểm phải từ 0 đến 10", Toast.LENGTH_SHORT).show();
                    } else {
                        insert();
                    }
                }
                dialog.dismiss(); // Đóng dialog sau khi xử lý xong
            }
        });

        // Nếu không muốn thêm điểm, đóng dialog
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // Show dialog lên activity
        dialog.show();
    }


    private void insert() {
        Intent i = getIntent();
        id = i.getIntExtra("ID", -1);
        int subjectid = getIdSubjectFromSpinner(spnSubject);
        float score = Float.parseFloat(edtScoreValue.getText().toString());

        if (isSubjectIdExist(subjectid)) {
            Toast.makeText(ActivityAddScore.this, "Môn học đã tồn tại", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put("studentid", id);
        contentValues.put("subjectid", subjectid);
        contentValues.put("scorevalue", score);
        SQLiteDatabase database = DatabaseHelper.initDatabase(this, DATABASE_NAME);

        long result = database.insert("scores", null, contentValues);
        if (result == -1) {
            Toast.makeText(ActivityAddScore.this, "Không thể thêm dữ liệu", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(ActivityAddScore.this, ActivityInformationScore.class);
            intent.putExtra("ID", id);
            setResult(Activity.RESULT_OK, intent);
            finish();
            Toast.makeText(ActivityAddScore.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean isSubjectIdExist(int idSubject) {
        SQLiteDatabase database = DatabaseHelper.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM scores WHERE subjectid = ? AND studentid = ?",
                new String[]{String.valueOf(idSubject), String.valueOf(id)});
        boolean exist = cursor.getCount() > 0;
        cursor.close();
        return exist;
    }

    public List<Subject> getAllSubject() {
        List<Subject> subjectList = new ArrayList<>();
        SQLiteDatabase db = DatabaseHelper.initDatabase(this, DATABASE_NAME);
        Cursor cursor = db.rawQuery("SELECT subjectid, name FROM subject", null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int subjectID = cursor.getInt(cursor.getColumnIndex("subjectid"));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                Subject subject = new Subject(subjectID, name);
                subjectList.add(subject);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return subjectList;
    }

    public int getIdSubjectFromSpinner(Spinner spinner) {
        // Lấy vị trí của item đã chọn trong Spinner
        int selectedPosition = spinner.getSelectedItemPosition();

        // Lấy dữ liệu từ Spinner theo vị trí đã chọn
        Subject selectedTen = (Subject) spinner.getItemAtPosition(selectedPosition);

        // Trả về id của CLB đã chọn từ Spinner
        return selectedTen.getSubjectid();
    }
}