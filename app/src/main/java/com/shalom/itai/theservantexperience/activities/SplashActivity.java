package com.shalom.itai.theservantexperience.activities;

import com.shalom.itai.theservantexperience.gallery.GalleryActivity;
import com.shalom.itai.theservantexperience.R;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);



        Thread welcomeThread = new Thread() {

            @Override
            public void run() {
                try {
                    super.run();
                    sleep(500);  //Delay of 10 seconds
                } catch (Exception e) {
                    final String TAG = "SplashActivity";
                    Log.d(TAG, "SplashActivity: Error!");
                } finally {

                    Intent i = new Intent(SplashActivity.this,
                            GalleryActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        };
        welcomeThread.start();
    }
}