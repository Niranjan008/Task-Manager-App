package com.example.taskmanager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ViewActivity extends AppCompatActivity {
    ListView lv;
    ArrayList<String> titles;
    ArrayList<Task> tasksass;
    ArrayAdapter<String> arrayAdapter;
    FirebaseDatabase tasksdb;
    DatabaseReference ref;
    String phonenum;
    Task extask;
    PendingIntent pendingIntent;
    AlarmManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        lv = (ListView)findViewById(R.id.taskview);
        titles = new ArrayList<String>();
        tasksdb = FirebaseDatabase.getInstance();
        ref = tasksdb.getReference("task");
        tasksass = new ArrayList<Task>();
        phonenum = getIntent().getStringExtra("phone");
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,titles);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    extask = ds.getValue(Task.class);
                    String cmp1 = new String(extask.getEmployeephone());
                    String cmp2 = new String(phonenum);
                    if(extask.getEmployeephone().compareTo(phonenum) == 0 ){
                        titles.add(extask.getTitle());
                        tasksass.add(extask);
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
                Intent ins = new Intent(ViewActivity.this,ConfirmActivity.class);
                Task ex = (Task)tasksass.get(position);

                ins.putExtra("taskdata",new Gson().toJson(ex));
                startActivity(ins);
            }
        });
    }

}
