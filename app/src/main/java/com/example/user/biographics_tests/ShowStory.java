package com.example.user.biographics_tests;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ShowStory  extends AppCompatActivity {

    private GridView Audios;
    private GridView Images;
    private GridView Videos;
    private Story toShow;
    private int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_activity_show_story);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        Intent getStoryId = getIntent();
        id = getStoryId.getIntExtra("idOrNewOrCampaignQ", -1);
        // if there was nothing passed in the idOrNewOrCampaignQ
        if (id == -1) {
            Log.e("eliLog", "From: ShowStory.java. id badly passed from dorActivity and cant be parsed");
        }
        TextView title = (TextView) findViewById(R.id.TitleView);
        TextView date = (TextView) findViewById(R.id.DateView);
        ImageView img = (ImageView) findViewById(R.id.ThumbView);
        try {
            //open db
            DBHandler db = new DBHandler(getApplicationContext());
            db.initUnqId(this);
            toShow = db.getStoryById(id);
            //get tumbImg
            Log.i("eliLog", "file:" + toShow.getDefaultTimeLinePhoto());
            Uri uri = Uri.parse("file:" + toShow.getDefaultTimeLinePhoto());
            //Bitmap bitmap;
            if( toShow.getDefaultTimeLinePhoto().compareTo("Audio") == 0){
                Picasso.with(this).
                        load(R.drawable.audiologo).
                        into(img);
            }
            else {
                Picasso.with(this)
                        .load(uri)
                        .transform(new CircleTransform())
                        .into(img);
            }
            img.bringToFront();
            title.setText(toShow.getName());
            GregorianCalendar cDate = toShow.getDate();
            date.setText(cDate.get(Calendar.DAY_OF_MONTH) + "." + cDate.get(Calendar.MONTH) + "." + cDate.get(Calendar.YEAR));
            //init images gridView
            Images = (GridView) findViewById(R.id.photos);
            Images.setAdapter(new ImageAdapter(this, toShow));
            Images.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent,
                                        View view, int position, long id) {
                    //shows the image by intent
                    String path = toShow.getPhotoAt(position);
                    Uri uri = Uri.parse("file:"+path);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.setDataAndType(uri, "image/jpeg");
                    startActivityForResult(intent,1);
                }
            });

            //init videos gridView
            Videos = (GridView) findViewById(R.id.videos);
            Videos.setAdapter(new VideoAdapter(this, toShow));
            Videos.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent,
                                        View view, int position, long id) {
                    //play video by intent
                    String videoPath = "file:" + toShow.getVideoAt(position);
                    Uri videoUri = Uri.parse(videoPath);
                    Intent intent = new Intent(Intent.ACTION_VIEW, videoUri);
                    intent.setDataAndType(videoUri, "video/mp4");
                    startActivityForResult(intent,1);
                }
            });
            Videos.setVisibility(View.INVISIBLE);
            //init audios gridView
            Audios = (GridView) findViewById(R.id.audios);
            Audios.setAdapter(new AudioAdapter(this, toShow));
            Audios.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent,
                                        View view, int position, long id) {
                    //play audio by MediaPlayer
                    String path = toShow.getAudioAt(position);
                    Uri audioUri = Uri.parse(path);
                    Intent playAudio = new Intent();
                    playAudio.setAction(Intent.ACTION_VIEW);
                    playAudio.setDataAndType(audioUri, "video/*");
                    startActivity(playAudio);
                }
            });
            Audios.setVisibility(View.INVISIBLE);

        } catch (Exception e) {
            if (e.getMessage().contains("Parse")) {
            Log.e("eliLog", "problem with db.getStoryById");
            }
            else {
                Log.e("eliLog", "From ShowStory with exception: " + e.getMessage());
            }
        }
        //display the media by priority.
        if(toShow.photosSize() > 0){
            Audios.setVisibility(View.INVISIBLE);
            Videos.setVisibility(View.INVISIBLE);
            Images.setVisibility(View.VISIBLE);
        }
        else if(toShow.videosSize() > 0){
            Images.setVisibility(View.INVISIBLE);
            Audios.setVisibility(View.INVISIBLE);
            Videos.setVisibility(View.VISIBLE);
        }
        else if(toShow.audiosSize() > 0){
            Images.setVisibility(View.INVISIBLE);
            Videos.setVisibility(View.INVISIBLE);
            Audios.setVisibility(View.VISIBLE);

        }
    }
    //----------------------BUTTONS--------------------------------

    public void photoDisplay(View v){
        if(Audios != null){
        Audios.setVisibility(View.INVISIBLE);}
        if(Videos != null){
        Videos.setVisibility(View.INVISIBLE);}
        Images.setVisibility(View.VISIBLE);
    }
    public void videoDisplay(View v) {
        if(Images != null){
        Images.setVisibility(View.INVISIBLE);}
        if(Audios != null){
        Audios.setVisibility(View.INVISIBLE);}
        Videos.setVisibility(View.VISIBLE);
    }

    public void audioDisplay(View v) {
        if(Images != null){
        Images.setVisibility(View.INVISIBLE);}
        if(Videos != null){
        Videos.setVisibility(View.INVISIBLE);}
        Audios.setVisibility(View.VISIBLE);
    }

    public void addContent(View view)
    {
        Intent addContent = new Intent(ShowStory.this, MediaChooserActivity.class);
        addContent.putExtra("idOrNewOrCampaignQ", id);
        startActivity(addContent);
    }
    //----------------------General---------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_story, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
//------------------------------Trash----------------------------------

//    private Bitmap getCroppedBitmap(Bitmap bitmap) {
//
//        float xCoor =  bitmap.getWidth() / 2;
//        float yCoor =  bitmap.getHeight() /2;
//        float radios = Math.min(bitmap.getWidth(), bitmap.getHeight()) / 2;
//        //Bottum, Left, Right, Top
//
//        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
////        Bitmap output = Bitmap.createBitmap(ballSize, ballSize, Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(output);
//
//
//
//        //Drawing bitmap circle
//        final int color = 0xff424242;
//        final Rect rect = new Rect((int)(xCoor - radios), (int)(yCoor - radios), bitmap.getWidth(), bitmap.getHeight());
//        final RectF rectF = new RectF(rect);
//        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        paint.setAntiAlias(true);
//        paint.setColor(color);
//        canvas.drawOval(rectF, paint);
//        canvas.drawARGB(0, 0, 0, 0);
//
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//        canvas.drawBitmap(bitmap, rect, rect, paint);
//        bitmap.recycle();
//
//        //rescale bitmap size to match the wanted size
////        if(imagePath.compareTo("smallAudio") != 0){
////            Bitmap _bmp = Bitmap.createScaledBitmap(output, ballSize, ballSize, false);
////            return _bmp;
////        }
//        return output;
//
