package com.example.taskmanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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

public class EditListActivity extends AppCompatActivity {
    ListView lv;
    DatabaseReference df;
    ArrayList<String> titles;
    ArrayList<Task> tasks;
    String phonenum;
    ArrayAdapter<String> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_list);
        lv = (ListView)findViewById(R.id.listview);
        titles = new ArrayList<String>();
        tasks = new ArrayList<Task>();
        phonenum = getIntent().getStringExtra("phoneno");
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,titles);
        df = FirebaseDatabase.getInstance().getReference("task");
        df.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    Task tsk = ds.getValue(Task.class);
                    if(tsk.getAssignerphone().compareTo(phonenum)==0){
                        titles.add(tsk.getTitle() + " / " + tsk.getDateandtime().substring(0,5)+Integer.toString(Integer.valueOf( tsk.getDateandtime().substring(5,7))+1)+ tsk.getDateandtime().substring(7,10)+ " / " + tsk.getDateandtime().substring(11,16));
                        tasks.add(tsk);
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
                Intent intent = new Intent(EditListActivity.this,EditActivity.class);
                intent.putExtra("taskobj",new Gson().toJson(tasks.get(position)));
                startActivity(intent);
            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(EditListActivity.this)
                        .setTitle("Delete entry")
                        .setMessage("Are you sure you want to delete this entry?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                df = FirebaseDatabase.getInstance().getReference("task").child(tasks.get(position).getId());
                                df.removeValue();
                                titles.remove(position);
                                Intent h = new Intent(EditListActivity.this,ProfileActivity.class);
                                h.putExtra("userphone",phonenum);
                                startActivity(h);
                                //arrayAdapter.notifyDataSetChanged();

                            }
                        }).setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

                return true;
            }
        });

    }
}
