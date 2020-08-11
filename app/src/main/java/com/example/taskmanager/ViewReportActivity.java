package com.example.taskmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

public class ViewReportActivity extends AppCompatActivity {
    TaskReport taskrep;
    TextView title,desc,comp,workstatus;
    DatabaseReference ds;
    EditText ll;
    DatabaseReference fb;
    public void sendmess(View v){
        if(ll.getText().toString().compareTo("")!=0){
            String id = fb.push().getKey();
            Comment cm = new Comment(id,taskrep.getAssignerPhone(),taskrep.getCompleterphone(),taskrep.getTitle(),taskrep.getDescription(),taskrep.getTaskRemarks(),ll.getText().toString());
            fb.child(id).setValue(cm);
            Toast.makeText(this,"Message Sent",Toast.LENGTH_LONG).show();
            ll.setText("");
            sendSMS(taskrep.getCompleterphone(), "You got a comment for your report.Check Task manager app");
        }
    }
    public void sendSMS(String phoneNo, String msg) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNo, null, msg, null, null);
        Toast.makeText(getApplicationContext(), "SMS sent.",
                Toast.LENGTH_LONG).show();
    }
    public void done(View v){
        DatabaseReference ff = FirebaseDatabase.getInstance().getReference("taskreport").child(taskrep.getId());
        TaskReport hh = new TaskReport(taskrep.getId(),taskrep.getTitle(),taskrep.getDescription(),taskrep.getTaskName(),taskrep.getTaskRemarks(),"true",taskrep.getAssignerPhone(),taskrep.getCompleterphone());
        ff.setValue(hh);
        ds = FirebaseDatabase.getInstance().getReference("taskreport").child(taskrep.getId());
        ds.removeValue();
        Intent o = new Intent(ViewReportActivity.this,ProfileActivity.class);
        o.putExtra("userphone",taskrep.getAssignerPhone());
        startActivity(o);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);
        String data = getIntent().getStringExtra("data");
        ll = (EditText)findViewById(R.id.editText6);
        taskrep = new Gson().fromJson(data,TaskReport.class);
        title = (TextView)findViewById(R.id.titletextView);
        desc = (TextView)findViewById(R.id.desctextView);
        comp =(TextView)findViewById(R.id.compbytextView);
        workstatus = (TextView)findViewById(R.id.statustextView);
        title.setText(taskrep.getTitle());
        desc.setText(taskrep.getDescription());
        comp.setText(taskrep.getTaskName());
        workstatus.setText(taskrep.getTaskRemarks());
        fb = FirebaseDatabase.getInstance().getReference("comments");
    }
}
