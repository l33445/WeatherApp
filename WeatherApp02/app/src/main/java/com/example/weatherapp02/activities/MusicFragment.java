package com.example.weatherapp02.activities;
import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.weatherapp02.R;
import com.example.weatherapp02.adapter.MusicAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MusicFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MusicFragment extends Fragment{
    MediaPlayer mediaPlayer = new MediaPlayer();
    boolean flag = true;
    boolean isPlaying = true; // 初始化为暂停状态
    ImageView imageViewDisc;
    ImageButton imageButtonPre;
    ImageButton imageButtonNext;
    ImageButton imageButtonBack;
    ImageButton imageButtonForward;
    ImageButton imageButtonPlayOrPause;
    SeekBar seekBar;
    RatingBar ratingBar;
    TextView textView;
    ListView listView;
    private List<String> mp3Files = new ArrayList<>();
//    String mp3List[] = {"1.mp3", "2.mp3"};
    int index= 0;
    //String mp3List[] = {"1.mp3", "2.mp3", "3.mp3", "4.mp3", "5.mp3", "6.mp3"};
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MusicFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MusicFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MusicFragment newInstance(String param1, String param2) {
        MusicFragment fragment = new MusicFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_music, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.MANAGE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                play(index);
            } else {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 999);
            }
        } else {
            requestPermissions(new String[]{Manifest.permission.MANAGE_EXTERNAL_STORAGE}, 998);
        }
        imageButtonPlayOrPause = getActivity().findViewById(R.id.imageButtonPlayOrPause);
        imageButtonPlayOrPause.setImageResource(android.R.drawable.ic_media_pause);
        seekBar = getActivity().findViewById(R.id.seekBar);
        seekBar.setMax(100);
        ratingBar = getActivity().findViewById(R.id.ratingBar);
        ratingBar.setMax(10);
        ratingBar.setNumStars(10);
        ratingBar.setProgress(3);
        textView = getActivity().findViewById(R.id.textView);
        imageButtonBack = getActivity().findViewById(R.id.imageButtonBack);
        imageButtonForward = getActivity().findViewById(R.id.imageButtonForward);
        imageViewDisc = getActivity().findViewById(R.id.imageViewDisc);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mp3Files = getMP3Files();
        MusicAdapter adapter = new MusicAdapter(mp3Files);
        recyclerView.setAdapter(adapter);

        new MyTask().execute();

        adapter.setOnItemClickListener(new MusicAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                index = position;
                play(index);
            }
        });

        imageButtonBack.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 10000 < 0 ? 0 : mediaPlayer.getCurrentPosition() - 10000);
                                               }
                                           });
        imageButtonForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 10000 > mediaPlayer.getDuration() ? 0 : mediaPlayer.getCurrentPosition() + 10000);
            }
        });


                ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        if (mediaPlayer != null) {
                            mediaPlayer.setVolume(rating / 10, rating / 10);
                        }
                    }
                });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer != null && fromUser){
                    int duration = mediaPlayer.getDuration();
                    mediaPlayer.seekTo(Math.round(duration * (progress/100.0f)));

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        imageButtonPlayOrPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer != null && mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    isPlaying = false; // 更新播放状态
                    imageButtonPlayOrPause.setImageResource(android.R.drawable.ic_media_play);
                }else if(mediaPlayer != null && !mediaPlayer.isPlaying()){
                    mediaPlayer.start();
                    isPlaying = true; // 更新播放状态
                    imageButtonPlayOrPause.setImageResource(android.R.drawable.ic_media_pause);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 998 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                play(index);
            } else {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 999);
            }
        } else if (requestCode == 999 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            play(0);
        }
    }

    private void play(int index) {
        String path = Environment.getExternalStorageDirectory() + File.separator + "Pictures" + File.separator + mp3Files.get(index);

        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();
            mediaPlayer.setVolume(0.5f, 0.5f);
            mediaPlayer.setOnCompletionListener(onCompletionListener);
            updateUI();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void updateUI() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            int currentPosition = mediaPlayer.getCurrentPosition();
            int duration = mediaPlayer.getDuration();

            int percent = Math.round((currentPosition + 0.0f) / duration * 100);
            seekBar.setProgress(percent);
            String text = "音乐名： " + mp3Files.get(index)
                    + "时长： " + duration / 1000 / 60 + "分" + duration / 1000 % 60 + "秒"
                    + "当前进度： " + currentPosition / 1000 / 60 + "分" + currentPosition / 1000 % 60 + "秒";
            textView.setText(text);

            if (isPlaying) {
                float degree = imageViewDisc.getRotation() + 10;
                if (degree >= 360) degree = 0;
                imageViewDisc.setRotation(degree);
            }
        }
    }
    private void playNext() {
        if (index < mp3Files.size() - 1) {
            index++;
            play(index);
        } else {
            index = 0;
            play(index);
        }
    }

    private List<String> getMP3Files() {
        List<String> mp3Files = new ArrayList<>();
        File directory = new File(Environment.getExternalStorageDirectory() + File.separator + "Pictures");
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".mp3")) {
                    mp3Files.add(file.getName());
                }
            }
        }
        return mp3Files;
    }

    private MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            // 播放下一首
            playNext();
        }
    };

    class MyTask extends AsyncTask<String, Integer, Object>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            // 只有在 mediaPlayer 准备好之后才更新 UI
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                int currentPosition = mediaPlayer.getCurrentPosition();
                int duration = mediaPlayer.getDuration();

                int percent = Math.round((currentPosition + 0.0f) / duration * 100);
                seekBar.setProgress(percent);
                String text = "音乐名： " + mp3Files.get(index)
                        + "时长： " + duration / 1000 / 60 + "分" + duration / 1000 % 60 + "秒"
                        + "当前进度： " + currentPosition / 1000 / 60 + "分" + currentPosition / 1000 % 60 + "秒";
                textView.setText(text);

                if (isPlaying) {
                    float degree = imageViewDisc.getRotation() + 10;
                    if (degree >= 360) degree = 0;
                    imageViewDisc.setRotation(degree);
                }
            }
        }

        @Override
        protected Object doInBackground(String... strings) {
            while (flag) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                publishProgress();
            }
            return null;
        }
    }
}