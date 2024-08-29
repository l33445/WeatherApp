package com.example.weatherapp02.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.Date;
import java.util.List;

@Dao
public interface EventDao {
    @Insert
    void insert(Event event);

    @Query("SELECT * FROM event WHERE date = :date")
    LiveData<List<Event>> getEventsByDate(Date date);

    @Query("SELECT * FROM event ORDER BY date DESC")
    LiveData<List<Event>> getAllEvents();

    @Delete
    void delete(Event event); // 添加删除方法
}
