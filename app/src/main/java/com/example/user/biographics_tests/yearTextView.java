package com.example.user.biographics_tests;


import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.AttributeSet;
import android.widget.TextView;


public class yearTextView extends TextView {

    int color;
    private int year;

    public yearTextView(Context context, int year){
        super(context);
        color = Color.BLACK;
        this.year = year;
    }


    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawText("testing text",canvas.getWidth()/2,canvas.getHeight()/2,paint);

        //TODO: how to show text on text view?



    }






}
