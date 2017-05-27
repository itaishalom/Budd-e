package com.shalom.itai.theservantexperience;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.shalom.itai.theservantexperience.Utils.CameraPreview;
import com.shalom.itai.theservantexperience.Utils.mySurfaceView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.shalom.itai.theservantexperience.Utils.Constants.Directory;
import static com.shalom.itai.theservantexperience.Utils.Functions.saveMemory;

public class SelfieV2 extends AppCompatActivity {

    CameraPreview surfaceView;
    SurfaceHolder surfaceHolder;
    SelfieV2 inst;
    private BroadcastReceiver mReceiver;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selfie_v2);

        Button buttonStartCameraPreview = (Button) findViewById(R.id.startcamerapreview);

        getWindow().setFormat(PixelFormat.UNKNOWN);

        surfaceView = (CameraPreview) findViewById(R.id.camerapreview);

        surfaceHolder = surfaceView.getHolder();

        surfaceHolder.addCallback(surfaceView);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


        buttonStartCameraPreview.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
           //         surfaceView.onStart(inst);
                    surfaceView.takeImage();
                }catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        inst = this;
       // surfaceView.onStart(this);
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
                byte[] data = intent.getByteArrayExtra("path");
                mergeImages(data);
            }
        };
        //registering our receiver
        this.registerReceiver(mReceiver, intentFilter);
    }

    private void mergeImages(byte[] imageTaken) {

        Bitmap bottomImage = BitmapFactory.decodeByteArray(imageTaken, 0, imageTaken.length);

        Bitmap topImage = BitmapFactory.decodeResource(this.getResources(), R.drawable.angry);

        Bitmap bmOverlay = Bitmap.createBitmap(bottomImage.getWidth(), bottomImage.getHeight(), bottomImage.getConfig());
        Canvas canvas = new Canvas(bmOverlay);


        canvas.drawBitmap(bottomImage, new Matrix(), null);
        canvas.drawBitmap(topImage, bottomImage.getWidth() - topImage.getWidth(), 300, null);
        saveMemory(bmOverlay, "Our selfie!");

        finish();
    }
}