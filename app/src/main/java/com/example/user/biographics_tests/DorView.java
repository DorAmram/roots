package com.example.user.biographics_tests;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.graphics.BitmapFactory;

import java.util.GregorianCalendar;


public class DorView extends BallView {

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
    }

    public DorView(Context context, int color, boolean includeVideos, int ballSize, String header, int uniqueId, GregorianCalendar date, String imagePath, View v) //throws IOException //throws Exception
    {
        super(context, color, includeVideos, ballSize, header, uniqueId, date, imagePath, v);
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

    private Bitmap overlay(Bitmap bmp1, Bitmap bmp2) {

        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);

        Bitmap stretched_bmp = Bitmap.createScaledBitmap(bmp2, ballSize, ballSize, false);

        float centreX = (canvas.getWidth() - bmp1.getWidth()) / 2;
        float centreY = (canvas.getHeight() - bmp1.getHeight()) / 2;

        canvas.drawBitmap(bmp1, centreX, centreY, null);
        canvas.drawBitmap(stretched_bmp, centreX, centreY, null);

//        canvas.drawBitmap(bmp1, new Matrix(), null);
//        canvas.drawBitmap(bmp2, new Matrix(), null);

        return bmOverlay;
    }

    private Bitmap getCroppedBitmap(Bitmap bitmap, float xCoor, float yCoor, float radios) {

        //Bottum, Left, Right, Top

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

//        //Drawing background black circle
//        int increaseFactor = 30;
//        final Rect backgroundRect = new Rect((int)(xCoor - radios - increaseFactor),
//                (int)(yCoor - radios - increaseFactor), bitmap.getWidth() + increaseFactor, bitmap.getHeight() + increaseFactor);
//        final RectF backgroundRectF = new RectF(backgroundRect);
//        final Paint backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        backgroundPaint.setAntiAlias(true);
//        backgroundPaint.setColor(Color.BLACK);
//        canvas.drawOval(backgroundRectF, backgroundPaint);
////        backgroundPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//        canvas.drawBitmap(bitmap, backgroundRect, backgroundRect, backgroundPaint);

        //Drawing bitmap circle

        final int color = 0xff424242;
        final Rect rect = new Rect((int)(xCoor - radios), (int)(yCoor - radios), bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);
        canvas.drawARGB(0, 0, 0, 0);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        bitmap.recycle();

        //rescale bitmap size to match ball size
        if(imagePath.compareTo("smallAudio") != 0){
            Bitmap _bmp = Bitmap.createScaledBitmap(output, ballSize, ballSize, false);
            return _bmp;
        }
        return output;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float xMid =  getXCoordinate();
        float yMid =  getYCoordinate();
        float radius = Math.min(getWidth(), getHeight()) / 2;
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);

        //makes the path string an Uri
        Uri uri = Uri.parse("file:" + imagePath);
        Bitmap bitmap = null;
        if(imagePath.compareTo("Audio") == 0){
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bigaudiologo);
        }if(imagePath.compareTo("smallAudio") == 0){
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.audiologo);
        }else{
            try {
                //on file as bitmap
                bitmap = MediaStore.Images.Media.getBitmap(this.context.getContentResolver(), uri);
            } catch (Exception e) {
                Log.e("eliLog", "PicShow with exception: " + e.getMessage());
            }
        }

        if(bitmap != null) {
            //cut the bitmap to a circle
            if(includeVideos){
                bitmap = overlay(getCroppedBitmap(bitmap, xMid, yMid, radius), BitmapFactory.decodeResource(getResources(), R.drawable.videoplay));
            }else{
                bitmap = getCroppedBitmap(bitmap, xMid, yMid, radius);
            }
            //draws the bitmap
            canvas.drawBitmap(bitmap, xMid - radius, yMid - radius, paint);
        }
    }

//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        float xMid =  getXCoordinate();
//        float yMid =  getYCoordinate();
//        float radius = Math.min(getWidth(), getHeight()) / 2;
//        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        paint.setColor(color);
//        paint.setStyle(Paint.Style.FILL);
//
//        canvas.drawCircle(xMid, yMid, radius, paint);
//
//        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        paint.setColor(Color.parseColor("#122f40"));
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setStrokeWidth(7);
//
//        canvas.drawCircle(xMid, yMid, radius - 2.5f, paint);
//
//        if (this.selected)
//        {
//            Paint overlayPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//            overlayPaint.setColor(Color.argb(70, 0, 128, 255));
//            overlayPaint.setStyle(Paint.Style.FILL);
//            canvas.drawCircle(xMid, yMid, radius, overlayPaint);
//        }
//    }

    public void centering(RelativeLayout layout)
    {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(0, 0);
        layoutParams.addRule(RelativeLayout.ALIGN_TOP, getId());
        layoutParams.addRule(RelativeLayout.ALIGN_LEFT, getId());
        layoutParams.setMargins(DorActivity.SIZE / 2, DorActivity.SIZE / 2, 0, 0);
        layout.addView(center, layoutParams);
    }

    public void select()
    {
        super.select();
        invalidate();
    }

    public void deselect()
    {
        super.deselect();
        invalidate();
    }
}
