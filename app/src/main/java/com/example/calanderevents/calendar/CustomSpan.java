package com.example.calanderevents.calendar;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.prolificinteractive.materialcalendarview.spans.DotSpan;

public class CustomSpan extends DotSpan {

    int color;
    int offset;
    float radius = 5f;

    CustomSpan(int color, int offset) {
        this.color = color;
        this.offset = offset;
    }


    @Override
    public void drawBackground(Canvas canvas, Paint paint, int left, int right, int top, int baseline, int bottom, CharSequence charSequence, int start, int end, int lineNum) {
        super.drawBackground(canvas, paint, left, right, top, baseline, bottom, charSequence, start, end, lineNum);

        int oldColor = paint.getColor();
        if (color != 0) {
            paint.setColor(color);
        }
        int x = ((left + right) / 2);
        canvas.drawCircle(x + offset, bottom + radius, radius, paint);

        paint.setColor(oldColor);
    }
}
