package com.example.taskmanager;

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

public class CommentsListActivity extends AppCompatActivity {
    ListView lv;
    String loginphone;
    ArrayList<String> titles;
    ArrayList<Comment> comments;
    DatabaseReference df;
    ArrayAdapter<String> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments_list);
        lv = (ListView)findViewById(R.id.lv);
        titles = new ArrayList<String>();
        comments = new ArrayList<Comment>();
        loginphone = getIntent().getStringExtra("phone");
        df = FirebaseDatabase.getInstance().getReference("comments");
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,titles);
        df.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    Comment cm = ds.getValue(Comment.class);
                    try {
                        if(cm.getEmployeephone().compareTo(loginphone)==0){
                            titles.add(cm.getTitle());
                            comments.add(cm);
                        }
                    }catch(Exception e){

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
                Intent intent = new Intent(CommentsListActivity.this,ViewCommentActivity.class);
                intent.putExtra("comobj",new Gson().toJson(comments.get(position)));
                startActivity(intent);
            }
        });
    }
}
