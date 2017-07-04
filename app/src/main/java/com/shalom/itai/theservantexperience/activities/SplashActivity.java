package com.shalom.itai.theservantexperience.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.shalom.itai.theservantexperience.R;
import com.shalom.itai.theservantexperience.gallery.GalleryActivity;

import static com.shalom.itai.theservantexperience.utils.Constants.JonIntents.INPUT_TO_SPLASH_CLASS_NAME;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Intent intent = getIntent();
        final String className = intent.getStringExtra(INPUT_TO_SPLASH_CLASS_NAME);

        final Thread welcomeThread = new Thread() {

            @Override
            public void run() {
                try {
                    super.run();
                    sleep(500);  //Delay of 10 seconds
                } catch (Exception e) {
                    final String TAG = "SplashActivity";
                    Log.d(TAG, "SplashActivity: Error!");
                } finally {
                    Intent i = null;
                    if (className.equals("GalleryActivity")) {
                         i = new Intent(SplashActivity.this,
                                GalleryActivity.class);
                    }else if(className.equals("MainActivity"))
                         i = new Intent(SplashActivity.this,
                                MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        };
        welcomeThread.start();
    }
}