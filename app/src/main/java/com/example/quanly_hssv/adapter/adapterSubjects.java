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

import com.example.quanly_hssv.ActivityInformationSubject;
import com.example.quanly_hssv.ActivityUpdateSubject;
import com.example.quanly_hssv.R;
import com.example.quanly_hssv.database.DatabaseHelper;
import com.example.quanly_hssv.model.Subject;

import java.util.ArrayList;

public class adapterSubjects extends BaseAdapter {

    //context
    Activity context;
    ;
    //Mảng subject
    ArrayList<Subject> list;

    public adapterSubjects(Activity context, ArrayList<Subject> list) {
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
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(R.layout.listsubject, null);

        TextView txtSubjectName = (TextView) convertView.findViewById(R.id.txtSubjectName);
        TextView txtCredits = (TextView) convertView.findViewById(R.id.txtCredits);
        ImageButton imgbtnDelete = (ImageButton) convertView.findViewById(R.id.subjectdelete);
        ImageButton imgbtnUpdate = (ImageButton) convertView.findViewById(R.id.subjecupdate);
        ImageButton imgbtnInformation = (ImageButton) convertView.findViewById(R.id.subjecinformation);

        final Subject subject = list.get(position);

        txtSubjectName.setText(subject.subjectname);
        txtCredits.setText(subject.credits + "");

        imgbtnInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ActivityInformationSubject.class);
                i.putExtra("SubjectID", subject.subjectid);
                context.startActivity(i);
            }
        });

        imgbtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(android.R.drawable.ic_delete);
                builder.setTitle("Xác nhận xóa");
                builder.setMessage("Bạn có chắc chắn muốn xóa môn học này ?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        delete(subject.subjectid);
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
                Intent i = new Intent(context, ActivityUpdateSubject.class);
                i.putExtra("SubjectID", subject.subjectid);
                context.startActivity(i);
            }
        });
        return convertView;
    }

    private void delete(int subjectId) {
        SQLiteDatabase database = DatabaseHelper.initDatabase(context, "studentmanagement");
        int result = database.delete("subject", "subjectid = ?", new String[]{subjectId + ""});

        if (result > 0) {
            Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
            list.clear();
            Cursor cursor = database.rawQuery("SELECT * FROM subject", null);
            while (cursor.moveToNext()) {
                int idsubject = cursor.getInt(0);
                String name = cursor.getString(1);
                String decription = cursor.getString(2);
                int credits = cursor.getInt(3);
                int time = cursor.getInt(4);
                list.add(new Subject(idsubject, name, decription, credits, time));
            }
            notifyDataSetChanged();
        } else {
            Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
        }
    }
}
