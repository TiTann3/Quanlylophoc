package com.example.quanly_hssv.adapter;

import android.app.Activity;
import android.app.AlertDialog;
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

import com.example.quanly_hssv.ActivityInformationClass;
import com.example.quanly_hssv.ActivityInformationStudent;
import com.example.quanly_hssv.ActivityUpdateClass;
import com.example.quanly_hssv.R;
import com.example.quanly_hssv.database.DatabaseHelper;
import com.example.quanly_hssv.model.Classes;

import java.util.ArrayList;

public class adapterClasses extends BaseAdapter {
    Activity context;
    ArrayList<Classes> list;

    public adapterClasses(Activity context, ArrayList<Classes> list) {
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
        view = inflater.inflate(R.layout.listclass, null);

        TextView txtClassName = (TextView) view.findViewById(R.id.txtClassName);
        TextView txtClassCourse = (TextView) view.findViewById(R.id.txtCourse);
        ImageButton ibtnDelete = (ImageButton) view.findViewById(R.id.classdelete);
        ImageButton ibtnUpdate = (ImageButton) view.findViewById(R.id.classupdate);
        ImageButton ibtnInfor = (ImageButton) view.findViewById(R.id.classinformation);

        final Classes classes = list.get(i);

        txtClassName.setText(classes.className);
        txtClassCourse.setText(classes.classCourse);

        ibtnInfor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ActivityInformationClass.class);
                i.putExtra("ID", classes.classId);
                context.startActivity(i);
            }
        });

        ibtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(android.R.drawable.ic_delete);
                builder.setTitle("Xác nhận xóa");
                builder.setMessage("Bạn có chắc chắn muốn xóa lớp học này ?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        delete(classes.classId);
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
        ibtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ActivityUpdateClass.class);
                i.putExtra("ClassID", classes.classId);
                context.startActivity(i);
            }
        });
        return view;
    }
    private void delete(int classId) {
        SQLiteDatabase database = DatabaseHelper.initDatabase(context, "studentmanagement");
        int deletedRows = database.delete("class", "classid = ?", new String[]{classId + ""});
        if (deletedRows > 0) {
            Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
            list.clear();
            Cursor cursor = database.rawQuery("SELECT * FROM class", null);
            while(cursor.moveToNext()){
                int classid = cursor.getInt(0);
                String name = cursor.getString(1);
                String course = cursor.getString(2);
                list.add(new Classes(classid, name, course));
            }
            notifyDataSetChanged();
        } else {
            // Xóa không thành công
            Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
        }
    }
}
