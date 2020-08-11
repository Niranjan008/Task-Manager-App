package com.example.taskmanager;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ReportActivity extends AppCompatActivity {
    ListView lv;
    ArrayList<String> titles;
    ArrayList<TaskReport> reps;
    ArrayAdapter<String> arrayAdapter;
    FirebaseDatabase fdb;
    DatabaseReference df;
    TaskReport ui;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        final String phone = getIntent().getStringExtra("phone");
        titles = new ArrayList<String>();
        reps = new ArrayList<TaskReport>();
        lv = findViewById(R.id.reportlist);
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,titles);
        fdb = FirebaseDatabase.getInstance();
        df = fdb.getReference("taskreport");
        df.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    ui= ds.getValue(TaskReport.class);
                    if(ui.getAssignerPhone().compareTo(phone) ==0 && ui.getAcknowledge().compareTo("false")==0){
                        titles.add(ui.getTitle());
                        reps.add(ui);
                    }
                }
                lv.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent op = new Intent(ReportActivity.this,ViewReportActivity.class);
                TaskReport obj = (TaskReport)reps.get(position);
                op.putExtra("data",new Gson().toJson(obj));
                startActivity(op);
            }
        });
    }
}
