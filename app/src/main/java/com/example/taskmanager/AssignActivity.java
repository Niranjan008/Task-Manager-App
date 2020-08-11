package com.example.taskmanager;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AssignActivity extends AppCompatActivity {
    Intent intent;
    Button datetime,tim;
    TextView vdate,vtime;
    DatePickerDialog ss;
    TimePickerDialog mTimePicker;
    Map<String,String> header;
    Date findate;
    int time[] = new int[2];
    RequestQueue requestQueue;
    int hours,mins;
    TimePickerDialog picker;
    Calendar myCalendar;
    String assignerphone,assignedphone="";
    EditText assphone,title,des;
    DatabaseReference tasks,numtasks;
    String myyear="",mymonth="",mydate="";


    public void home(View v){
        Intent h = new Intent(AssignActivity.this,ProfileActivity.class);
        h.putExtra("userphone",assignerphone);
        startActivity(h);
    }
    public void pick(View v){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 1);
        } else {
            getcontact();
        }
    }
    private void getcontact() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(intent, 2);

    }
    public void assign(View v){
        if(TextUtils.isEmpty(assphone.getText())){
            Toast.makeText(this,"Destination phone cant be empty",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(title.getText())){
            Toast.makeText(this,"Title cant be empty",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(des.getText())){
            Toast.makeText(this,"Title cant be empty",Toast.LENGTH_SHORT).show();
        }
        else if(time[0]==-1 && time[1]== -1){
            Toast.makeText(this,"Time cant be empty",Toast.LENGTH_SHORT).show();
        }
        else if(myyear.compareTo("")==0 && mymonth.compareTo("")==0 &&  mydate.compareTo("")==0){
            Toast.makeText(this,"Time cant be empty",Toast.LENGTH_SHORT).show();
        }
        else {
            String worktitle = title.getText().toString();
            String description = des.getText().toString();
            assignedphone = assphone.getText().toString();
            boolean flag = false;
            if(assphone.getText().toString().length()<13 || assphone.getText().toString().length()>13) {
                flag = true;
                Toast.makeText(this,"Enter Valid Phone Number",Toast.LENGTH_LONG).show();
            }

             hours = time[0];
             mins = time[1];
            String hs = "", ms = "";
            if (hours < 10) {
                hs += "0";
                hs += Integer.toString(hours);
            } else {
                hs += Integer.toString(hours);
            }
            if (mins < 10) {
                ms += "0";
                ms += Integer.toString(mins);
            } else {
                ms += Integer.toString(mins);
            }
            String time = "";
            time += hs + ":" + ms + ":" + "00" + "+05:30";
            String df = "";
            df += myyear + '-' + mymonth + '-' + mydate + 'T' + time;

            if(flag == false) {
                String id = tasks.push().getKey();
                Task task = new Task(id, worktitle, description, df, assignerphone, assignedphone, "false", "false");
                tasks.child(id).setValue(task);
                sendSMS(assignedphone, "You are assigned work.Check Task manager app");
                assphone.setText("");
                Toast.makeText(AssignActivity.this, "Assigned Successfully", Toast.LENGTH_LONG).show();
            }
        }

    }


    public void sendSMS(String phoneNo, String msg) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNo, null, msg, null, null);
        Toast.makeText(getApplicationContext(), "SMS sent.",
                Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 2) {
            String phoneNo = null;
            String name = null;

            Uri uri = data.getData();
            Cursor cursor = this.getContentResolver().query(uri, null, null, null, null);

            if (cursor.moveToFirst()) {
                int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);

                phoneNo = cursor.getString(phoneIndex);
                name = cursor.getString(nameIndex);

            }
            if(phoneNo.charAt(0)=='+'){
                assphone.setText(phoneNo);
            }
            else {
                assphone.setText("+91"+phoneNo);
            }
            cursor.close();
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign);
        vdate =(TextView)findViewById(R.id.textView20);
        vtime = (TextView)findViewById(R.id.textView21);
        requestQueue = Volley.newRequestQueue(this);
        assphone = (EditText)findViewById(R.id.editText4);
        title = (EditText)findViewById(R.id.editText);
        des = (EditText)findViewById(R.id.editText2);
        assignerphone = getIntent().getStringExtra("phone");
        datetime =(Button)findViewById(R.id.button2);
        tim =(Button)findViewById(R.id.button4);
        myCalendar = Calendar.getInstance();
        time[0] = -1;
        time[1] = -1;
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MM/dd/yy"; //In which you need put here
                myyear = Integer.toString(year);
                if(monthOfYear+1<10){
                    mymonth = "0";
                    mymonth += monthOfYear;
                }else {
                    mymonth =Integer.toString(monthOfYear);
                }
                if(dayOfMonth<10){
                    mydate = "0";
                    mydate += dayOfMonth;
                }
                else{
                    mydate = Integer.toString(dayOfMonth);
                }
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                vdate.setText(mydate+"/"+Integer.toString(monthOfYear+1)+"/"+myyear);
                Toast.makeText(AssignActivity.this,mydate+"/"+Integer.toString(monthOfYear+1)+"/"+myyear,Toast.LENGTH_SHORT).show();
            }

        };

        datetime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                 ss =new DatePickerDialog(AssignActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                ss.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
                ss.show();


               // Toast.makeText(AssignActivity.this,myCalendar.get(Calendar.DATE) +"/"+myCalendar.get(Calendar.MONTH)+"/"+myCalendar.get(Calendar.YEAR),Toast.LENGTH_LONG).show();
            }
        });
        tim.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//                Calendar mcurrentTime = Calendar.getInstance();
//                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//                int minute = mcurrentTime.get(Calendar.MINUTE);

                mTimePicker = new TimePickerDialog(AssignActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        time[0] = selectedHour;
                        time[1] = selectedMinute;
                        mTimePicker.updateTime(selectedHour,selectedMinute);
                        vtime.setText(Integer.toString(time[0])+":"+Integer.toString(time[1]));
                        Toast.makeText(AssignActivity.this,Integer.toString(time[0])+":"+Integer.toString(time[1]),Toast.LENGTH_LONG).show();
                    }
                }, time[0],time[1], true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                //Toast.makeText(AssignActivity.this,Integer.toString(h)+":"+Integer.toString(minute),Toast.LENGTH_LONG).show();
            }
        });

        tasks = FirebaseDatabase.getInstance().getReference("task");
        numtasks = FirebaseDatabase.getInstance().getReference("numtask");
    }

}
