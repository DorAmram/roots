package com.example.user.biographics_tests;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;

public class Story implements Comparable<Story>{

    private String name;
    private GregorianCalendar storyDate;
    private ArrayList<String> photos;
    private ArrayList<String> photosThumb;
    private ArrayList<String> videos;
    private ArrayList<String> videosThumb;
    private ArrayList<String> audios;
    private String location;
    private int id;
    private int uniqueId;

    //Globals
    private static String mCurrentPhotoPath;

    public Story(String name, GregorianCalendar storyDate){
        this.name = name;
        this.storyDate = storyDate;
        location = "";
        audios = new ArrayList<String>();
        photos = new ArrayList<String>();
        videos = new ArrayList<String>();
        photosThumb = new ArrayList<String>();
        videosThumb = new ArrayList<String>();
    }

    public String getDefaultTimeLinePhoto(){

        if(videos.size() > 0)
        {
            return videosThumb.get(0);
        }
        else if (photos.size() > 0) {
            return photosThumb.get(0);
        }
        else
            return "Audio" ; //TODO default audio photo. is it working??
    }

    public String getAudioAt(int place){
        return this.audios.get(place);
    }

    public int audiosSize()
    {
        return audios.size();
    }

    public String getVideoAt(int place){
        return this.videos.get(place);
    }

    public int videosSize()
    {
        return videos.size();
    }

    public String getPhotoAt(int place){
        return this.photos.get(place);
    }

    public int photosSize()
    {
        return photos.size();
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public void addVideo(String videoPath, String tPath){
        this.videos.add(videoPath);
        this.videosThumb.add(tPath);
/*        Uri uri = Uri.parse(videoPath);
        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(uri.getPath(),
                MediaStore.Images.Thumbnails.MINI_KIND);
        Bitmap output = Bitmap.createScaledBitmap(bitmap, 200, 200, false);
        FileOutputStream out = null;
        File imageFile = createImageFile();
        try {
            out = new FileOutputStream(mCurrentPhotoPath);
            // choose JPEG format
            output.compress(Bitmap.CompressFormat.JPEG, 100, out);
            if (out != null) {
                out.flush();
                out.close();
                videosThumb.add(mCurrentPhotoPath);
            }
        } catch (Exception e) {
            // Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            // manage exception
        }*/
    }

    public void addPhoto(Context context,String photoPath, String tPath){
        this.photos.add(photoPath);
        photosThumb.add(tPath);
 /*       Uri uri = Uri.parse(photoPath);
        try
        {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            Bitmap output = Bitmap.createScaledBitmap(bitmap, 200, 200, false);
            FileOutputStream out = null;
            File imageFile = createImageFile();
            out = new FileOutputStream(mCurrentPhotoPath);
            // choose JPEG format
            output.compress(Bitmap.CompressFormat.JPEG, 100, out);
            if (out != null) {
                out.flush();
                out.close();
                photosThumb.add(mCurrentPhotoPath);
            }
        } catch (Exception e) {
            // Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            // manage exception
        }*/
    }

    private File createImageFile() {
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

    public String getVideoThumbAt(int location){
        return this.videosThumb.get(location);
    }

    public String getPhotosThumbAt(int location){
        return this.photosThumb.get(location);
    }

    public void addAudio(String audioPath){
        this.audios.add(audioPath);
    }

    public GregorianCalendar getDate(){
        return this.storyDate;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getUniqueId()
    {
        return uniqueId;
    }

    public void setUniqueId(int uniqueId)
    {
        this.uniqueId = uniqueId;
    }

    public ArrayList<String> dbGetPhotos()
    {
        return photos;
    }

    public ArrayList<String> dbGetVideos()
    {
        return videos;
    }

    public ArrayList<String> dbGetAudios()
    {
        return audios;
    }

    public ArrayList<String> dbGetPhotosThumb()
    {
        return photosThumb;
    }

    public ArrayList<String> dbGetVideosThumb(){return videosThumb;}

    public void audioPlayer(String path, String fileName){
    }

    public String getLocation()
    {
        return this.location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    @Override
    public int compareTo(Story another) {
        return this.getDate().compareTo(another.getDate());
    }
}
