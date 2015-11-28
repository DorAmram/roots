package com.example.user.biographics_tests;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.GregorianCalendar;


public class DoubleCirc extends BallView {

    DorView mainCirc;
    DorView littleCirc;

    public DoubleCirc(Context context, int color, boolean includeVideos, int ballSize, String header, int uniqueId, GregorianCalendar date, String imagePath, View v) {
        super(context, color, includeVideos, ballSize, header, uniqueId, date, imagePath, v);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        mainCirc = new DorView(getContext(), color, includeVideos, ballSize, header, uniqueId, date, imagePath, center);
        littleCirc = new DorView(getContext(), Color.BLUE, false, ballSize, "", 0, null, "smallAudio", new View(getContext()));

        int maxSize = b - t;

        addView(mainCirc);
        addView(littleCirc);
//        mainCirc.layout(0, 0, maxSize * 5 / 6, maxSize * 5 / 6);
        mainCirc.layout(0, 0, maxSize, maxSize);
        littleCirc.layout(maxSize / 2, 0, maxSize, maxSize / 2);
        //Parameters above are: left top right bottom
    }

    public void select()
    {
        super.select();
        mainCirc.select();
        littleCirc.select();
    }

    public void deselect()
    {
        super.deselect();
        mainCirc.deselect();
        littleCirc.deselect();
    }
}
