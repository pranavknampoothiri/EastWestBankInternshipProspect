package com.example.pranav.sharkfeed;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;

import com.example.pranav.sharkfeed.R;


public class OpenActivity extends AppCompatActivity {

    private static final int KEEP_TIME = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_open);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(OpenActivity.this, SharkGalleryActivity.class);
                startActivity(intent);
                finish();
            }
        }, KEEP_TIME);
    }


}

