package com.example.user.biographics_tests;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

public class OptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.x = -20;
        params.height = 100;
        params.width = 550;
        params.y = -10;

        //TODO: add features of story based on id
        Intent intent = getIntent();
        String value = intent.getStringExtra("id");

        this.getWindow().setAttributes(params);
    }





}
