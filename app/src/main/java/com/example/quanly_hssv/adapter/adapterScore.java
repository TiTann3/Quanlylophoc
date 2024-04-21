package com.example.quanly_hssv.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.quanly_hssv.ActivityInformationScore;
import com.example.quanly_hssv.R;
import com.example.quanly_hssv.model.Student;

import java.util.ArrayList;

public class adapterScore extends BaseAdapter{

    Activity context;
    ArrayList<Student> list;

    public adapterScore(Activity context, ArrayList<Student> list) {
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
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.liststudentcore, null);

        TextView txtName = (TextView) view.findViewById(R.id.txtStudentName);
        TextView txtcode = (TextView) view.findViewById(R.id.txtCode);

        ImageButton imgbtnInformation = (ImageButton) view.findViewById(R.id.studentinformation);

        Student student = list.get(i);

        txtName.setText(student.studentname);
        txtcode.setText(student.studentcode);

        imgbtnInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ActivityInformationScore.class);
                i.putExtra("ID", student.studentid);
                context.startActivity(i);
            }
        });
        return view;
    }
}
