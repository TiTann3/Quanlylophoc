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

import com.example.quanly_hssv.ActivityUpdateScore;
import com.example.quanly_hssv.R;
import com.example.quanly_hssv.model.Student;

import java.util.ArrayList;

public class adapterInformationScore extends BaseAdapter {

    Activity context;
    ArrayList<Student> list;

    public adapterInformationScore(Activity context, ArrayList<Student> list) {
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
        view = inflater.inflate(R.layout.listscore, null);

        TextView txtSubjectName = (TextView) view.findViewById(R.id.txtSubjectName);
        TextView txtScoreValue = (TextView) view.findViewById(R.id.txtScorevalue);
        ImageButton ibtnupdate = (ImageButton) view.findViewById(R.id.scoreupdate);

        final Student student = list.get(i);
        txtSubjectName.setText(student.getNameSubject());
        txtScoreValue.setText(student.getScorevalue()+"");
        ibtnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ActivityUpdateScore.class);
                i.putExtra("ID", student.subjetid);
                i.putExtra("IDStudent", student.studentid);
                context.startActivity(i);
            }
        });

        return view;
    }
}
