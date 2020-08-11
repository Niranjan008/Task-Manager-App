package com.example.taskmanager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {
    String phonenum;
    Intent intent,s,d,e;
    Button signout,assreport;
    FirebaseAuth.AuthStateListener authStateListener;
    TextView ss;
    private FirebaseAuth firebaseAuth;
    public void report(View v){
        e = new Intent(ProfileActivity.this,ReportActivity.class);
        e.putExtra("phone",phonenum);
        startActivity(e);
    }
    public void accept(View v){
        d = new Intent(ProfileActivity.this,ViewActivity.class);
        d.putExtra("phone",phonenum);
        startActivity(d);
    }
    public void commentlist(View v){
        Intent o = new Intent(ProfileActivity.this,CommentsListActivity.class);
        o.putExtra("phone",phonenum);
        startActivity(o);
    }
    public void signout(View v){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.addAuthStateListener(authStateListener);

        firebaseAuth.signOut();
    }
    public void assignact(View view){
        intent = new Intent(ProfileActivity.this,AssignActivity.class);
        intent.putExtra("phone",phonenum);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 111 && grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this,"Permission Granted",Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this,"Permission Denied",Toast.LENGTH_LONG).show();
        }

    }
    public void edit(View v){
        Intent intent = new Intent(ProfileActivity.this,EditListActivity.class);
        intent.putExtra("phoneno",phonenum);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        phonenum = getIntent().getStringExtra("userphone");
        assreport = (Button)findViewById(R.id.button9);
        signout = (Button)findViewById(R.id.button3);
        ss =(TextView)findViewById(R.id.textView14);
        ss.setText("You are signed in as : " +phonenum);
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null){
                    //Do anything here which needs to be done after signout is complete
                    s = new Intent(ProfileActivity.this,MainActivity.class);
                    startActivity(s);
                }
                else {
                }
            }
        };
        assreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this,StatActivity.class);
                i.putExtra("phonenum",phonenum);
                startActivity(i);
            }
        });
        if(checkSelfPermission(Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.SEND_SMS},111);
        }
    }
}
