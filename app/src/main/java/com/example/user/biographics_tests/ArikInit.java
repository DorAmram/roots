package com.example.user.biographics_tests;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;


public class ArikInit extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arik_init);
        Log.i("eliLog", "in arikinit");

        SharedPreferences preferences = getSharedPreferences("filename", MODE_PRIVATE);
        boolean activated = preferences.getBoolean("activated", false);

        if(!activated)
        {
            addStrories();
            preferences.edit().putBoolean("activated", true).apply();
        }

        Intent i = new Intent(ArikInit.this, DorActivity.class);
        i.putExtra("uid", 6);
        startActivity(i);
        finish();
    }

    private void addStrories()
    {
        DBHandler db = new DBHandler(getApplicationContext());
        db.initUnqId(this);

        // Story off birth
        Story arik1 = new Story("born", new GregorianCalendar(1939,2,1));
        String arik1Pic1 = "file:/storage/emulated/0/arik/lul2.jpg";
        String arik1Pic1T = makeThumbnailFromPhoto(Uri.parse(arik1Pic1));
        arik1.addPhoto(this, arik1Pic1, arik1Pic1T);
        int id1  = db.insertStory(arik1);

        // Story of basketBall
        Story arik2 = new Story("Sports", new GregorianCalendar(1955,0,1));
        String arik2Pic1 = "file:/storage/emulated/0/arik/basketball.jpg";
        String arik2Pic1T = makeThumbnailFromPhoto(Uri.parse(arik2Pic1));
        arik2.addPhoto(this, arik2Pic1, arik2Pic1T);
        int id2  = db.insertStory(arik2);
        String arik2Pic2 = "file:/storage/emulated/0/arik/basketball1.jpg";
        String arik2Pic2T = makeThumbnailFromPhoto(Uri.parse(arik2Pic2));
        db.insertMediaToExistStory(id2,"photo", arik2Pic2, arik2Pic2T );
        String arik2Pic3 = "file:/storage/emulated/0/arik/basketball2.jpg";
        String arik2Pic3T = makeThumbnailFromPhoto(Uri.parse(arik2Pic3));
        db.insertMediaToExistStory(id2,"photo", arik2Pic3, arik2Pic3T );
        String arik2Pic4 = "file:/storage/emulated/0/arik/basketball3.jpg";
        String arik2Pic4T = makeThumbnailFromPhoto(Uri.parse(arik2Pic4));
        db.insertMediaToExistStory(id2, "photo", arik2Pic4, arik2Pic4T);

        // Story yoshev hal ha gader
        Story arik3 = new Story("Yoshev hal hagader", new GregorianCalendar(1982,7,1));
        String arik3Pic1 = "file:/storage/emulated/0/arik/yoshev.jpg";
        String arik3Pic1T = makeThumbnailFromPhoto(Uri.parse(arik3Pic1));
        arik3.addPhoto(this, arik3Pic1, arik3Pic1T);
        int id3  = db.insertStory(arik3);
        String arik3Pic2 = "file:/storage/emulated/0/arik/yoshev1.jpg";
        String arik3Pic2T = makeThumbnailFromPhoto(Uri.parse(arik3Pic2));
        db.insertMediaToExistStory(id3, "photo", arik3Pic2, arik3Pic2T);
        String arik3Pic3 = "file:/storage/emulated/0/arik/yoshev2.jpg";
        String arik3Pic3T = makeThumbnailFromPhoto(Uri.parse(arik3Pic3));
        db.insertMediaToExistStory(id3, "photo", arik3Pic3, arik3Pic3T);
        String arik3vid = "file:/storage/emulated/0/arik/yoshev.mp4";
        String arik3vidT = makeThumbnailFromVideo(Uri.parse(arik3vid));
        db.insertMediaToExistStory(id3, "video", arik3vid, arik3vidT);

        // Story big eyes
        Story arik4 = new Story("Big Eyes", new GregorianCalendar(1974,0,1));
        String arik4pic1 = "file:/storage/emulated/0/arik/bigeyes.jpg";
        String arik4pic1T = makeThumbnailFromPhoto(Uri.parse(arik4pic1));
        arik4.addPhoto(this, arik4pic1, arik4pic1T);
        int id4  = db.insertStory(arik4);
        String arik4vid = "file:/storage/emulated/0/arik/bigeyesv.mp4";
        String arik4vidT = makeThumbnailFromVideo(Uri.parse(arik4vid));
        db.insertMediaToExistStory(id4, "video", arik4vid, arik4vidT);

        // Story ani ve ata
        Story arik5 = new Story("Ani veata", new GregorianCalendar(1971,0,1));
        String arik5pic1 = "file:/storage/emulated/0/arik/meandyou1.jpg";
        String arik5pic1T = makeThumbnailFromPhoto(Uri.parse(arik5pic1));
        arik5.addPhoto(this, arik5pic1, arik5pic1T);
        int id5  = db.insertStory(arik5);
        String arik5pic2 = "file:/storage/emulated/0/arik/meandyou.jpg";
        String arik5pic2T = makeThumbnailFromPhoto(Uri.parse(arik5pic2));
        db.insertMediaToExistStory(id5, "photo", arik5pic2, arik5pic2T);
        String arik5aud = "file:/storage/emulated/0/arik/meandyou.mp3";
        db.insertMediaToExistStory(id5, "audio", arik5aud, null);

        //Story lul
        Story arik6 = new Story("lul", new GregorianCalendar(1970,11,4));
        String arik6pic1 = "file:/storage/emulated/0/arik/lul1.jpg";
        String arik6pic1T = makeThumbnailFromPhoto(Uri.parse(arik6pic1));
        arik6.addPhoto(this, arik6pic1, arik6pic1T);
        int id6  = db.insertStory(arik6);
        String arik6pic2 = "file:/storage/emulated/0/arik/lul2.jpg";
        String arik6pic2T = makeThumbnailFromPhoto(Uri.parse(arik6pic2));
        db.insertMediaToExistStory(id6, "photo", arik6pic2, arik6pic2T);
        String arik6pic3 = "file:/storage/emulated/0/arik/lul3.jpg";
        String arik6pic3T = makeThumbnailFromPhoto(Uri.parse(arik6pic3));
        db.insertMediaToExistStory(id6, "photo", arik6pic3, arik6pic3T);
        String arik6pic4 = "file:/storage/emulated/0/arik/lul4.jpg";
        String arik6pic4T = makeThumbnailFromPhoto(Uri.parse(arik6pic4));
        db.insertMediaToExistStory(id6, "photo", arik6pic4, arik6pic4T);
        String arik6vid = "file:/storage/emulated/0/arik/lul.mp4";
        String arik6vidT = makeThumbnailFromVideo(Uri.parse(arik6vid));
        db.insertMediaToExistStory(id6, "video", arik6vid, arik6vidT);

        //Story nahal
        Story arik7 = new Story("Nahal Days", new GregorianCalendar(1959,0,1));
        String arik7pic1 = "file:/storage/emulated/0/arik/nachal1959.jpg";
        String arik7pic1T = makeThumbnailFromPhoto(Uri.parse(arik7pic1));
        arik7.addPhoto(this, arik7pic1, arik7pic1T);
        int id7 = db.insertStory(arik7);
        String arik7vid = "file:/storage/emulated/0/arik/nahal2004.mp4";
        String arik7vidT = makeThumbnailFromVideo(Uri.parse(arik7vid));
        db.insertMediaToExistStory(id7, "video", arik7vid, arik7vidT);

        // Story 1979
        Story arik8 = new Story("good days", new GregorianCalendar(1979,1,1));

        db.close();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_arik_init, menu);
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
    String mCurrentPhotoPath;

    private String makeThumbnailFromVideo(Uri uri)
    {
        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(uri.getPath(),
                MediaStore.Images.Thumbnails.MINI_KIND);
        Bitmap output = Bitmap.createScaledBitmap(bitmap, 200, 200, false);
        FileOutputStream out = null;
        File imageFile = createImageFileForVideo();
        try {
            out = new FileOutputStream(mCurrentPhotoPath);
            // choose JPEG format
            output.compress(Bitmap.CompressFormat.JPEG, 100, out);
            if (out != null) {
                out.flush();
                out.close();
                return mCurrentPhotoPath;
            }
        } catch (Exception e) {
            Log.e("eliLog", "From video.java. make thumbnail failed with exception: " + e.getMessage());

        }
        return null;
    }

    private File createImageFileForVideo() {
        // Create an image file name by time and date.
        long epoch = System.currentTimeMillis();
        String timeStamp = new SimpleDateFormat("MM.dd.yyyy_HH:mm:ss").
                format(new java.util.Date(epoch));
        //To get acsses to external  storage
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        try {
            //Creates a new image.
            File image = File.createTempFile(
                    timeStamp,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
            // Save a file: path for use with ACTION_VIEW intents
            mCurrentPhotoPath =  image.getAbsolutePath();
            return image;
        }catch(IOException e){
            //TODO informative Toast
        }
        return null;
    }
    private String makeThumbnailFromPhoto(Uri uri)
    {
        try
        {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getApplicationContext().getContentResolver(), uri);
            Bitmap output = Bitmap.createScaledBitmap(bitmap, 200, 200, false);
            FileOutputStream out = null;
            File imageFile = createImageFile("thumb");
            out = new FileOutputStream(mCurrentPhotoPath);
            // choose JPEG format
            output.compress(Bitmap.CompressFormat.JPEG, 100, out);
            if (out != null) {
                out.flush();
                out.close();
                return mCurrentPhotoPath;
            }
        } catch (Exception e) {
            Log.e("eliLog", "From Camera.java. make thumbnail failed with exception: " + e.getMessage());
        }
        return null;
    }

    private File createImageFile(String use) {
        // Create an image file name by time and date.
        long epoch = System.currentTimeMillis();
        String timeStamp = new SimpleDateFormat("MM.dd.yyyy_HH:mm:ss").
                format(new java.util.Date(epoch));
        //To get acsses to external  storage
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        try {
            //Creates a new image.
            File image = File.createTempFile(
                    timeStamp,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
            if(use.equals("thumb"))
            {
                mCurrentPhotoPath = image.getAbsolutePath();
            }else {
                // Save a file: path for use with ACTION_VIEW intents
                mCurrentPhotoPath = "file:" + image.getCanonicalPath();
            }
            return image;
        }catch(IOException e) {}
        return null;
    }
}
