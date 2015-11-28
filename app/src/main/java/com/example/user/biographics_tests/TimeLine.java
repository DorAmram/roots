package com.example.user.biographics_tests;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Date;
import java.util.LinkedList;
import java.util.Iterator;

public class TimeLine extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_time_line, menu);
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

    //TODO: how to handle when 2 events are happening at the same time?

    public class Story{
        private String eventName;
        private Date eventStartingTime, eventEndingTime;
        //TODO: create an object of mediaType that will rerieve data from specified folder
        //TODO: finish adding all of event fields

        //TODO: add getters and setters

        //TODO: add media getters from directories

    }

        public static void main(String args[]) {
        // create a linked list
        LinkedList <Integer>stories = new LinkedList();
        // add elements to the linked list
        stories.add(1);
        stories.add(3);
        stories.add(5);


        System.out.println("Original contents of stories: " + stories);
        // remove elements from the linked list
        stories.remove("F");
        stories.remove(2);
        System.out.println("Contents of stories after deletion: " + stories);
        // remove first and last elements
        stories.removeFirst();
        stories.removeLast();
        System.out.println("stories after deleting first and last: " + stories);
        // get and set a value
        Object val = stories.get(2);
        System.out.println("stories after change: " + stories);

        //creating an iterator
        Iterator it = stories.iterator();
        while (it.hasNext())
        {
            String s = (String)it.next();
            System.out.println(s);
        }
    }
}

