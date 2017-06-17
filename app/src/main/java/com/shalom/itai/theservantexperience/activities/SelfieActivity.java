/*
package com.shalom.itai.theservantexperience.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.shalom.itai.theservantexperience.FaceOverlayView;
import com.shalom.itai.theservantexperience.GifImageView;
import com.shalom.itai.theservantexperience.R;

import static com.shalom.itai.theservantexperience.Utils.Constants.IMAGE_BYTE_ARRAY;
import static com.shalom.itai.theservantexperience.Utils.Constants.SAVE_IMAGE;
import static com.shalom.itai.theservantexperience.Utils.Functions.takeScreenshot;

public class SelfieActivity extends AppCompatActivity {
    private static final String TAG = "SelfieActivity";
    private int TAKE_IMAGE = 1;
    GifImageView gifImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selfie);
        setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar));
        gifImageView = (GifImageView) findViewById(R.id.GifImageView);
        gifImageView.setGifImageResource(R.drawable.jon_blinks);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askForPicure();
            }
        });
    }
    private void askForPicure(){
        Intent i = new Intent(this, PictureActivty.class).putExtra(SAVE_IMAGE,false);
        startActivityForResult(i, TAKE_IMAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode ==TAKE_IMAGE){

            if(resultCode == Activity.RESULT_OK){

                byte[] image = data.getByteArrayExtra(IMAGE_BYTE_ARRAY);
                Bitmap photo = BitmapFactory.decodeByteArray(image, 0, image.length);
                FaceOverlayView mFaceOverlayView  = (FaceOverlayView) findViewById(R.id.face_overlay);
                mFaceOverlayView.setBitmap(photo);
                double val = mFaceOverlayView.getSmilingProb() ;
           //     mFaceOverlayView.setVisibility(View.INVISIBLE);
                if(val==-1){
                    Toast.makeText(SelfieActivity.this ,"Where is your face??", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(SelfieActivity.this ,"Thanks!", Toast.LENGTH_SHORT).show();
                }
           //     ImageView img = (ImageView) findViewById(R.id.selfie_image);
            //    img.setImageBitmap(photo);
            //    img.bringToFront();
              //  mFaceOverlayView.invalidateThis();
                gifImageView.bringToFront();
                takeScreenshot(this,"Our selfie!");

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                askForPicure();
                //Write your code if there's no result
            }
        }
        else{
            Log.d(TAG, "onActivityResult: error! canceled");
        }
    }

}
*/
