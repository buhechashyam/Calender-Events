package com.example.calanderevents.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.io.Serializable;

@Entity(tableName = "event")
public class Event implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    int id;

    String eventName;
    String eventDesc;
    @TypeConverters(CalenderDayConverters.class)
    CalendarDay eventDate;
    String eventTime;
    String eventType;
    int color;
    int spanNum;

    public int getSpanNum() {
        return spanNum;
    }

    public void setSpanNum(int spanNum) {
        this.spanNum = spanNum;
    }

    public Event(String eventName, String eventDesc, CalendarDay eventDate, String eventTime, String eventType, int color, int spanNum) {
        this.eventName = eventName;
        this.eventDesc = eventDesc;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.eventType = eventType;
        this.color = color;
        this.spanNum = spanNum;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getId(){
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDesc() {
        return eventDesc;
    }

    public void setEventDesc(String eventDesc) {
        this.eventDesc = eventDesc;
    }

    public CalendarDay getEventDate() {
        return eventDate;
    }

    public void setEventDate(CalendarDay eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {

    }
}
