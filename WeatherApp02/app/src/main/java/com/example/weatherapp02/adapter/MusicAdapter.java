package com.example.weatherapp02.adapter;

import android.annotation.SuppressLint;
import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp02.R;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {

    private List<String> mp3Files;

    public MusicAdapter(List<String> mp3Files) {
        this.mp3Files = mp3Files;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music, parent, false);
        return new ViewHolder(view);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String mp3File = mp3Files.get(position);
        long duration = 0;
        try {
            duration = getMP3Duration(mp3File);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String name = mp3File.substring(0, mp3File.lastIndexOf("."));
        String durationStr = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
        holder.textViewMusicName.setText(name + " (" + durationStr + ")");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            }
        });
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }


    @Override
    public int getItemCount() {
        return mp3Files.size();
    }
    private long getMP3Duration(String mp3File) throws IOException {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "Pictures" + File.separator + mp3File);
        retriever.setDataSource(file.getAbsolutePath());
        String durationStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        retriever.release();
        return Long.parseLong(durationStr);
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewMusicName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMusicName = itemView.findViewById(R.id.textViewMusicName);
        }
    }

}
