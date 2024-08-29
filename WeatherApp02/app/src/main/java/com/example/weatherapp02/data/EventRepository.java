package com.example.weatherapp02.data;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventRepository {
    private EventDao eventDao;
    private ExecutorService executorService;

    public EventRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        eventDao = db.eventDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(Event event) {
        executorService.execute(() -> eventDao.insert(event));
    }

    public LiveData<List<Event>> getEventsByDate(Date date) {
        return eventDao.getEventsByDate(date);
    }

    public LiveData<List<Event>> getAllEvents() {
        return eventDao.getAllEvents();
    }

    public void delete(Event event) {
        executorService.execute(() -> eventDao.delete(event)); // 添加删除方法
    }
}
