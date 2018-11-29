package com.hlub.dev.project_01_reminder.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.hlub.dev.project_01_reminder.MainActivity;
import com.hlub.dev.project_01_reminder.R;
import com.hlub.dev.project_01_reminder.fragment.SettingsFragment;
import com.hlub.dev.project_01_reminder.fragment.TasksFragment;

public class AlarmService extends Service{
    MediaPlayer mediaPlayer;
    int id;
    private static  final int NOTIFICATION_ID=99;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String nhanKey=intent.getExtras().getString(TasksFragment.KEY);
        Log.e("Nhận key ",nhanKey);

        if(nhanKey.equals("On")){
            id=1;
        }else if(nhanKey.equals("Off")){
            id=0;
        }

        if(id==1){
            mediaPlayer = MediaPlayer.create(this, SettingsFragment.fileSong);
            Log.d("Key music",SettingsFragment.fileSong+"");
            mediaPlayer.start();
            id=0;
            runAsForeground();
        }else{
            mediaPlayer.stop();
            mediaPlayer.reset();
        }

        return super.onStartCommand(intent, flags, startId);
    }


    private void runAsForeground() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        @SuppressLint("WrongConstant") PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, Intent.FLAG_ACTIVITY_NEW_TASK);

        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentText(getString(R.string.isRecording))
                .setContentIntent(pendingIntent).build();

        startForeground(NOTIFICATION_ID, notification);

    }
}
