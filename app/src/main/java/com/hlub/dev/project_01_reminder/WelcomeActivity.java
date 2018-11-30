package com.hlub.dev.project_01_reminder;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


public class WelcomeActivity extends AppCompatActivity {
    CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

    }

    @Override
    protected void onStart() {
        super.onStart();
        countTimer();
    }

    public void countTimer(){
        timer = new CountDownTimer(1500, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d("abc", millisUntilFinished + "");
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }.start();
    }
}
