package com.example.calanderevents.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.calanderevents.room.AppDatabase;
import com.example.calanderevents.model.Event;

import java.util.List;

public class EventViewModel extends AndroidViewModel {

    AppDatabase appDatabase;
    public EventViewModel(@NonNull Application application) {
        super(application);
        appDatabase = AppDatabase.getINSTANCE(application);
    }

    public void insert(Event event) {
        appDatabase.eventDao().insert(event);
    }

    public void update(int id, String title, String desc, String time, String eventType){
        appDatabase.eventDao().update(id,title, desc, time, eventType);
    }

    public void delete(int id){
        appDatabase.eventDao().delete(id);
    }

    MutableLiveData<List<Event>> _mListEvents = new MutableLiveData<>();
    LiveData<List<Event>> mListEvents = _mListEvents;

    public LiveData<List<Event>> getAllEvents(){

        _mListEvents.postValue(appDatabase.eventDao().getAllEvents());

        return mListEvents;

    }
}
