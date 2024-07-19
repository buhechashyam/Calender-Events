package com.example.calanderevents.calendar;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.style.LineBackgroundSpan;

public class CenterTextSpan implements LineBackgroundSpan {
    private final String text;

    public CenterTextSpan(String text) {
        this.text = text;
    }

    @Override
    public void drawBackground(Canvas canvas, Paint paint, int left, int right, int top, int baseline, int bottom, CharSequence text, int start, int end, int lineNumber) {
        float width = paint.measureText(text.subSequence(start, end).toString());
        float x = (left + right) / 2f - width / 2f;
        canvas.drawText(text, start, end, x, baseline, paint);
    }
}
