package com.example.user.biographics_tests;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by elithe on 9/29/2015.
 */

/**
 *  HOW TO INITIATE DB???
 *  DBHandler db = new DBHandler(getApplicationContext());
 *      ADDDD IN ALL CODES!!!  initUnqId(Context context)
 *  db.insertStory(Story story);
 *  db.close()
 *   ArrayList<Story> storyrlst = db.getOrderedStories();
 */

public class DBHandler extends SQLiteOpenHelper {
    //SQLiteDatabase mdb;
    int unqId;
    SharedPreferences sharedP;
    // Database Version
    private static final int DATABASE_VERSION = 3;

    // Database Name
    private static final String DATABASE_NAME = "biodb";

    // fields for main table. the table where all stories are located.
    private static String MAIN_TABLE_NAME =  "main_table";
    private static String KEY_ID = "id";
    private static String KEY_NAME = "name";
    private static String KEY_DATE = "date";
    private static String KEY_LOCATION = "location";

    //fields for content table. the table where the videos, audios, photos
    // of all the stories are located.
    private static String CONTENTS_TABLE_NAME = "contents";
    private static String KEY_CONTENT_ID = "id";
    private static String KEY_PATH = "path";

    //fields for thumbs table. the table where the videoThums, photosThumbs
    // of all the stories are located.
    private static String THUMBS_TABLE_NAME = "thumbs";
    private static String KEY_THUMBS_ID = "id";
    private static String KEY_THUMBS_PATH = "path";

    private static final String CREATE_TABLE_MAIN = "CREATE TABLE "
            + MAIN_TABLE_NAME + "(" + KEY_ID + " INTEGER," + KEY_NAME
            + " TEXT," + KEY_DATE + " TEXT, " + KEY_LOCATION + " TEXT" + ")";

    private static final String CREATE_TABLE_CONTENTS = "CREATE TABLE "
            + CONTENTS_TABLE_NAME + "(" + KEY_CONTENT_ID + " TEXT," + KEY_PATH
            + " TEXT" + ")";

