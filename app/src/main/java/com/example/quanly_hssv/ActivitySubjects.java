package com.example.quanly_hssv;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.quanly_hssv.adapter.adapterSubjects;
import com.example.quanly_hssv.database.DatabaseHelper;
import com.example.quanly_hssv.model.Subject;

import java.util.ArrayList;

public class ActivitySubjects extends AppCompatActivity {

    final String DATABASE_NAME = "studentmanagement";
    SQLiteDatabase database;
    int YOUR_REQUEST_CODE = 123;

    ListView listView;

    ArrayList<Subject> list;
    adapterSubjects adapter;
    Toolbar toolbarSubject;

    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);

        toolbarSubject = findViewById(R.id.toolbarSubject);

        setSupportActionBar(toolbarSubject);

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
        ImageButton ibtnAddSubject = (ImageButton) findViewById(R.id.ibtnAddSubject);
        ibtnAddSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivitySubjects.this, ActivityAddSubjects.class);
                startActivity(intent);
            }
        });
        listView = findViewById(R.id.listviewSubject);
        list = new ArrayList<>();
        adapter = new adapterSubjects(this, list);
        listView.setAdapter(adapter);
    }
    private void readData() {
        database = DatabaseHelper.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM subject", null);
        list.clear();
        for(int i = 0; i < cursor.getCount(); i++){
            cursor.moveToPosition(i);
            int subjectid = cursor.getInt(0);
            String name = cursor.getString(1);
            String decription = cursor.getString(2);
            int credits = cursor.getInt(3);
            int time = cursor.getInt(4);
            list.add(new Subject(subjectid, name, decription, credits, time));
        }
        adapter.notifyDataSetChanged();
        database.close();
    }

//    }
//    public void information(final int pos){
//
//
//        Cursor cursor = database.getDataSubjects();
//        while (cursor.moveToNext()) {
//            int id = cursor.getInt(0);
//            if (id == pos) {
//                Intent intent = new Intent(ActivitySubjects.this, ActivityInformationSubject.class);
//                intent.putExtra("id", pos);
//
//                String title = cursor.getString(1);
//                int credits = cursor.getInt(2);
//                String time = cursor.getString(3);
//                String place = cursor.getString(4);
//
//                intent.putExtra("title", title);
//                intent.putExtra("credit", credits);
//                intent.putExtra("time", time);
//                intent.putExtra("place", place);
//
//                startActivity(intent);
//            }
//        }
//
//    }
//
//    //Dialog Update
//    private void DialogDeleteSubject(int position) {
//
//        //Tạo đối tượng cửa sổ dialog
//        Dialog dialog  =  new Dialog(this);
//
//        //Nạp layout vào
//        dialog.setContentView(R.layout.dialogdeletesubject);
//
//        //Click No mới thoát, click ngoài ko thoát
//        dialog.setCanceledOnTouchOutside(false);
//
//        //Ánh xạ
//        Button btnYes = dialog.findViewById(R.id.buttonYesDeleteSubject);
//        Button btnNo = dialog.findViewById(R.id.buttonNoDeleteSubject);
//
//        btnYes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                database = new database(ActivitySubjects.this);
//                //Xóa trong SQL
//                database.DeleteSubject(position);
//                database.DeleteSubjectStudent(position);
//                //Cập nhật lại listview
//                Intent intent = new Intent(ActivitySubjects.this,ActivitySubjects.class);
//                startActivity(intent);
//
//            }
//        });
//        //Nếu no thì đóng dialog
//        btnNo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.cancel();
//            }
//        });
//        //show dialog lên activity
//        dialog.show();
//    }
//
//
    //Nhấn nút back ở activity này thì chuyển qua activity được thiết lập riêng
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
            Intent intent = new Intent(ActivitySubjects.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }

}