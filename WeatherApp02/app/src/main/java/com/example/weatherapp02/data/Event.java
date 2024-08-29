package com.example.weatherapp02.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Event {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public Date date;
    public String description;

    public Event(Date date, String description) {
        this.date = date;
        this.description = description;
    }
}
