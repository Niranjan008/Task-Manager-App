package com.example.taskmanager;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.google.gson.Gson;


public class ReceiverActivity extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

//        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        MediaPlayer mp = MediaPlayer.create(context, notification);
//        mp.start();
        String taskd = intent.getStringExtra("taskdata");
        Task exectask = new Gson().fromJson(taskd,Task.class);
        Intent i = new Intent(context.getApplicationContext(), NotifyActivity.class);

        Bundle extras = new Bundle();

        extras.putString("title",exectask.getTitle());
        extras.putString("desc",exectask.getDescription());
        extras.putString("taskobj",new Gson().toJson(exectask));
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_FROM_BACKGROUND);
        i.putExtras(extras);
        context.startActivity(i);

        Toast.makeText(context, "Alarm received!", Toast.LENGTH_LONG).show();

    }
}
