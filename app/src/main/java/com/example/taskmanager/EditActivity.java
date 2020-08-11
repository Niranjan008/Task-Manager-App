package com.example.taskmanager;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

public class EditActivity extends AppCompatActivity {
    String jsonobj;
    TimePickerDialog mTimePicker;
    String indata;
    Task task;
    DatePickerDialog ss;
    Button datetime,tim;
    int time[] = new int[2];
    String assignedphone;
    Calendar myCalendar;
    String myyear="",mymonth="",mydate="";
    EditText title,description,assignerphone;
    TextView dateset,timeset;
    int hours,mins;
    public void assign(View v){
        if(TextUtils.isEmpty(assignerphone.getText())){
            Toast.makeText(this,"Destination phone cant be empty",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(title.getText())){
            Toast.makeText(this,"Title cant be empty",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(description.getText())){
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
            String desc = description.getText().toString();
            assignedphone = assignerphone.getText().toString();
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

            String str = task.getId();
            String assignerphone = task.getAssignerphone();
            DatabaseReference ds = FirebaseDatabase.getInstance().getReference("task").child(task.getId());
            Task task = new Task(str, worktitle, desc, df, assignerphone, assignedphone, "false", "false");
            ds.setValue(task);
            sendSMS(assignedphone, "You are re-assigned a work.Check Task manager app");
            Toast.makeText(EditActivity.this, "Re - Assigned Successfully", Toast.LENGTH_LONG).show();
            Intent h = new Intent(EditActivity.this,ProfileActivity.class);
            h.putExtra("userphone",indata);
            startActivity(h);
        }

    }
    public void sendSMS(String phoneNo, String msg) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNo, null, msg, null, null);
        Toast.makeText(getApplicationContext(), "SMS sent.",
                Toast.LENGTH_LONG).show();
    }
    public void pick(View v){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 1);
        } else {
            getcontact();
        }
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
                assignerphone.setText(phoneNo);
            }
            else {
                assignerphone.setText("+91"+phoneNo);
            }
            cursor.close();
        }

    }
    private void getcontact() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(intent, 2);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        dateset = (TextView)findViewById(R.id.textView22);
        timeset = (TextView)findViewById(R.id.textView23);
        jsonobj = getIntent().getStringExtra("taskobj");
        task = new Gson().fromJson(jsonobj,Task.class);
        indata = task.getAssignerphone();
        title = (EditText)findViewById(R.id.editText);
        description =(EditText)findViewById(R.id.editText2);
        assignerphone = (EditText)findViewById(R.id.editText4);
        datetime =(Button)findViewById(R.id.button2);
        tim =(Button)findViewById(R.id.button4);
        title.setText(task.getTitle());
        description.setText(task.getDescription());
        assignerphone.setText(task.getEmployeephone());
        time[0] = -1;
        time[1] = -1;

        String timestamp = task.getDateandtime();
        String year = timestamp.substring(0,4);
        final String month = timestamp.substring(5,7);
        final String day = timestamp.substring(8,10);
        String hour = timestamp.substring(11,13);
        String mins = timestamp.substring(14,16);
        myCalendar = Calendar.getInstance();
        myCalendar.set(Calendar.YEAR, Integer.valueOf(year));
        myCalendar.set(Calendar.MONTH, Integer.valueOf(month));
        myCalendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(day));
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                String myFormat = "MM/dd/yy"; //In which you need put here
                myyear = Integer.toString(year);
                dateset.setText(dayOfMonth+"/"+monthOfYear+"/"+year);
                if(monthOfYear+1<10){
                    mymonth = "0";
                    mymonth += monthOfYear;
                }else {
                    mymonth +=monthOfYear;
                }
                if(dayOfMonth<10){
                    mydate = "0";
                    mydate += dayOfMonth;
                }
                else{
                    mydate = Integer.toString(dayOfMonth);
                }
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                Toast.makeText(EditActivity.this,mydate+"/"+mymonth+"/"+myyear,Toast.LENGTH_SHORT).show();
            }

        };

        datetime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ss =new DatePickerDialog(EditActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                ss.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
                ss.show();



            }
        });

        tim.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//                Calendar mcurrentTime = Calendar.getInstance();
//                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//                int minute = mcurrentTime.get(Calendar.MINUTE);

                mTimePicker = new TimePickerDialog(EditActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        time[0] = selectedHour;
                        time[1] = selectedMinute;
                        timeset.setText(Integer.toString(time[0])+":"+Integer.toString(time[1]));
                        Toast.makeText(EditActivity.this,Integer.toString(time[0])+":"+Integer.toString(time[1]),Toast.LENGTH_LONG).show();
                    }
                }, time[0], time[1], true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                //Toast.makeText(EditActivity.this,Integer.toString(hour)+":"+Integer.toString(minute),Toast.LENGTH_LONG).show();
            }
        });
    }
}
