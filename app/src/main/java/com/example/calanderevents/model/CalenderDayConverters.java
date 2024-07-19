package com.example.calanderevents.model;


import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.prolificinteractive.materialcalendarview.CalendarDay;

public class CalenderDayConverters {

    @TypeConverter
    public static String toString(CalendarDay calendarDay){

        Gson gson = new Gson();
        String day = gson.toJson(calendarDay);
        return day;
    }
    @TypeConverter
    public static CalendarDay toCalenderDay(String s){
        Gson gson = new Gson();
        CalendarDay calendarDay = gson.fromJson(s,CalendarDay.class);

        return calendarDay;
    }
}
