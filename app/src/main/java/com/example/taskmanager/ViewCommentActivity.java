package com.example.taskmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import org.w3c.dom.Text;

public class ViewCommentActivity extends AppCompatActivity {
    String jsonobj;
    Comment com;
    TextView title,description,oldmess,newmess;
    DatabaseReference df;
    public void del(View v){
        df = FirebaseDatabase.getInstance().getReference("comments").child(com.getId());
        df.removeValue();
        Intent in = new Intent(ViewCommentActivity.this,ProfileActivity.class);
        in.putExtra("userphone",com.getEmployeephone());
        startActivity(in);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_comment);

        jsonobj = getIntent().getStringExtra("comobj");
        com = new Gson().fromJson(jsonobj,Comment.class);
        title = (TextView)findViewById(R.id.textView24);
        description = (TextView)findViewById(R.id.textView25);
        oldmess = (TextView)findViewById(R.id.textView28);
        newmess = (TextView)findViewById(R.id.textView30);
        title.setText(com.getTitle());
        description.setText(com.getDescription());
        oldmess.setText(com.getOldmess());
        newmess.setText(com.getNewmess());
    }
}
