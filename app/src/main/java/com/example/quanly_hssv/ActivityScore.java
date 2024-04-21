package com.example.quanly_hssv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import androidx.appcompat.widget.Toolbar;

import com.example.quanly_hssv.adapter.adapterScore;
import com.example.quanly_hssv.database.DatabaseHelper;
import com.example.quanly_hssv.model.Student;

import java.util.ArrayList;

public class ActivityScore extends AppCompatActivity {

    final String DATABASE_NAME = "studentmanagement";
    int YOUR_REQUEST_CODE = 123;
    Toolbar toolbarScore;
    ListView listView;
    SQLiteDatabase database;
    ArrayList<Student> list;
    adapterScore adapter;
    int counter = 0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        toolbarScore = findViewById(R.id.toolbarScore);

        setSupportActionBar(toolbarScore);

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
        displayScoreList(); // Cập nhật dữ liệu khi Activity resumed
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == YOUR_REQUEST_CODE && resultCode == RESULT_OK) {
            displayScoreList(); // Cập nhật dữ liệu khi quay lại từ AddActivityTV
        }
    }

    private void addControls() {
        listView = findViewById(R.id.listviewScoreStudent);
        list = new ArrayList<>();
        adapter = new adapterScore(this, list);
        listView.setAdapter(adapter);
    }

    private void displayScoreList() {
        database = DatabaseHelper.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT studentid, name, code FROM student", null);
        list.clear();
        for(int i = 0; i < cursor.getCount(); i++){
            cursor.moveToPosition(i);
            int studentid = cursor.getInt(0);
            String name = cursor.getString(1);
            String code = cursor.getString(2);
            list.add(new Student(studentid, code, name));
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
            Intent intent = new Intent(ActivityScore.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }
}