    private static final String CREATE_TABLE_THUMBS = "CREATE TABLE "
            + THUMBS_TABLE_NAME + "(" + KEY_THUMBS_ID + " TEXT," + KEY_THUMBS_PATH
            + " TEXT" + ")";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create both tables
        db.execSQL(CREATE_TABLE_MAIN);
        db.execSQL(CREATE_TABLE_CONTENTS);
        db.execSQL(CREATE_TABLE_THUMBS);
        db.execSQL(CREATE_TABLE_CAMPAIGN);

    }
    //we init or load the unqId count!
    public void initUnqId(Context context)
    {
        sharedP = context.getSharedPreferences("userPrvChoice", Context.MODE_PRIVATE);
        int UID = sharedP.getInt("UID", -1);
        //if there isnt any ID- it is the first time ever we run the app. init to 0;
        if(UID == -1)
        {
            unqId = 0;
        }
        else {
            unqId = UID;
        }
    }

    //used in order gto save cur unq id on db.
    private void saveUnqId(int IdToSave)
    {
        SharedPreferences.Editor editor = sharedP.edit();
        editor.putInt("UID", IdToSave);
        editor.commit();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + MAIN_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CONTENTS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + THUMBS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CAMPAIGN_TABLE_NAME);


        // reset the story ids counter
        unqId = 0;
        saveUnqId(unqId);
        // create new tables
        onCreate(db);
    }

    public int insertStory(Story story)
    {
        unqId++;
        saveUnqId(unqId);
        SQLiteDatabase db = this.getWritableDatabase();
        //insert some properties to the main table: story name, date of story happening
        ContentValues toMainTbl = new ContentValues();
        toMainTbl.put(KEY_ID, unqId);
        toMainTbl.put(KEY_NAME, story.getName());
        String date = formatDate(story.getDate());
        toMainTbl.put(KEY_DATE, date);
        toMainTbl.put(KEY_LOCATION, story.getLocation());
        Long ret = db.insertOrThrow(MAIN_TABLE_NAME, null, toMainTbl);

        // insert the new content to contents tbl.
       //ContentValues toContentsTbl = new ContentValues();
        insertContentLists(db, story, unqId);
        return unqId;
    }

    /**
     *
     * @param uid id
     * @param type Must be on of the following: photo, audio, video
     * @param mediaPath path of media file
     * @param tumbPath path of the tumbnail if exist, else null
     * @return uid
     */
    public int insertMediaToExistStory( int uid, String type, String mediaPath, String tumbPath )
    {
        SQLiteDatabase db = this.getWritableDatabase();
        // if the type is no as specified:
        if(type != "audio" && type != "video" && type != "photo")
        {
            Log.e("elilog", "Trying to to insert content with uncompetible type");
            return -1;
        }
        ContentValues content = new ContentValues();
        content.put(KEY_CONTENT_ID,  String.format(uid + type));
        content.put(KEY_PATH, mediaPath);
        db.insert(CONTENTS_TABLE_NAME, null, content);
        content.clear();

        // now add tumbnail if exists.
        if(tumbPath != null) {
            content.put(KEY_THUMBS_ID,  String.format(uid + type));
            content.put(KEY_THUMBS_PATH, tumbPath);
            db.insert(THUMBS_TABLE_NAME, null, content);
            content.clear();
        }
        return uid;
    }

    // here we make a String in a format we can reformat to Gdate afterwords.
    private String formatDate(GregorianCalendar storyDate) {
        String month = String.valueOf(storyDate.get(Calendar.MONTH) + 1);
        //if month is one character. add 0 before it.
        if(month.length() == 1)
        {
            month = "0" + month;
        }
        String day = String.valueOf(storyDate.get(Calendar.DAY_OF_MONTH));
        //if day is one character. add 0 before it.
        if(day.length() == 1)
        {
            day = "0" + day;
        }
        String result =  day + " " + month + " " + storyDate.get(Calendar.YEAR);
        return result;
    }

    /*
        iterates over all the story content lists and inserts its contents to the db
     */
    private void insertContentLists(SQLiteDatabase db, Story story, int unqId) {
        //check if list is empty. if not, insert its content.
        if(story.photosSize() != 0)
        {
            addSpecificContentType(db, story.dbGetPhotos(), String.format(unqId + "photo"));
            //photo thumbnails should be same size as photos.
            addThumbs(db, story.dbGetPhotosThumb(), String.format(unqId + "photo"));
        }
        if(story.audiosSize() != 0)
        {
            addSpecificContentType(db, story.dbGetAudios(), String.format(unqId + "audio"));
        }
        if(story.videosSize() != 0)
        {
            addSpecificContentType(db, story.dbGetVideos(), String.format(unqId + "video"));
            addThumbs(db, story.dbGetVideosThumb(), String.format(unqId + "video"));
        }
    }
    // loop over the thumbs list and insert them all to the table.
    private void addThumbs(SQLiteDatabase db, ArrayList<String> list, String id) {
        ContentValues nextContent = new ContentValues();
        for(String singleContentPath : list)
        {
            nextContent.put(KEY_THUMBS_ID, id);
            nextContent.put(KEY_THUMBS_PATH, singleContentPath);
            db.insert(THUMBS_TABLE_NAME, null, nextContent);
            nextContent.clear();
        }
    }

    /*
        iterate over current list and add all items to the db
     */
    private void addSpecificContentType(SQLiteDatabase db, ArrayList<String> list, String id) {
        ContentValues nextContent = new ContentValues();
        for(String singleContentPath : list)
        {
            nextContent.put(KEY_CONTENT_ID, id);
            nextContent.put(KEY_PATH, singleContentPath);
            db.insert(CONTENTS_TABLE_NAME, null, nextContent);
            nextContent.clear();
        }
    }
    /*
        returns am arraylist with all the stories with their data.
     */
    public ArrayList<Story> getOrderedStories() throws ParseException {
        ArrayList<Story> result = new ArrayList<Story>();
        SQLiteDatabase db = this.getReadableDatabase();
        //first get all the entries from main table: each row is a story.
        String selectAllStories = "SELECT  * FROM " + MAIN_TABLE_NAME;
        Cursor crs = db.rawQuery(selectAllStories, null);
        if(crs.getCount() == 0)
        {
            return result;
        }
        //traverse through each line( each story entry)
        int uid;
        String name;
        String datestr;
        String location;
        crs.moveToFirst();
        for(int i = 0; i <  crs.getCount(); i++)
        {
            uid = crs.getInt(0);
            name = crs.getString(1);
            datestr = crs.getString(2);
            location = crs.getString(3);
            DateFormat df = new SimpleDateFormat("dd MM yyyy");
            Date d = df.parse(datestr);
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(d);
            //generate new story obj
            Story newStory = new Story(name, cal);
            //add contents to the story
            addContents(newStory, db, uid);
            newStory.setUniqueId(uid);
            newStory.setLocation(location);
            crs.moveToNext();
            result.add(newStory);
        }
        Collections.sort(result);
        //set indexes to all objects.
        for(int i = 0; i < result.size(); i++)
        {
            result.get(i).setId(i+1);
        }
        return result;
    }

    public Story getStoryById(int uid) throws ParseException {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectStoryWithId = "SELECT  * FROM " + MAIN_TABLE_NAME + " WHERE id = " + uid;
        Cursor crs = db.rawQuery(selectStoryWithId, null);
        crs.moveToFirst();
        String name = crs.getString(1);
        String datestr = crs.getString(2);

        DateFormat df = new SimpleDateFormat("dd MM yyyy");
        Date d = df.parse(datestr);
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(d);
        cal.add(Calendar.MONTH,1);
        //generate new story obj
        Story newStory = new Story(name, cal);
        newStory.setUniqueId(uid);
        //add contents to the story
        addContents(newStory, db, uid);
        return newStory;
    }


    private void addContents(Story story, SQLiteDatabase db, int id) {
        //sql line for selecting all photos
        String sqLineSelectAllPhotos = "SELECT  * FROM " + CONTENTS_TABLE_NAME + " WHERE " + KEY_CONTENT_ID + "=" + "'" + String.format(id + "photo") + "'";
        //sql line for selecting all videos
        String sqLineSelectAllVideos = "SELECT  * FROM " + CONTENTS_TABLE_NAME + " WHERE " + KEY_CONTENT_ID + "=" + "'" + String.format(id + "video") + "'";
        //sql line for selecting all audios
        String sqLineSelectAllAudio = "SELECT  * FROM " + CONTENTS_TABLE_NAME + " WHERE " + KEY_CONTENT_ID + "=" + "'" + String.format(id + "audio") + "'";

        //sql line for selecting all photo THUMBS
        String sqLineSelectAllPhotoThumbs = "SELECT  * FROM " + THUMBS_TABLE_NAME + " WHERE " + KEY_THUMBS_ID + "=" + "'" + String.format(id + "photo") + "'";
        String sqLineSelectAllVideoThumbs = "SELECT  * FROM " + THUMBS_TABLE_NAME + " WHERE " + KEY_THUMBS_ID + "=" + "'" + String.format(id + "video") + "'";

        //add to all three lists. photos. videos, audio.
        addToList(story.dbGetPhotos(), sqLineSelectAllPhotos, db);
        addToList(story.dbGetVideos(), sqLineSelectAllVideos , db);
        addToList(story.dbGetAudios(), sqLineSelectAllAudio, db);

        //adding the thumbs
        addToList(story.dbGetPhotosThumb(), sqLineSelectAllPhotoThumbs, db);
        addToList(story.dbGetVideosThumb(), sqLineSelectAllVideoThumbs, db);

    }
    private void addToList(ArrayList<String> copyTo, String sqLine, SQLiteDatabase db) {
        Cursor crs = db.rawQuery(sqLine, null);
        if(crs.getCount() != 0)
        {
            crs.moveToFirst();
            //traverse the cursor and inject each path to the copyTo list of the object.
            //copies from the selected table and copies to the given list.
            for(int i = 0; i <  crs.getCount(); i++)
            {
                copyTo.add(crs.getString(1));
                crs.moveToNext();
            }
        }
    }

    /*
    CAMPAIGN RELATED STUFF
     */
    // fields for main table. the table where all stories are located.
    private static String CAMPAIGN_TABLE_NAME =  "questions";
    private static String KEY_CAMP_QUESTION = "question";
    private static String KEY_CAMP_ANSWERSTATE = "answerstate";

    private static final String CREATE_TABLE_CAMPAIGN = "CREATE TABLE "
            + CAMPAIGN_TABLE_NAME + "(" + KEY_CAMP_QUESTION + " STRING," + KEY_CAMP_ANSWERSTATE
            + " INTEGER"+ ")";

    private SQLiteDatabase qDb;
    private void addNewQuestionToDb(String newQuestion, SQLiteDatabase db)
    {
        ContentValues cv = new ContentValues();
        cv.put(KEY_CAMP_QUESTION, newQuestion);
        cv.put(KEY_CAMP_ANSWERSTATE, 0);
        db.insert(CAMPAIGN_TABLE_NAME, null, cv);
    }

    public void updateQuestionDb(List<String> newQuestions )
    {
        qDb = this.getWritableDatabase();
        ArrayList<Question> questionsInDb =  getQuestions();
        //if there are no questions in db
        if(questionsInDb.size() == 0)
        {
            for(String newQ : newQuestions)
            addNewQuestionToDb(newQ, qDb);
            return;
        }

        //for each question in the new list question, check if it is already in the db.
        for(String possiblyNew : newQuestions )
        {
            for(int i = 0; i < questionsInDb.size(); i++)
            {
                //if it is the last question in the db and is not the same question, we will have to add it.
                if(i == questionsInDb.size() - 1 && !questionsInDb.get(i).compareTo(possiblyNew))
                {
                    addNewQuestionToDb(possiblyNew, qDb);
                }
                if(questionsInDb.get(i).compareTo(possiblyNew))
                {
                    break;
                }
            }
        }
    }

    public int numberOfQuestionInDb()
    {
        String selectAllQuestions = "SELECT  * FROM " + CAMPAIGN_TABLE_NAME;
        Cursor crs = qDb.rawQuery(selectAllQuestions, null);
        return crs.getCount();
    }

    public int bumOfAnswerdQuestions()
    {
        String selectNotAnsweredQuestions = "SELECT  * FROM " + CAMPAIGN_TABLE_NAME + " WHERE answerstate = " + 1;
        Cursor crs = qDb.rawQuery(selectNotAnsweredQuestions, null);
        return crs.getCount();
    }

    public ArrayList<Question> getQuestions()
    {
        String selectAllQuestions = "SELECT  * FROM " + CAMPAIGN_TABLE_NAME;
        Cursor crs = qDb.rawQuery(selectAllQuestions, null);
        return addQuestionsFromCursor(crs);
    }

    public ArrayList<Question> getNotAnsweredQuestions()
    {
        String selectNotAnsweredQuestions = "SELECT  * FROM " + CAMPAIGN_TABLE_NAME + " WHERE answerstate = " + 0;
        Cursor crs = qDb.rawQuery(selectNotAnsweredQuestions, null);
        return addQuestionsFromCursor(crs);
    }

    public void markQuestionAsAnswered(String question)
    {
        ContentValues cv = new ContentValues();
        cv.put("answerstate", 1);
        int rowsUpdated = qDb.update(CAMPAIGN_TABLE_NAME, cv ,"question = " +  "'" + question + "'", null  );
        if(rowsUpdated != 1)
        {
            Log.e("eliLog", "should update exactly one row but updated: " + rowsUpdated);
        }
    }

    private ArrayList<Question> addQuestionsFromCursor(Cursor crs)
    {
        ArrayList<Question> questions = new ArrayList<Question>();
        if(crs.getCount() != 0)
        {
            crs.moveToFirst();
            for(int i = 0; i <  crs.getCount(); i++)
            {
                questions.add(new Question(crs.getString(0), crs.getInt(1)));
                crs.moveToNext();
            }
        }
        return questions;
    }
}
