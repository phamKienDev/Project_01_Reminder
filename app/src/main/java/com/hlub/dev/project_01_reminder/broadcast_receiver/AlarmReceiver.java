package com.hlub.dev.project_01_reminder.broadcast_receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.hlub.dev.project_01_reminder.service.AlarmService;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Receiver", "Hello");

        String chuoi = intent.getExtras().getString("extra");
        Log.e("Key ", chuoi);

        Intent myIntent = new Intent(context, AlarmService.class);
        myIntent.putExtra("extra", chuoi);

        //từ android 8.0 bị hạn chế

        if (Build.VERSION.SDK_INT>=26) {
            context.startForegroundService(myIntent);
        } else {
            context.startService(myIntent);
        }

    }
}
