package com.example.weatherapp02.activities;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.weatherapp02.R;
import com.example.weatherapp02.adapter.EventAdapter;
import com.example.weatherapp02.data.Event;
import com.example.weatherapp02.databinding.FragmentTimeBinding;
import com.example.weatherapp02.activities.TimeViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import java.util.Date;

public class TimeFragment extends Fragment {
    private FragmentTimeBinding binding;
    private TimeViewModel timeViewModel;
    private EventAdapter eventAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_time, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        timeViewModel = new ViewModelProvider(this).get(TimeViewModel.class);

        MaterialCalendarView calendarView = binding.calendarView;
        calendarView.setSelectedDate(new Date());

        TextInputEditText eventInput = binding.eventInput;
        MaterialButton addButton = binding.addButton;

        addButton.setOnClickListener(v -> {
            String eventText = eventInput.getText().toString().trim();
            if (!eventText.isEmpty()) {
                timeViewModel.addEvent(calendarView.getSelectedDate().getDate(), eventText);
                eventInput.setText("");
                Toast.makeText(getContext(), "日程已添加", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "请输入日程内容", Toast.LENGTH_SHORT).show();
            }
        });

        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        timeViewModel.getAllEvents().observe(getViewLifecycleOwner(), events -> {
            if (eventAdapter == null) {
                eventAdapter = new EventAdapter(events, event -> {
                    // 删除操作
                    timeViewModel.deleteEvent(event);
                    Toast.makeText(getContext(), "日程已删除", Toast.LENGTH_SHORT).show();
                });
                recyclerView.setAdapter(eventAdapter);
            } else {
                eventAdapter.updateEvents(events);
            }
        });
    }
}