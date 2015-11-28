package com.example.user.biographics_tests;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class StoryDetails extends AppCompatActivity {

    String  mCurrentPhotoPath;
    private Spinner year;
    private Spinner month;
    private Spinner day;
    private String idOrNewOrCampaignQ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_details);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        //get the extra idOrNewOrCampaignQ str
        Intent i = getIntent();
        idOrNewOrCampaignQ = i.getStringExtra("idOrNewOrCampaignQ");
        // if there was nothing passed in the idOrNewOrCampaignQ
        if(idOrNewOrCampaignQ == null)
        {
            Log.e("eliLog", "From: StoryDetails.java. idOrNewOrCampaignQ was empty in get extra");
        }

        //init years spinner. DON'T FORGET TO COPY THE years_selection ARRAY FROM VALUES FOLDER
        year = (Spinner) findViewById(R.id.yearSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> yearAdapter = ArrayAdapter.createFromResource(this,
                R.array.years_selection, R.layout.spinner_item);
        // Specify the layout to use when the list of choices appears
        yearAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        // Apply the adapter to the spinner
        year.setAdapter(yearAdapter);

        //init MONTH spinner. DON'T FORGET TO COPY THE month_selection ARRAY FROM VALUES FOLDER
        month = (Spinner) findViewById(R.id.monthSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(this,
                R.array.month_selection, R.layout.spinner_item);

        // Specify the layout to use when the list of choices appears
        monthAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        // Apply the adapter to the spinner
        month.setAdapter(monthAdapter);
        //init day spinner. DON'T FORGET TO COPY THE day_selection ARRAY FROM VALUES FOLDER
        day = (Spinner) findViewById(R.id.daySpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> dayAdapter = ArrayAdapter.createFromResource(this,
                R.array.day_selection, R.layout.spinner_item);
        // Specify the layout to use when the list of choices appears
        dayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        // Apply the adapter to the spinner
        day.setAdapter(dayAdapter);

        ImageButton submit = (ImageButton) this.findViewById(R.id.SubmitButton);
        EditText editTitle = (EditText) findViewById(R.id.EditTitle);
        Bundle Title = getIntent().getExtras();
        String title = Title.getString("idOrNewOrCampaignQ");
        if (title != "new") {

            editTitle.setText(title);
            //editTitle.setEnabled(false);
        }

    }

    public void submit(View view) throws Exception {

        String yearStr = year.getSelectedItem().toString();
        String monthStr = month.getSelectedItem().toString();
        String dayStr = day.getSelectedItem().toString();
        int yearInt;
        // parse Year
        try {
            yearInt = Integer.parseInt(yearStr);
        }
        //if there was no year selected. do nothing
        catch (Exception e) {
            Toast.makeText(this.getApplicationContext(), "??? ???? ??? ????", Toast.LENGTH_LONG).show();
            return;
        }
        // try to parse month. if raises exception it has not been selected, assign 01
        try {
            Integer.parseInt(monthStr);
        } catch (Exception e) {
            monthStr = "01";
        }
        // try to parse day. if raises exception it has not been selected, assign 01
        try {
            Integer.parseInt(dayStr);
        } catch (Exception e) {
            dayStr = "01";
        }
        DateFormat df = new SimpleDateFormat("dd MM yyyy");
        Date d = new Date();

        try {
            d = df.parse(dayStr + " " + monthStr + " " + yearStr);
        } catch (Exception e) {
            Log.e("eliLog", "From: StoryDetails.java. date could not be parsed.");
        }
        //MY edit TODO chek me
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(d);


        //gets the text  and date from the input fields
        EditText editTitle = (EditText) findViewById(R.id.EditTitle);

        String title = editTitle.getText().toString();

        //Add location
        EditText location = (EditText) findViewById(R.id.EditLocation);
        String loc = location.getText().toString();

        //create new story
        Story newStory = new Story(title, cal);
        newStory.setLocation(loc);
        //get the file path and add the proper data member to the story.
        Bundle Data = getIntent().getExtras();
        if (Data == null)
            return;
        String path = Data.getString("path");
        String tPath =  Data.getString("tPath");
        String type = Data.getString("type");
        String scndTitle = Data.getString("2ndTitle");
        //Photo
        if (type.equals("Photo")) {
            newStory.addPhoto(this, path, tPath,scndTitle);
        }
        //Audio
        else if (type.equals("Audio")) {
            newStory.addAudio(path,scndTitle);
        } else if (type.equals("Video")) {
            newStory.addVideo(path,tPath,scndTitle);
        }
        else // audio recorded!!! not mp3 ext.
        {
            Log.i("eliLog", "audio added with no mp3 ending");
            newStory.addAudio(path,scndTitle);
        }
            DBHandler db = new DBHandler(getApplicationContext());
            db.initUnqId(this);
            int id = db.insertStory(newStory);
            db.close();
            //Go to timeline
            Intent i = new Intent(StoryDetails.this, DorActivity.class);
            i.putExtra("uid", id);
        startActivity(i);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent i=new Intent(this, MediaChooserActivity.class);
            i.putExtra("idOrNewOrCampaignQ", idOrNewOrCampaignQ);
            startActivity(i);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}