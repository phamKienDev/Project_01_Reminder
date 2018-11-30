package com.hlub.dev.project_01_reminder;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.hlub.dev.project_01_reminder.fragment.CalendarFragment;
import com.hlub.dev.project_01_reminder.fragment.SettingsFragment;
import com.hlub.dev.project_01_reminder.fragment.TasksFragment;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.navigation_task);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TasksFragment()).commit();
        }


    }

    //đăng xuất facebook
    @Override
    protected void onStart() {
        //LoginManager.getInstance().logOut();
        super.onStart();
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction().
                    replace(R.id.fragment_container, fragment).commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.navigation_task:
                fragment = new TasksFragment();
                break;
            case R.id.navigation_calendar:
                fragment = new CalendarFragment();
                break;
            case R.id.navigation_settings:
                fragment = new SettingsFragment();
                break;
        }
        return loadFragment(fragment);
    }
}
