package com.example.user.biographics_tests;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class BallView extends ViewGroup{

    protected int color;
    protected boolean includeVideos;
    protected String imagePath;
    protected GregorianCalendar date;
    protected String header;
    protected boolean selected;
    protected Context context;
    public int uniqueId;
    public int ballSize;
    public View center;
    public ImageView backGroundView;

    public BallView(Context context, int color, boolean includeVideos, int ballsize, String header, int uniqueId, GregorianCalendar date, String imagePath, View v) //throws IOException //throws Exception
    {
        super(context);
        this.color = color;
        this.includeVideos = includeVideos;
        this.header = header;
        this.ballSize = ballsize;
        center = v;
        this.date = date;
        this.imagePath = imagePath;
        this.uniqueId = uniqueId;
        this.selected = false;
        this.context = context;
//        setBackgroundColor(Color.BLACK);
        v.setLayoutParams(new ViewGroup.LayoutParams(0, 0));
        setWillNotDraw(false);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
    }

    @Override
    public DorView clone() throws CloneNotSupportedException {
        return (DorView) super.clone();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        invalidate();
    }

    public View getCenter(){
        return this.center;
    }

    public void setCenter(View center){
        this.center = center;
    }

    public String getHeader(){
        return this.header;
    }

    public void setHeader(String header){
        this.header = header;
    }

    public GregorianCalendar getDate(){
        return this.date;
    }

    public void setDate(GregorianCalendar date){
        this.date = date;
    }

    public String getImagePath(){
        return this.imagePath;
    }

    public void setImagePath(String imagePath){
        this.imagePath = imagePath;
    }

    public void setUniqueId(int id) { this.uniqueId = id; }

    public int getUniqueId() { return this.uniqueId; }

    public int getColor(){
        return this.color;
    }

    public void setColor(int color){
        this.color = color;
    }

    public int getYear() {
        return this.date.get(Calendar.YEAR);
    }

    public void select()
    {
        this.selected = true;
    }

    public void deselect()
    {
        this.selected = false;
    }

    public boolean isBallSelected()
    {
        return selected;
    }

    public float getXCoordinate(){
        return getWidth() / 2 + getPaddingLeft();
    }

    public float getYCoordinate(){
        return  getHeight() / 2 + getPaddingTop();
    }
}
