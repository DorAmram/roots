package com.example.user.biographics_tests;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class LineRelativeLayout extends RelativeLayout {
    ArrayList<int[]> lines = new ArrayList<>();

    public LineRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }

    public void addLine(int startX, int startY, int endX, int endY)
    {
        int[] toAdd = {startX, startY, endX, endY};
        lines.add(toAdd);
        invalidate();
    }

    public void clearLines()
    {
        lines.clear();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(25f);

        for (int[] line : lines)
        {
            canvas.drawLine(line[0], line[1], line[2], line[3], paint);
        }
    }
}
