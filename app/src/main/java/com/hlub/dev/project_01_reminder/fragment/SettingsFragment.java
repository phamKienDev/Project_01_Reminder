package com.hlub.dev.project_01_reminder.fragment;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.hlub.dev.project_01_reminder.R;
import com.hlub.dev.project_01_reminder.model.Song;

import java.util.ArrayList;

public class SettingsFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private TextView tvExit;
    private Spinner spinnerBellSound;
    private ImageView imgPlayBellSound;


    ArrayList<Song> songArrayList;
    int positionSong = 0;
    MediaPlayer mediaPlayer;
    public static int fileSong=0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, null);
        anhxa(view);

        songArrayList = new ArrayList<>();

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


        return view;

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
