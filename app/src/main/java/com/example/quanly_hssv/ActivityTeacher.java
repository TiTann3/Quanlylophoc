package com.example.quanly_hssv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.quanly_hssv.adapter.adapterSubjects;
import com.example.quanly_hssv.adapter.adapterTeacher;
import com.example.quanly_hssv.database.DatabaseHelper;
import com.example.quanly_hssv.model.Subject;
import com.example.quanly_hssv.model.Teacher;

import java.util.ArrayList;

public class ActivityTeacher extends AppCompatActivity {

    final String DATABASE_NAME = "studentmanagement";
    int YOUR_REQUEST_CODE = 123;
    SQLiteDatabase database;

    ListView listView;

    ArrayList<Teacher> list;
    adapterTeacher adapter;
    Toolbar toolbarTeacher;
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        toolbarTeacher = findViewById(R.id.toolbarTeacher);

        setSupportActionBar(toolbarTeacher);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addControls();
        if (savedInstanceState != null) {
            int index = savedInstanceState.getInt("listViewIndex");
            int top = savedInstanceState.getInt("listViewTop");

            listView.setSelectionFromTop(index, top);
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Lưu trạng thái của ListView, ví dụ: vị trí hiện tại của mục đang được chọn
        int index = listView.getFirstVisiblePosition();
        View v = listView.getChildAt(0);
        int top = (v == null) ? 0 : (v.getTop() - listView.getPaddingTop());

        outState.putInt("listViewIndex", index);
        outState.putInt("listViewTop", top);
    }

    protected void onResume() {
        super.onResume();
        readData(); // Cập nhật dữ liệu khi Activity resumed
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == YOUR_REQUEST_CODE && resultCode == RESULT_OK) {
            readData(); // Cập nhật dữ liệu khi quay lại từ AddActivityTV
        }
    }

    private void addControls() {
        listView = findViewById(R.id.listviewTeacher);
        list = new ArrayList<>();
        adapter = new adapterTeacher(this, list);
        listView.setAdapter(adapter);
        ImageButton ibntAdd = (ImageButton) findViewById(R.id.ibtnAddTeacher);
        ibntAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ActivityTeacher.this, ActicityAddTeacher.class);
                startActivity(i);
            }
        });
    }
    private void readData() {
        database = DatabaseHelper.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM teacher", null);
        list.clear();
        for(int i = 0; i < cursor.getCount(); i++){
            cursor.moveToPosition(i);
            int teacherid = cursor.getInt(0);
            String teachername = cursor.getString(1);
            String dob = cursor.getString(2);
            String gender = cursor.getString(3);
            list.add(new Teacher(teacherid, teachername, dob, gender));
        }
        adapter.notifyDataSetChanged();
        database.close();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Xử lý khi nút "Back" trên Toolbar được nhấn
                handleBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void handleBackPressed() {
        counter++;
        if (counter == 1) {
            Intent intent = new Intent(ActivityTeacher.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }

}