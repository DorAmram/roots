package com.example.user.biographics_tests;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.widget.ViewSwitcher;

import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

//android:background="#fcf9c3"



public class DorActivity extends AppCompatActivity implements GestureDetector.OnGestureListener{

    public final static int PICK_PHOTO_CODE = 1046;
    public static int SIZE = 100;
    private GestureDetectorCompat gestureDetector;
    ArrayList<BallView> balls = new ArrayList<>();
    ArrayList<TextView> headers = new ArrayList<>();
    ArrayList<LineView> lines = new ArrayList<>();
    int selectedCounter = 0;
    Boolean setExport = false;
    Point p;

    public void toastPrint(String str){
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(this, str, duration);
        toast.show();
    }

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dor);

        final ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                Intent inputIntent = getIntent();
                int lastCreatedBallId = inputIntent.getIntExtra("uid", -1);
                for (BallView ball : balls)
                {
                    if (ball.getId() == lastCreatedBallId)
                    {
                        scrollView.scrollTo(0, ball.getTop());
                        break;
                    }
                }
            }
        });

        SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);

                for (BallView ball : balls)
                {
                    //TODO: add handling for year
                    if (ball.getHeader().equals(query))
                    {
                        scrollView.smoothScrollTo(0, ball.getTop());
                        break;
                    }
                }
                ((SearchView) findViewById(R.id.searchView)).setQuery("", false);
                ((SearchView) findViewById(R.id.searchView)).clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        };

        ((SearchView) findViewById(R.id.searchView)).setOnQueryTextListener(onQueryTextListener);

        this.gestureDetector = new GestureDetectorCompat(this, this);
        DBHandler db = new DBHandler(getApplicationContext());
        db.initUnqId(this);
        final LineRelativeLayout layout = (LineRelativeLayout) findViewById(R.id.layout);
        ArrayList<Story> stories = null;
        boolean evenStoryIdx = false;
        boolean initiation = true;
        BallView lastViewCreated = null;

        try {
            stories = db.getOrderedStories();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        BallView.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BallView view = (BallView) v;
                if (!view.isBallSelected()) {
                    //Change Story to relevant class.
                    Intent intent = new Intent(DorActivity.this, ShowStory.class);
                    intent.putExtra("idOrNewOrCampaignQ", view.getUniqueId());
                    startActivity(intent);
                } else {
                    view.deselect();
                if(selectedCounter > 0) {
                    selectedCounter--;
                    if (selectedCounter == 0)
                    {
                        ((ViewSwitcher) findViewById(R.id.viewSwitcher)).showPrevious();
                        setExport = false;
                    }
                    //TODO: Back to normal.
                }
                }
            }
        };

        BallView.OnLongClickListener longClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                BallView view = (BallView) v;
                if (view.isBallSelected())
                {
                    return false;
                } else {
                    view.select();
                    selectedCounter++;
                    //TODO: Selection mode.
                    if (selectedCounter > 0 & !setExport)
                    {
                        ((ViewSwitcher) findViewById(R.id.viewSwitcher)).showNext();
                        setExport = true;
                    }
                }
                return true;
            }
        };

        Button btn_show = (Button) findViewById(R.id.menuButton);
        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                //Open popup window
                if (p != null)
                    showPopup(DorActivity.this, p);
            }
        });

        //Creating and placing story objects on timeline
        for (Story story:stories) {

            //TODO: remove these lines.
            Log.i("eliLog", "STORY ID: " + story.getUniqueId() + ". year: " + story.getDate().get(Calendar.YEAR)
                    + ". photo: " + story.photosSize() + ". photothumb: " + story.dbGetPhotosThumb().size()
                    + ". video: " + story.videosSize() + ". videothumb: " + story.dbGetVideosThumb().size() +  ". audio: " + story.audiosSize() );
            Log.i("eliLog", "DATE: day:" + story.getDate().get(Calendar.DAY_OF_MONTH) + " month:" + story.getDate().get(Calendar.MONTH) + " year:" + story.getDate().get(Calendar.YEAR));

            //Setting ballSize
            int pixelBallSize = new Random().nextInt(320 - 200 + 1) + 200;
            final float scale = getResources().getDisplayMetrics().density;
            int dpBallSize = (int) ((pixelBallSize) - 0.5f / scale);

            //Video's boolean value
            Boolean includeVideos = false;
            if (story.videosSize() > 0) {
                includeVideos = true;
            }

            //Setting and storing the ball object
            BallView tmpView;
            if (story.audiosSize() > 0 && (story.videosSize() > 0 || story.photosSize() > 0)) {
                tmpView = new DoubleCirc(this, Color.RED, includeVideos, dpBallSize, story.getName(), story.getUniqueId(), story.getDate(), story.getDefaultTimeLinePhoto(), new View(this));
            } else {
                tmpView = new DorView(this, Color.RED, includeVideos, dpBallSize, story.getName(), story.getUniqueId(), story.getDate(), story.getDefaultTimeLinePhoto(), new View(this));
            }
            tmpView.setId(View.generateViewId());
            tmpView.setOnClickListener(clickListener);
            tmpView.setOnLongClickListener(longClickListener);
            balls.add(tmpView);

            //Setting and storing the ball's header
            TextView header = new TextView(this);
            header.setId(View.generateViewId());
            header.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
            header.setTextColor(Color.BLACK);
            header.setGravity(Gravity.CENTER);
            String tmpString = tmpView.getHeader();
            SpannableString spanString = new SpannableString(tmpString);
            spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
            header.setText(spanString);
            headers.add(header);

            RelativeLayout.LayoutParams tmpLayoutParams = new RelativeLayout.LayoutParams(dpBallSize, dpBallSize);

            //Placing balls on activity
            if (initiation) {
                tmpLayoutParams.addRule(RelativeLayout.RIGHT_OF, R.id.dummy);
                tmpLayoutParams.setMargins(0, 70, 0, 0);
                initiation = false;
            } else {
                tmpLayoutParams.addRule(RelativeLayout.BELOW, lastViewCreated.getId());
                if (evenStoryIdx) {
                    tmpLayoutParams.addRule(RelativeLayout.LEFT_OF, R.id.dummy);
                } else {
                    tmpLayoutParams.addRule(RelativeLayout.RIGHT_OF, R.id.dummy);
                }
                if(lastViewCreated.getYear() != tmpView.getYear()) {
                    tmpLayoutParams.setMargins(0, 50, 0, 0);
                }

                float startX = (lastViewCreated.getLeft() + lastViewCreated.getRight()) / 2;
                float startY = (lastViewCreated.getTop() + lastViewCreated.getBottom()) / 2;
                float endX =   (tmpView.getLeft() + tmpView.getRight()) / 2;
                float endY =   (tmpView.getTop() + tmpView.getBottom()) / 2;
                LineView line = new LineView(this, startX, startY, endX, endY);
                lines.add(line);
            }

            //TODO: play with indentation
            //layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
            //Random indent
            //layoutParams.setMargins()

            layout.addView(tmpView, story.getId() - 1, tmpLayoutParams);
            lastViewCreated = tmpView;
            evenStoryIdx = !evenStoryIdx;
        }

        //Adding the year of the first story element on time line
        BallView initialStoryView = balls.get(0);
        TextView yearTextView = new TextView(this);
        yearTextView.setId(View.generateViewId());
        yearTextView.setText("- " + String.valueOf(initialStoryView.getYear()) + " -");
        yearTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 42);
        yearTextView.setBackgroundColor(Color.TRANSPARENT);
        yearTextView.setTextColor(Color.parseColor("#ffe45e"));
        RelativeLayout.LayoutParams initialHeaderLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        initialHeaderLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        initialHeaderLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, initialStoryView.getId());
        layout.addView(yearTextView, initialHeaderLayoutParams);

        //Adding years of additional story elements on timeline
        for (int i = 0 ; i < stories.size() - 1 ; i++) {
            BallView firstView = balls.get(i);
            BallView secondView = balls.get(i + 1);
            if(firstView.getYear() != secondView.getYear()) {
                yearTextView = new TextView(this);
                yearTextView.setId(View.generateViewId());
                yearTextView.setText("- " + String.valueOf(secondView.getYear()) + " -");
                yearTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 42);
                yearTextView.setBackgroundColor(Color.TRANSPARENT);
                yearTextView.setTextColor(Color.parseColor("#ffe45e"));
                RelativeLayout.LayoutParams tmpHeaderLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                tmpHeaderLayoutParams.addRule(RelativeLayout.BELOW, firstView.getId());
                tmpHeaderLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, secondView.getId());
                layout.addView(yearTextView, tmpHeaderLayoutParams);
            }
        }

        //Adding header for each story element on timeline
        for(int i = 0 ; i < balls.size() ; i++) {
            BallView currView = balls.get(i);
            TextView header = headers.get(i);
            RelativeLayout.LayoutParams tmpHeaderLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            tmpHeaderLayoutParams.addRule(RelativeLayout.BELOW, currView.getId());
            tmpHeaderLayoutParams.addRule(RelativeLayout.ALIGN_LEFT, currView.getId());
            layout.addView(header, tmpHeaderLayoutParams);
        }

        //Adding lines between story elements on timeline
        ViewTreeObserver viewTreeObserver = layout.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                Log.d("debug", "doing it again");
                layout.clearLines();

                for (int i = 0; i < balls.size() - 1; i++) {
                    BallView lastViewCreated = balls.get(i);
                    BallView tmpView = balls.get(i + 1);
                    float startX = (lastViewCreated.getLeft() + lastViewCreated.getRight()) / 2;
                    float startY = (lastViewCreated.getTop() + lastViewCreated.getBottom()) / 2;
                    float endX = (tmpView.getLeft() + tmpView.getRight()) / 2;
                    float endY = (tmpView.getTop() + tmpView.getBottom()) / 2;
                    layout.addLine((int) startX, (int) startY, (int) endX, (int) endY);
                }

                //Adding header for each story element on timeline
                for(int i = 0 ; i < balls.size() ; i++) {
                     BallView lastViewCreated = balls.get(i);
                     headers.get(i).getLayoutParams().width = lastViewCreated.getMeasuredWidth();
                }
            }
        });
    }

    public void createNewContent(View view){
        Intent i = new Intent(this, MediaChooserActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dor, menu);
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

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public void viewOnClick(View v)
    {
//        Toast.makeText(this, ((DorView) v).showHiddenMessage(), Toast.LENGTH_SHORT);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent i = new Intent(this, MediaChooserActivity.class);
            startActivity(i);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {}

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {}

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//        toast = Toast.makeText(this, "fling", duration);
//        toast.show();
        return false;
    }

    public void exportOnClick(View v)
    {
        ArrayList<Integer> selectedIds = new ArrayList<>();
        for (BallView ball : balls)
        {
            if (ball.isBallSelected()) {
                selectedIds.add(ball.getUniqueId());
                ball.deselect();
            }
        }
        selectedCounter = 0;
        ((ViewSwitcher) findViewById(R.id.viewSwitcher)).showPrevious();
        setExport = false;
        //TODO: Handle
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        int[] location = new int[2];
        Button button = (Button) findViewById(R.id.menuButton);

        // Get the x, y location and store it in the location[] array
        // location[0] = x, location[1] = y.
        button.getLocationOnScreen(location);

        //Initialize the Point with x, and y positions
        p = new Point();
        p.x = location[0];
        p.y = location[1];
    }

    // The method that displays the popup.
    private void showPopup(final Activity context, Point p) {

        final float scale = getResources().getDisplayMetrics().density;

        int pixelPopupWidth = 150;
        int pixelPopupHeight = 450;
        int dpPopupWidth = (int) ((pixelPopupWidth) - 0.5f / scale);
        int dpPopupHeight = (int) ((pixelPopupHeight) - 0.5f / scale);

        // Inflate the popup_layout.xml
        LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupLayout = layoutInflater.inflate(R.layout.popup_layout, viewGroup);

        // Creating the PopupWindow
        final PopupWindow popup = new PopupWindow(context);
        popup.setContentView(popupLayout);
        popup.setWidth(dpPopupWidth);
        popup.setHeight(dpPopupHeight);
        popup.setFocusable(true);

        // Some offset to align the popup a bit to the right, and a bit down, relative to button's position.
        int PIXEL_OFFSET_X = -15;
        int PIXEL_OFFSET_Y = -320;
        int DP_OFFSET_X = (int) ((PIXEL_OFFSET_X) - 0.5f / scale);
        int DP_OFFSET_Y = (int) ((PIXEL_OFFSET_Y) - 0.5f / scale);

        // Clear the default translucent background
        popup.setBackgroundDrawable(new BitmapDrawable());

        // Displaying the popup at the specified location, + offsets.
        popup.showAtLocation(popupLayout, Gravity.NO_GRAVITY, p.x + DP_OFFSET_X, p.y + DP_OFFSET_Y);

        // Getting a reference to Close button, and close the popup when clicked.
        Button close = (Button) popupLayout.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });
    }

    public void MoveToCampaign(View view) {
        Intent i = new Intent(DorActivity.this, Campaign.class);
        i.putExtra("idOrNewOrCampaignQ", 2);
        startActivity(i);
    }

    public void onSearch(){}
}
