package com.example.user.biographics_tests;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Calendar;

public class miniView extends View {

    int color;
    float radios;
    String name;
    String imagePath;
    public View center;
    public ImageView backGroundView;

    public miniView(Context context, int color, String name, String imagePath){
        super(context);
        this.name = name;
        this.color = color;
        this.radios = (float)Math.min(getWidth(), getHeight()) / 2;
        this.imagePath = imagePath;
    }

    @Override
    public miniView clone() throws CloneNotSupportedException {
        return (miniView) super.clone();
    }

    public View getCenter(){
        return this.center;
    }

    public void setCenter(View center){
        this.center = center;
    }

    public String getImagePath(){
        return this.imagePath;
    }

    public void setImagePath(String imagePath){
        this.imagePath = imagePath;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public int getColor(){
        return this.color;
    }

    public void setColor(int color){
        this.color = color;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float xMid =  getXCoordinate();
        float yMid =  getYCoordinate();
        float radius = Math.min(getWidth(), getHeight()) / 2;
//        this.radios = radius;
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawCircle(xMid, yMid, radius, paint);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.parseColor("#122f40"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);

        canvas.drawCircle(xMid, yMid, radius - 3f, paint);
    }

    //TODO: create oncick method that opens the options view

    public float getRadios() { return this.radios; }

    public float getXCoordinate(){
        return getWidth() / 2 + getPaddingLeft();
    }

    public float getYCoordinate(){
        return getHeight() / 2 + getPaddingTop();
    }

    public void centering(RelativeLayout layout)
    {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(0, 0);
        layoutParams.addRule(RelativeLayout.ALIGN_TOP, getId());
        layoutParams.addRule(RelativeLayout.ALIGN_LEFT, getId());
        layoutParams.setMargins(DorActivity.SIZE / 2, DorActivity.SIZE / 2, 0, 0);

        layout.addView(center, layoutParams);
    }

}
