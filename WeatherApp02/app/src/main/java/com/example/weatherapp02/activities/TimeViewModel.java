package com.example.weatherapp02.activities;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.weatherapp02.data.Event;
import com.example.weatherapp02.data.EventRepository;
import java.util.Date;
import java.util.List;

public class TimeViewModel extends AndroidViewModel {
    private EventRepository repository;

    public TimeViewModel(@NonNull Application application) {
        super(application);
        repository = new EventRepository(application);
    }

    public void addEvent(Date date, String description) {
        Event event = new Event(date, description);
        repository.insert(event);
    }

    public LiveData<List<Event>> getEventsByDate(Date date) {
        return repository.getEventsByDate(date);
    }

    public LiveData<List<Event>> getAllEvents() {
        return repository.getAllEvents();
    }

    public void deleteEvent(Event event) {
        repository.delete(event); // 添加删除方法
    }
}