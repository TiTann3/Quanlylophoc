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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.quanly_hssv.ActivityUpdateSubject;
import com.example.quanly_hssv.ActivityUpdateTeacher;
import com.example.quanly_hssv.R;
import com.example.quanly_hssv.database.DatabaseHelper;
import com.example.quanly_hssv.model.Subject;
import com.example.quanly_hssv.model.Teacher;

import java.util.ArrayList;

public class adapterTeacher extends BaseAdapter {

    private Activity context;
    private ArrayList<Teacher> list;

    public adapterTeacher(Activity context, ArrayList<Teacher> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.listteacher, null);

        TextView txtTeacherName = (TextView) view.findViewById(R.id.txtTeacherName);
        TextView txtDOB = (TextView) view.findViewById(R.id.txtDOB);
        TextView txtGender = (TextView) view.findViewById(R.id.txtGender);
        ImageButton imgbtnDelete = (ImageButton) view.findViewById(R.id.teacherdelete);
        ImageButton imgbtnUpdate = (ImageButton) view.findViewById(R.id.teacherupdate);

        final Teacher teacher = list.get(i);

        txtTeacherName.setText(teacher.teachername);
        txtDOB.setText(teacher.dob);
        txtGender.setText(teacher.gender);
        imgbtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(android.R.drawable.ic_delete);
                builder.setTitle("Xác nhận xóa");
                builder.setMessage("Bạn có chắc chắn muốn xóa giáo viên này ?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        delete(teacher.getTeacherid());
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
            public void onClick(View view) {
                Intent i = new Intent(context, ActivityUpdateTeacher.class);
                i.putExtra("TeacherID", teacher.teacherid);
                context.startActivity(i);
            }
        });

        return view;
    }
    private void delete(int teacherId) {
        SQLiteDatabase database = DatabaseHelper.initDatabase(context, "studentmanagement");
        database.delete("teacher", "teacherid = ?", new String[]{teacherId + ""});
        list.clear();
        Cursor cursor = database.rawQuery("SELECT * FROM teacher", null);
        while(cursor.moveToNext()){
            int idteacher = cursor.getInt(0);
            String name = cursor.getString(1);
            String dob = cursor.getString(2);
            String gender = cursor.getString(3);
            list.add(new Teacher(idteacher, name, dob, gender));
        }
        notifyDataSetChanged();
    }
}
