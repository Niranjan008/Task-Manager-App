package com.example.taskmanager;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NotifyActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextView ss,yy;
    MediaPlayer mp;
    Spinner sp;
    Intent ints;
    String remtime[] = {"Dont Remind","15 Minutes","30 Minutes","60 Minutes"};
    String taskstr;
    public void done(View v){
        finish();
        mp.stop();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);
        sp = (Spinner)findViewById(R.id.spinner);

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
         mp = MediaPlayer.create(getApplicationContext(), notification);
        mp.start();
        ss = (TextView)findViewById(R.id.textView6);
        yy = (TextView)findViewById(R.id.textView8);
        Bundle extras = getIntent().getExtras();
        String title = extras.getString("title");
        String desc = extras.getString("desc");
        taskstr = extras.getString("taskobj");
        ss.setText(title);
        yy.setText(desc);
        sp.setOnItemSelectedListener(this);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, remtime);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(dataAdapter);


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd:HH:mm");
        String currentDateandTime = sdf.format(new Date());
        Task task = new Gson().fromJson(taskstr,Task.class);
        Date date = null;
        try {
             date = sdf.parse(currentDateandTime);
        }catch (Exception e){
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if(position == 0){
            return;
        }
        else if(position ==1) {
            calendar.add(Calendar.MINUTE, 15);
        }
        else if(position == 2){
            calendar.add(Calendar.MINUTE,30);
        }
        else{
            calendar.add(Calendar.HOUR,1);
        }
        Intent intent = new Intent(getBaseContext(), ReceiverActivity.class);
        intent.putExtra("taskdata", new Gson().toJson(task));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        Toast.makeText(this, "Remainder Set", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
