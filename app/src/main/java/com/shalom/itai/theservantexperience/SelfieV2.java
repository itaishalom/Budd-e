package com.shalom.itai.theservantexperience;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.shalom.itai.theservantexperience.Activities.DialogCaller;
import com.shalom.itai.theservantexperience.Activities.MainActivity;
import com.shalom.itai.theservantexperience.Activities.MyAlertDialogFragment;
import com.shalom.itai.theservantexperience.Services.BuggerService;
import com.shalom.itai.theservantexperience.Utils.CameraPreview;

import static com.shalom.itai.theservantexperience.R.id.imageView;
import static com.shalom.itai.theservantexperience.Utils.Constants.IMAGE_READY;
import static com.shalom.itai.theservantexperience.Utils.SilentCamera.saveMemory;

public class SelfieV2 extends AppCompatActivity implements DialogCaller {

    CameraPreview surfaceView;
    SurfaceHolder surfaceHolder;
    SelfieV2 inst;
    private BroadcastReceiver mReceiver;
    private ImageView jonPng;

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
        jonPng = ((ImageView) findViewById(R.id.jon_in_slefie));

        surfaceHolder.addCallback(surfaceView);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        buttonStartCameraPreview.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    surfaceView.takeImage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        inst = this;
        showDialog();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(this.mReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(
                IMAGE_READY);

        mReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Bitmap imageBitmap = surfaceView.getImageBitmap();
                if (FaceOverlayView.isFaceInImage(imageBitmap, inst)) {
                    BuggerService.setSYSTEM_GlobalPoints(2);
                    mergeImages(imageBitmap);
                } else {
                    Toast.makeText(inst ,"You liar! Your'e face not on the image!", Toast.LENGTH_LONG).show();
                    BuggerService.setSYSTEM_GlobalPoints(-2);
                    finish();
                }
            }
        };
        //registering our receiver
        this.registerReceiver(mReceiver, intentFilter);
    }

    private void mergeImages(Bitmap bottomImage) {
       // (ImageView) findViewById(R.id.jon_in_slefie);
        ImageView jonPng = ((ImageView) findViewById(R.id.jon_in_slefie));
        //Bitmap topImag2e = BitmapFactory.decodeResource(this.getResources(), R.drawable.jon_png);
        Bitmap topImage = ((BitmapDrawable)jonPng.getDrawable()).getBitmap();

        topImage = (Bitmap.createScaledBitmap(topImage, jonPng.getWidth()+jonPng.getWidth()/2, jonPng.getHeight(), false));

        Bitmap bmOverlay = Bitmap.createBitmap(bottomImage.getWidth(), bottomImage.getHeight(), bottomImage.getConfig());
        Canvas canvas = new Canvas(bmOverlay);

        canvas.drawBitmap(bottomImage, new Matrix(), null);
        canvas.drawBitmap(topImage, bottomImage.getWidth() - topImage.getWidth(), 300, null);
        saveMemory(bmOverlay, "Our selfie!");
        Toast.makeText(inst ,"Thanks for the selfie!", Toast.LENGTH_LONG).show();
        surfaceView.clearRam();
        finish();
    }

    @Override
    public void doPositive() {
        jonPng.setVisibility(View.VISIBLE);
    }

    @Override
    public void doNegative() {
        Toast.makeText(inst ,"I hate you!", Toast.LENGTH_LONG).show();
        BuggerService.setSYSTEM_GlobalPoints(-2);
        finish();
    }

    @Override
    public void showDialog() {
        DialogFragment newFragment = MyAlertDialogFragment
                .newInstance(R.string.alert_dialog_Selfie_buttons_title,"Yes!","No!",getClass().getName());
        newFragment.show(getSupportFragmentManager(),"dialog");
    }
}