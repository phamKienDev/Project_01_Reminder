package com.hlub.dev.project_01_reminder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class WelcomeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


    }

    public void next(View view) {
        Intent intent=new Intent(WelcomeActivity.this,MainActivity.class);
        startActivity(intent);
    }
}
