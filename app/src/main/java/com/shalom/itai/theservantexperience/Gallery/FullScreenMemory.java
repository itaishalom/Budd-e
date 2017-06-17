package com.shalom.itai.theservantexperience.Gallery;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.shalom.itai.theservantexperience.activities.MainActivity;
import com.shalom.itai.theservantexperience.activities.SplashActivity;
import com.shalom.itai.theservantexperience.R;
import com.shalom.itai.theservantexperience.services.BuggerService;

import java.io.File;

import static android.os.Environment.DIRECTORY_PICTURES;
import static com.shalom.itai.theservantexperience.Utils.Functions.copy;

public class FullScreenMemory extends AppCompatActivity {
    private BottomNavigationView navigation;
    private boolean mVisible;
    private double deleteProb ;
    private LinearLayout container;
    private String mImagePath;
    public static final String EXTRA_SPACE_PHOTO = "SpacePhotoActivity.SPACE_PHOTO";
    public static final String CALLING_ACTIVITY = "Caller";
    private String caller;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if(caller.equals("GalleryActivity"))
                        GalleryActivity.closeOnReturn = true;
                    Intent startMainActivity = new Intent(FullScreenMemory.this, MainActivity.class);
                    startActivity(startMainActivity);
                    overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);

                    finish();
                    //      mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_delete:
                    delete();
                    //      mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_share:
                    //       mTextMessage.setText(R.string.share_option);
                    share();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mVisible  = true;

        setContentView(R.layout.activity_full_screen_memory);
        container = (LinearLayout) findViewById(R.id.container);
        container.setBackgroundColor(Color.parseColor("#000000"));
        ImageView imageView = (ImageView) findViewById(R.id.image);

        MemoryPhoto memoryPhoto = getIntent().getParcelableExtra(EXTRA_SPACE_PHOTO);
        caller = getIntent().getStringExtra(CALLING_ACTIVITY);
        mImagePath = memoryPhoto.getUrl();
        Bitmap bmp = BitmapFactory.decodeFile(mImagePath);
        imageView.setImageBitmap(bmp);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        deleteProb = -1;

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }


    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        navigation.setVisibility(View.INVISIBLE);
    //    mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
     //   mHideHandler.removeCallbacks(mShowPart2Runnable);
  //      mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {

        // Show the system bar

  //      mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
  //              | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        mVisible = true;
        navigation.setVisibility(View.VISIBLE);
        // Schedule a runnable to display UI elements after a delay

    //    mHideHandler.removeCallbacks(mHidePart2Runnable);
     //   mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);

    }


    private void delete() {
        if (deleteProb == -1 ){
            deleteProb = BuggerService.getInstance().shouldIDoThis();
        }
        if (deleteProb >= 0.5) {
            File file = new File(mImagePath);
            if (file.delete()) {
                Toast.makeText(this.getApplicationContext(), "Forgot it :(", Toast.LENGTH_SHORT).show();
                //  Snackbar.make(container, "Forgot it :(", Snackbar.LENGTH_SHORT).show();
                startActivity(new Intent(this, SplashActivity.class));
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                finish();
            } else {
                Snackbar.make(container, "I failed to forget it", Snackbar.LENGTH_SHORT).show();
            }

        } else {
            Snackbar.make(container, "Hahaha!! You can't delete my memories", Snackbar.LENGTH_SHORT).show();
        }
    }
    private void share(){
        if (copy(new File(mImagePath),new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES)+ File.separator+"temporary_file.jpeg"))) {
            Uri uriToImage = Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES)+ File.separator+"temporary_file.jpeg"));
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, uriToImage);
            shareIntent.setType("image/jpeg");
            shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check me and Jon!");
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Check me and Jon!");
            //      setShareIntent(shareIntent);
            BuggerService.getInstance().unbug();
            Intent result = Intent.createChooser(shareIntent, getResources().getText(R.string.send_to));
            startActivity(result);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    BuggerService.getInstance().bug();
                }
            }, 60 * 1000);
            //   finish();
        }
    }
}
