package com.example.calanderevents.calendar;


import android.text.style.ForegroundColorSpan;
import android.text.style.LineBackgroundSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

public class EventDecorate implements DayViewDecorator {

    String eventName;
    String eventTime;
    int color;
    CalendarDay calendarDay;
    int spanType;
    private static final int[] xOffsets = new int[]{0,-15,15,-30};


    public EventDecorate(String eventName, String eventTime, int color, CalendarDay calendarDay, int spanType) {
        this.eventName = eventName;
        this.eventTime = eventTime;
        this.color = color;
        this.calendarDay = calendarDay;
        this.spanType = spanType;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return calendarDay.equals(day);
    }

    @Override
    public void decorate(DayViewFacade view) {

        LineBackgroundSpan span = new CustomSpan(color, xOffsets[spanType]);
        view.addSpan(span);

    }

    public String getEventName() {
        return eventName;
    }

    public String getEventTime() {
        return eventTime;
    }
}
