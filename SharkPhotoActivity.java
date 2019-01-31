package com.example.pranav.sharkfeed;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.pranav.sharkfeed.R;
import com.example.pranav.sharkfeed.SharkPhotoFragment;

// This activity actually presents the full picture.
public class SharkPhotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sharkactivity_photo);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = new SharkPhotoFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }
}
