package com.example.weatherapp02.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.weatherapp02.R;
import com.example.weatherapp02.data.Event;
import java.text.SimpleDateFormat;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<Event> eventList;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private OnDeleteClickListener onDeleteClickListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(Event event);
    }

    public EventAdapter(List<Event> eventList, OnDeleteClickListener onDeleteClickListener) {
        this.eventList = eventList;
        this.onDeleteClickListener = onDeleteClickListener;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.dateTextView.setText(dateFormat.format(event.date));
        holder.descriptionTextView.setText(event.description);
        holder.deleteButton.setOnClickListener(v -> onDeleteClickListener.onDeleteClick(event)); // 设置删除按钮点击事件
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView;
        TextView descriptionTextView;
        ImageButton deleteButton; // 添加删除按钮

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    public void updateEvents(List<Event> events) {
        this.eventList = events;
        notifyDataSetChanged();
    }
}
