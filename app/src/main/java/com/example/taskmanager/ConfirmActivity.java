package com.example.taskmanager;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.Nullable;
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
import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ConfirmActivity extends AppCompatActivity {
    TextView title ,des,duedate;
    DateTime dt;
    DatabaseReference ds;
    int hour,min,sec,day,month,year;
    Task task,newtasks;
    DatabaseReference fb,taskrep;
    EditText name,status;
    FirebaseDatabase numtaskdb;
    String taskcompname="",taskcompmess="",tasktitle,taskdes;
    public void delete(View v){
        ds = FirebaseDatabase.getInstance().getReference("task").child(task.getId());
        ds.removeValue();
        Intent y = new Intent(ConfirmActivity.this, ProfileActivity.class);
        y.putExtra("userphone", task.getEmployeephone());
        startActivity(y);
    }
    public void complete(View v){
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference("task").child(task.getId());
        Task newtask = new Task(task.getId(),task.getTitle(),task.getDescription(),task.getDateandtime(),task.getAssignerphone(),task.getEmployeephone(),task.getAlarmset(),"true");
        dr.setValue(newtask);
        name = (EditText)findViewById(R.id.editText3);
        status = (EditText)findViewById(R.id.editText5);
        taskcompname = name.getText().toString();
        taskcompmess = status.getText().toString();
        tasktitle = task.getTitle();
        taskdes = task.getDescription();
        if(taskcompname.compareTo("")==0)
            Toast.makeText(this,"Enter your Name",Toast.LENGTH_LONG).show();
        else if(taskcompmess.compareTo("")==0)
            Toast.makeText(this,"Enter completion status",Toast.LENGTH_LONG).show();
        else {
            String tid = taskrep.push().getKey();
            TaskReport tr = new TaskReport(tid, tasktitle, taskdes, taskcompname, taskcompmess, "false", task.getAssignerphone(),task.getEmployeephone());
            taskrep.child(tid).setValue(tr);
            Toast.makeText(this, "Completed", Toast.LENGTH_LONG).show();
            sendSMS(task.getAssignerphone(), taskcompname+" has sent you a report.");
            Intent y = new Intent(ConfirmActivity.this, ProfileActivity.class);
            y.putExtra("userphone", task.getEmployeephone());
            startActivity(y);
        }
    }
    public void sendSMS(String phoneNo, String msg) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNo, null, msg, null, null);
        Toast.makeText(getApplicationContext(), "SMS sent.",
                Toast.LENGTH_LONG).show();
    }
    public void remind(View v){
        if(task.getAlarmset().compareTo("false")==0) {
            task.alarmset = "true";
            newtasks = new Task(task.getId(), task.getTitle(), task.getDescription(), task.getDateandtime(), task.getAssignerphone(), task.getEmployeephone(), "true", task.getViewed());
            fb.setValue(newtasks);
            Calendar cal = Calendar.getInstance();
            cal.clear();
            cal.set(Calendar.DATE, day);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.HOUR_OF_DAY, hour);  //HOUR
            cal.set(Calendar.MINUTE, min);       //MIN
            cal.set(Calendar.SECOND, sec);
            //Toast.makeText(this,day +"/"+month +"/" +year +"--" + hour+":"+min+":"+sec,Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getBaseContext(), ReceiverActivity.class);
            intent.putExtra("taskdata", new Gson().toJson(task));
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);


            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
            Toast.makeText(this, "Remainder Set", Toast.LENGTH_LONG).show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.SYSTEM_ALERT_WINDOW) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW}, 5);
            } else {

            }

        }
        else{
            Toast.makeText(this
                    ,"Alarm already Set",Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 5) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                        Toast.makeText(this,"Please grant permission",Toast.LENGTH_SHORT).show();
                } else {
                    //Permission Granted-System will work

                }

            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        String data = getIntent().getStringExtra("taskdata");
        task = new Gson().fromJson(data,Task.class);
        title = (TextView)findViewById(R.id.title);
        des = (TextView)findViewById(R.id.description);
        duedate = (TextView)findViewById(R.id.textView9);
        title.setText(task.getTitle());
        des.setText(task.getDescription());
         dt = new DateTime(task.getDateandtime()) ;
        hour = dt.getHourOfDay();
        min = dt.getMinuteOfHour();
        sec = dt.getSecondOfMinute();
        year = dt.getYear();
        month = dt.getMonthOfYear();
        day = dt.getDayOfMonth();
        String str = "Due on ";
        str+= Integer.toString(day) +"/" +Integer.toString(month+1) +"/" +Integer.toString(year) + " at " + Integer.toString(hour) +":"+Integer.toString(min);
        duedate.setText(str);
         fb = FirebaseDatabase.getInstance().getReference("task").child(task.getId());


        taskrep = FirebaseDatabase.getInstance().getReference("taskreport");
    }
}
