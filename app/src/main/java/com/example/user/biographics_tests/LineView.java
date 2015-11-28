package com.example.user.biographics_tests;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;


public class LineView extends View {

    int color;
    boolean topLeft;
    float _startY;
    float _endX;
    float _endY;
    Paint paint;



    public LineView(Context context, float startX, float startY, float endX, float endY){
        super(context);
        color = Color.BLUE;
        this._startX = startX;
        this._startY = startY;
        this._endX = endX;
        this._endY = endY;
        this.topLeft = false;
        this.setBackgroundColor(Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int topX, bottomX;

        if (topLeft)
        {
            topX = 0;
            bottomX = getWidth();
        }
        else
        {
            topX = getWidth();
            bottomX = 0;
        }

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(color);
        paint.setStrokeWidth(3f);

//        canvas.drawLine(topX, 0, bottomX, getHeight(), paint);
        canvas.drawLine(_startX, _startY, _endX, _endY, paint);
    }


    public float get_startX() {
        return _startX;
    }

    float _startX;

    public float get_endY() {
        return _endY;
    }

    public float get_endX() {
        return _endX;
    }

    public float get_startY() {
        return _startY;
    }

    public void set_endY(float _endY) {
        this._endY = _endY;
    }

    public void set_endX(float _endX) {
        this._endX = _endX;
    }

    public void set_startY(float _startY) {
        this._startY = _startY;
    }

    public void set_startX(float _startX) {
        this._startX = _startX;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public Paint getPaint() {
        return paint;
    }

//    @Override
//    protected void onDraw(Canvas canvas) {
//
////        Paint paint = new Paint();
//        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
////        paint.setAntiAlias(true);
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setColor(color);
//        paint.setStrokeWidth(2.5f);
//
//        canvas.drawLine(_startX, _startY, _endX, _endY, paint);
//    }





}
