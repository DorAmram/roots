package com.example.user.biographics_tests;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class TestLineView extends View {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public TestLineView(Context context) {
        super(context);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2.5f);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawLine(0, 50, 350, 150, paint);
    }

}