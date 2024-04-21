package com.example.quanly_hssv;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.quanly_hssv.adapter.adapterStudent;
import com.example.quanly_hssv.database.DatabaseHelper;
import com.example.quanly_hssv.model.Student;

import java.util.ArrayList;

public class ActivityStudent extends AppCompatActivity {

    final String DATABASE_NAME = "studentmanagement";
    int YOUR_REQUEST_CODE = 123;
    Toolbar toolbarStudent;
    ListView listView;
    SQLiteDatabase database;

    ArrayList<Student> list;
    adapterStudent adapter;
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        toolbarStudent = findViewById(R.id.toolbarStudent);

        setSupportActionBar(toolbarStudent);

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
        ImageButton ibtnAddStudent = (ImageButton) findViewById(R.id.ibtnAddStudent);
        ibtnAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ActivityStudent.this, ActivityAddStudent.class);
                startActivity(i);
            }
        });
        listView = findViewById(R.id.listviewStudent);
        list = new ArrayList<>();
        adapter = new adapterStudent(this, list);
        listView.setAdapter(adapter);
    }

    private void readData() {
        database = DatabaseHelper.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM student", null);
        list.clear();
        for(int i = 0; i < cursor.getCount(); i++){
            cursor.moveToPosition(i);
            int studentid = cursor.getInt(0);
            String code = cursor.getString(1);
            String name = cursor.getString(2);
            String dob = cursor.getString(3);
            String gender = cursor.getString(4);
            String address = cursor.getString(5);
            list.add(new Student(studentid, code, name, dob, gender, address));
        }
        adapter.notifyDataSetChanged();
        database.close();
    }

    //Nạp một menu add vào actionbar
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menuaddstudent,menu);
//        return true;
//    }
//    //Bắt sự kiện khi click vào Add
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId())
//        {
//            case R.id.menuAddStudent:
//                //Chuyển tới màn hình thêm môn học
//                Intent intent = new Intent(this,ActivityAddStudent.class);
//                intent.putExtra("id_subject",id_subject);
//                startActivity(intent);
//                break;
//            default:
//                Intent intent1 = new Intent(this,ActivitySubjects.class);
//                startActivity(intent1);
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    //Phương thức xóa student
//    public void delete(final int id_student){
//        DialogDeleteStudent(id_student);
//    }
//
//    public void update(final int id_student){
//
//        Cursor cursor = database.getDataStudent(id_subject);
//
//        while (cursor.moveToNext()){
//
//            int id = cursor.getInt(0);
//
//            if(id == id_student){
//
//                Intent intent = new Intent(ActivityStudent.this,ActivityUpdateStudent.class);
//
//                String name = cursor.getString(1);
//                String sex = cursor.getString(2);
//                String code = cursor.getString(3);
//                String birthday = cursor.getString(4);
//
//                intent.putExtra("id",id_student);
//                intent.putExtra("name", name);
//                intent.putExtra("sex", sex);
//                intent.putExtra("code", code);
//                intent.putExtra("birthday", birthday);
//                intent.putExtra("id_subject",id_subject);
//
//                startActivity(intent);
//            }
//        }
//
//
//    }
//    public void information(final int pos){
//
//
//        Cursor cursor = database.getDataStudent(id_subject);
//        while (cursor.moveToNext()) {
//            int id = cursor.getInt(0);
//            if (id == pos) {
//                Intent intent = new Intent(ActivityStudent.this, ActivityInformationStudent.class);
//                intent.putExtra("id", pos);
//
//                String name = cursor.getString(1);
//                String sex = cursor.getString(2);
//                String code = cursor.getString(3);
//                String birthday = cursor.getString(4);
//                int id_subject = cursor.getInt(5);
//
//                intent.putExtra("name", name);
//                intent.putExtra("sex", sex);
//                intent.putExtra("code", code);
//                intent.putExtra("birthday", birthday);
//                intent.putExtra("id_subject",id_subject);
//
//                startActivity(intent);
//            }
//        }
//
//    }
//
//    //Dialog delete
//    private void DialogDeleteStudent(int id_student) {
//
//        //Tạo đối tượng cửa sổ dialog
//        Dialog dialog  =  new Dialog(this);
//
//        //Nạp layout vào
//        dialog.setContentView(R.layout.dialogdeletestudent);
//
//        //Click No mới thoát, click ngoài ko thoát
//        dialog.setCanceledOnTouchOutside(false);
//
//        //Ánh xạ
//        Button btnYes = dialog.findViewById(R.id.buttonYesDeleteStudent);
//        Button btnNo = dialog.findViewById(R.id.buttonNoDeleteStudent);
//
//        btnYes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                database = new database(ActivityStudent.this);
//                //Xóa trong SQL
//                database.DeleteStudent(id_student);
//                //Cập nhật lại listview
//                Intent intent = new Intent(ActivityStudent.this,ActivityStudent.class);
//                intent.putExtra("id_subject",id_subject);
//                startActivity(intent);
//
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
            Intent intent = new Intent(ActivityStudent.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }
}