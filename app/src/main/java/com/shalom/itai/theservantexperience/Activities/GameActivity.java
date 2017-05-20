/*

package com.shalom.itai.theservantexperience.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.shalom.itai.theservantexperience.FaceOverlayView;
import com.shalom.itai.theservantexperience.R;
import com.shalom.itai.theservantexperience.Services.BuggerService;
import com.shalom.itai.theservantexperience.Utils.Functions;
import com.shalom.itai.theservantexperience.Utils.SilentCamera;
import com.shalom.itai.theservantexperience.Utils.Functions.*;


public class GameActivity extends AppCompatActivity {
    private BroadcastReceiver mReceiver;
    SilentCamera c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        c = new SilentCamera(this);
    //    c.getCameraInstance();
        analyze();
    }



    @Override
    protected void onPause() {
        super.onPause();
        //  BuggerService.isFunActivityUp = false;
        this.unregisterReceiver(this.mReceiver);
    }
    @Override
    protected void onResume() {
        super.onResume();
        // BuggerService.isMainActivityUp = true;
        IntentFilter intentFilter = new IntentFilter(
                "android.intent.action.ImageReady");

        mReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                //extract our message from intent
                byte[] pathToImage = intent.getByteArrayExtra("path");
                continueAnalyze2(pathToImage);
            }
        };
        //registering our receiver
        this.registerReceiver(mReceiver, intentFilter);
    }


    public void analyze() {
       c.getCameraInstance();

        c.takePicture();
        return;
    }

    public void continueAnalyze(final String path){
        Bitmap photo = Functions.getImageBitmap(path);
        FaceOverlayView mFaceOverlayView;
        mFaceOverlayView = (FaceOverlayView) findViewById(R.id.face_overlay);
        //    mFaceOverlayView.setBitmap(photo);

        SparseArray<Face> mFaces =null;
        FaceDetector detector = new FaceDetector.Builder( getApplicationContext() )
                .setTrackingEnabled(true)
                .setMode(FaceDetector.ACCURATE_MODE).setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .build();

        if (!detector.isOperational()) {
            //Handle contingency
        } else {

            Frame frame = new Frame.Builder().setBitmap(photo).build();

            mFaces = detector.detect(frame);
            detector.release();
        }
        if(mFaces.size()==0){
            return;

Toast.makeText(FunActivity.this, "I don't see your face!",
                                    Toast.LENGTH_LONG).show();
                            GlobalPoints -= 2;*//*
*/
/*

        }else {//mFaces.size()
            for (int i = 0; i <1 ; i++) {
                Face face = mFaces.valueAt(i);

                float smilingProbability = face.getIsSmilingProbability();


                if(smilingProbability>0.8) {
                    mFaceOverlayView.setBitmapLight(photo);
                    Toast.makeText(this, "smile!",
                            Toast.LENGTH_LONG).show();
                }

            }
        }

        analyze();
    }


    public void continueAnalyze2(final byte[] path){
        // This code will always run on the UI thread, therefore is safe to modify UI elements.
        // This code will always run on the UI thread, therefore is safe to modify UI elements.
     //   analyze();
        Bitmap photo = BitmapFactory.decodeByteArray(path, 0, path.length);
     //   Bitmap photo = Functions.getImageBitmap(path);
        FaceOverlayView mFaceOverlayView;
        mFaceOverlayView = (FaceOverlayView) findViewById(R.id.face_overlay);
        //    mFaceOverlayView.setBitmap(photo);

        mFaceOverlayView.setBitmap(photo);
        double smilingProbability = mFaceOverlayView.getSmilingProb();
        if(smilingProbability>0.8) {
            mFaceOverlayView.setBitmap(photo);
            Toast.makeText(this, "smile!",
                    Toast.LENGTH_LONG).show();
        }
        analyze();
    }
}

*/
