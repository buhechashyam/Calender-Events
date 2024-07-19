package com.example.calanderevents.room;

import android.app.Application;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.calanderevents.model.Event;

@Database(entities = {Event.class},version = 2,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract EventDao eventDao();

    public static AppDatabase INSTANCE = null;

    public static AppDatabase getINSTANCE(Application application) {
        if (INSTANCE==null){
            INSTANCE = Room.databaseBuilder(application,AppDatabase.class,"event-db").allowMainThreadQueries().build();
        }
        return INSTANCE;
    }
}
