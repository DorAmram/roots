package com.example.user.biographics_tests;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.biographics_tests.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Campaign extends ActionBarActivity {

    private final int numberOfStars = 5;
    TextView questionDisplay;
    RatingBar ratingBar;
    private Random randomGenerator;
    ArrayList<Question> notAnsweredQuestions;
    String questionOnScreenNow;
    DBHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_activity_campaign);

        questionDisplay = (TextView) findViewById(R.id.qDisp);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        List<String> campaignQuestions = Arrays.asList(getResources().getStringArray(R.array.campaignQuestions));
        db = new DBHandler(getApplicationContext());
        db.initUnqId(this);
        db.updateQuestionDb(campaignQuestions);
        notAnsweredQuestions = db.getNotAnsweredQuestions();
        ratingBar.setIsIndicator(true);
        ratingBar.setNumStars(5);
        ratingBar.setStepSize((float) 0.1);
        float AnswerdPart = ( (1-((float) notAnsweredQuestions.size() / (float)db.numberOfQuestionInDb())) * numberOfStars);
        ratingBar.setRating( (float)AnswerdPart);

        Log.i("campLog", "notAnsweredQuestions size at begining: " + notAnsweredQuestions.size());
        Log.i("campLog", "setrating: " + AnswerdPart);
        generateQuestion();
    }
    public void changeQuestion(View v)
    {
        generateQuestion();
    }
    public void takeQuestion(View v)
    {
        db.markQuestionAsAnswered(questionOnScreenNow);
        Toast.makeText(getApplicationContext(),questionOnScreenNow, Toast.LENGTH_LONG).show();
        Intent MediaChooserActivity = new Intent(this, MediaChooserActivity.class);
        MediaChooserActivity.putExtra("idOrNewOrCampaignQ", questionOnScreenNow);
        startActivity(MediaChooserActivity);
        Log.i("campLog", "db.numberOfQuestionInDb(): " + db.numberOfQuestionInDb() + " notAnsweredQuestions.size(): " + db.getNotAnsweredQuestions().size());
        db.close();
    }
    private void generateQuestion() {
        double randIdx = Math.random() * notAnsweredQuestions.size();
        questionOnScreenNow = notAnsweredQuestions.get((int)randIdx).getQuestion();
        questionDisplay.setText(questionOnScreenNow);

    }
    float x1,x2;
    // onTouchEvent () method gets called when User performs any touch event on screen
    // Method to handle touch event like left to right swap and right to left swap

    public boolean onTouchEvent(MotionEvent touchevent)
    {
        switch (touchevent.getAction())
        {
            // when user first touches the screen we get x and y coordinate
            case MotionEvent.ACTION_DOWN:
            {
                x1 = touchevent.getX();
                break;
            }
            case MotionEvent.ACTION_UP:
            {
                x2 = touchevent.getX();

                //if left to right sweep event on screen
                if (x1 < x2)
                {
                    db.markQuestionAsAnswered(questionOnScreenNow);
                    Toast.makeText(getApplicationContext(),questionOnScreenNow, Toast.LENGTH_LONG).show();
                    Intent i = new Intent(this, MediaChooserActivity.class);
                    i.putExtra("idOrNewOrCampaignQ", questionOnScreenNow);
                    startActivity(i);
                    Log.i("campLog", "db.numberOfQuestionInDb(): " + db.numberOfQuestionInDb() + " notAnsweredQuestions.size(): " + db.getNotAnsweredQuestions().size());
                    db.close();
                }

                // if right to left sweep event on screen
                if (x1 > x2)
                {
                    generateQuestion();
                }
            }
        }
        return false;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_campaign, menu);
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
