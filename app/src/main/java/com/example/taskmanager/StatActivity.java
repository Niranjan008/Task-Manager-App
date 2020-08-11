package com.example.taskmanager;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
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

import java.util.ArrayList;

public class StatActivity extends AppCompatActivity {
    ListView lv;
    ArrayList<String> titles;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<Task> tasksarr;
    DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat);
        lv = (ListView)findViewById(R.id.listView);
        final String assignerphone = getIntent().getStringExtra("phonenum");
        titles = new ArrayList<String>();
        tasksarr = new ArrayList<Task>();
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,titles);
        ref = FirebaseDatabase.getInstance().getReference("task");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    Task obj = ds.getValue(Task.class);
                    if(obj.getAssignerphone().compareTo(assignerphone)==0 && obj.getAlarmset().compareTo("false")==0) {
                        titles.add(obj.getTitle());
                        tasksarr.add(obj);
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
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(tasksarr.get(position).getEmployeephone(), null,"Remainder:You have to complete the task", null, null);
                Toast.makeText(getApplicationContext(), "SMS sent.",
                        Toast.LENGTH_LONG).show();
                //Toast.makeText(StatActivity.this,"SMS Sent",Toast.LENGTH_LONG).show();
            }
        });
    }
}
