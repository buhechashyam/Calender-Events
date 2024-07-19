package com.example.calanderevents.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.calanderevents.model.Event;

import java.util.List;

@Dao
public interface EventDao {

    @Insert
    void insert(Event event);

    @Query("DELETE FROM event WHERE id = :id")
    void delete(int id);

    @Query("UPDATE event SET eventName = :title , eventDesc = :desc, eventTime = :time , eventType = :eventType WHERE id = :id")
    void update(int id, String title, String desc, String time, String eventType);
    @Query("SELECT * FROM event")
    List<Event> getAllEvents();

}
