package com.example.quanly_hssv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.example.quanly_hssv.adapter.adapterInformationScore;
import com.example.quanly_hssv.database.DatabaseHelper;
import com.example.quanly_hssv.model.Student;

import java.util.ArrayList;

public class ActivityInformationScore extends AppCompatActivity {

    final String DATABASE_NAME = "studentmanagement";

    int YOUR_REQUEST_CODE = 123;
    Toolbar toolbarScoreInfor;

    ListView listView;
    ArrayList<Student> list;
    SQLiteDatabase database;
    adapterInformationScore adapter;
    int counter = 0;
    int id = -1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_score);

        toolbarScoreInfor = findViewById(R.id.toolbarScoreInfor);

        setSupportActionBar(toolbarScoreInfor);

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
        listView = findViewById(R.id.listviewScoreInfor);
        list = new ArrayList<>();
        adapter = new adapterInformationScore(this, list);
        listView.setAdapter(adapter);
    }

    private void readData() {
        Intent i = getIntent();
        id = i.getIntExtra("ID", -1);
        ImageButton ibtnAdd = (ImageButton) findViewById(R.id.ibtnAddScore);
        ibtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ActivityInformationScore.this, ActivityAddScore.class);
                i.putExtra("ID", id);
                startActivity(i);
            }
        });
        database = DatabaseHelper.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT a.scorevalue, b.name, b.subjectid " +
                "from scores a, subject b, student c " +
                "where a.subjectid = b.subjectid and a.studentid = c.studentid and a.studentid = ?",new String[]{id+""});
        list.clear();
        for(int j = 0; j < cursor.getCount(); j++){
            cursor.moveToPosition(j);
            float scorevalue = cursor.getFloat(0);
            String name = cursor.getString(1);
            int subjectid = cursor.getInt(2);
            list.add(new Student(name, scorevalue, subjectid, id));
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
            Intent intent = new Intent(ActivityInformationScore.this, ActivityScore.class);
            setResult(Activity.RESULT_OK, intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }
}