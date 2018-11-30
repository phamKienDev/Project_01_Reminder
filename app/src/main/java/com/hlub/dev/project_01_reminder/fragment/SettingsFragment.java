package com.hlub.dev.project_01_reminder.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.hlub.dev.project_01_reminder.R;
import com.hlub.dev.project_01_reminder.model.Song;
import com.facebook.FacebookSdk;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class SettingsFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private TextView tvExit;
    private TextView tvShare;
    private Spinner spinnerBellSound;
    private ImageView imgPlayBellSound;


    ArrayList<Song> songArrayList;
    int positionSong = 0;
    MediaPlayer mediaPlayer;
    public static int fileSong = 0;

    //facebook
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    ShareLinkContent shareLinkContent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, null);
        callbackManager = CallbackManager.Factory.create();
        anhxa(view);

        songArrayList = new ArrayList<>();
        shareDialog = new ShareDialog(getActivity());
        //SPINNER MUSIC
        addSong();
        spinnerBellSound.setOnItemSelectedListener(this);
        khoiTaoMedia();
        loadSpinnerBellSound();

        imgPlayBellSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //play: nghe nhac - tạm dừng
                if (mediaPlayer.isPlaying()) {
                    //đang phát nhạc :khi ấn->pause
                    mediaPlayer.pause();
                    imgPlayBellSound.setImageResource(R.drawable.ic_play);
                } else {
                    //k phát nhạc -> khi ấn ->play
                    mediaPlayer.start();
                    imgPlayBellSound.setImageResource(R.drawable.ic_play_light_36dp);
                }
            }
        });

        //EXIT
        tvExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogExit();
            }
        });

        //SHARE
        tvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogShare();
            }
        });

        return view;

    }

    private void showDialogShare() {
        //SHARE LINK
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            shareLinkContent = new ShareLinkContent.Builder().
                    setContentTitle("abc").
                    setContentDescription("abc").
                    setContentUrl(Uri.parse("https://developers.facebook.com/docs/facebook-login/android"))
                    .build();
        }
        shareDialog.show(shareLinkContent);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    //DIALOG EXIT APP
    public void showDialogExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.thoat_ung_dung));

        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onBackPressed();
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    public void onBackPressed() {
        getActivity().finish();
        getActivity().moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    private void addSong() {
        songArrayList.add(new Song("Alarm tone", R.raw.alarm_tone));
        songArrayList.add(new Song("Clock ringing", R.raw.clock_ringing));
        songArrayList.add(new Song("Digital phone ringing", R.raw.digital_phone_ringing));
        songArrayList.add(new Song("Wake up sound", R.raw.wake_up_sounds));
        songArrayList.add(new Song("Eerie clock chimes", R.raw.eerie_clock_chimes_sound));
    }

    private void khoiTaoMedia() {
        mediaPlayer = MediaPlayer.create(getActivity(), songArrayList.get(positionSong).getFile());
    }

    public void loadSpinnerBellSound() {
        ArrayAdapter<Song> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, songArrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBellSound.setAdapter(adapter);
    }

    public void anhxa(View view) {
        tvExit = view.findViewById(R.id.tvExit);
        tvShare = view.findViewById(R.id.tvShare);
        spinnerBellSound = view.findViewById(R.id.spinnerBellSound);
        imgPlayBellSound = view.findViewById(R.id.imgPlayBellSound);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        positionSong = position;
        //dừng nhạc -> đổi hình imgplay-> khởi tạo nhạc tại vị trí vừa nghe
        mediaPlayer.stop();
        mediaPlayer.release();//giải phóng
        imgPlayBellSound.setImageResource(R.drawable.ic_play);
        fileSong = songArrayList.get(positionSong).getFile();
        khoiTaoMedia();
        Toast.makeText(getActivity(), position + "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
