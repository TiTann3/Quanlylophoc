package com.example.quanly_hssv.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.quanly_hssv.ActivityInformationStudent;
import com.example.quanly_hssv.ActivityInformationSubject;
import com.example.quanly_hssv.ActivityStudent;
import com.example.quanly_hssv.ActivityUpdateStudent;
import com.example.quanly_hssv.R;
import com.example.quanly_hssv.database.DatabaseHelper;
import com.example.quanly_hssv.model.Student;

import java.util.ArrayList;

public class adapterStudent extends BaseAdapter {

    Activity context;
    ArrayList<Student> list;

    public adapterStudent(Activity context, ArrayList<Student> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(R.layout.liststudent, null);

        TextView txtName = (TextView) convertView.findViewById(R.id.txtStudentName);
        TextView txtcode = (TextView) convertView.findViewById(R.id.txtCode);

        ImageButton imgbtnDelete = (ImageButton) convertView.findViewById(R.id.studentdelete);
        ImageButton imgbtnUpdate = (ImageButton) convertView.findViewById(R.id.studentupdate);
        ImageButton imgbtnInformation = (ImageButton) convertView.findViewById(R.id.studentinformation);

        Student student = list.get(position);

        txtName.setText(student.studentname);
        txtcode.setText(student.studentcode);

        imgbtnInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ActivityInformationStudent.class);
                i.putExtra("ID", student.getStudentid());
                context.startActivity(i);
            }
        });

        imgbtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(android.R.drawable.ic_delete);
                builder.setTitle("Xác nhận xóa");
                builder.setMessage("Bạn có chắc chắn muốn xóa học sinh này ?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        delete(student.studentid);
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        imgbtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ActivityUpdateStudent.class);
                i.putExtra("ID", student.studentid);
                context.startActivity(i);
            }
        });
        return convertView;
    }

    private void delete(int studentId) {
        SQLiteDatabase database = DatabaseHelper.initDatabase(context, "studentmanagement");
        int result = database.delete("student", "studentid = ?", new String[]{studentId+""});

        if (result > 0) {
            Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
            list.clear();
            Cursor cursor = database.rawQuery("SELECT * FROM student", null);
            while(cursor.moveToNext()){
                int idstudent = cursor.getInt(0);
                String code = cursor.getString(1);
                String name = cursor.getString(2);
                String dob = cursor.getString(3);
                String gender = cursor.getString(4);
                String address = cursor.getString(5);
                list.add(new Student(idstudent, code, name, dob, gender, address));
            }
            notifyDataSetChanged();
            notifyDataSetChanged();
        } else {
            Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
        }
    }
}
