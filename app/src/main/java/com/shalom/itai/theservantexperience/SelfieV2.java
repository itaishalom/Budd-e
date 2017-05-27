package com.shalom.itai.theservantexperience;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
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

public class SelfieV2 extends AppCompatActivity {

    CameraPreview surfaceView;
    SurfaceHolder surfaceHolder;
    boolean previewing = false;

    private boolean hasCamera;
   AppCompatActivity inst;
    private Camera camera;
    private int cameraId;
    ;

    String stringPath = "/sdcard/samplevideo.3gp";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selfie_v2);

        Button buttonStartCameraPreview = (Button) findViewById(R.id.startcamerapreview);
        Button buttonStopCameraPreview = (Button) findViewById(R.id.stopcamerapreview);
        //LinearLayout ln = (LinearLayout) findViewById(R.id.linearLayout1);
     //   ln.bringToFront();
   //     ImageView jonsFace = (ImageView) findViewById(R.id.jonsFace);
     //   jonsFace.bringToFront();
        getWindow().setFormat(PixelFormat.UNKNOWN);

        surfaceView = (CameraPreview) findViewById(R.id.camerapreview);

        surfaceHolder = surfaceView.getHolder();

        surfaceHolder.addCallback(surfaceView);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

  //      surfaceView.surfaceCreated(surfaceHolder);

        // = c.getApplicationContext();

inst = this;


  /*
        camera = Camera.open(cameraId);
        if (camera != null) {

            //    camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
             //   surfaceView.startFromOutside = true;
            // //   surfaceView.surfaceCreated(surfaceHolder);
                Camera.Parameters params = camera.getParameters();
                params.setJpegQuality(50);
                params.setRotation(270);
                params.setRotation(90);
                camera.setParameters(params);
                previewing = true;
         /*
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        */
        buttonStartCameraPreview.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                surfaceView.takeImage(inst);
            }
        });
                // TODO Auto-generated method stub




        buttonStopCameraPreview.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (camera != null && previewing) {
                    camera.stopPreview();
                    camera.release();
                    camera = null;

                    previewing = false;
                }
            }
        });

    }



    private void getCamera(){
        if(this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            cameraId = getFrontCameraId();

            if(cameraId != -1){
                hasCamera = true;
            }else{
                hasCamera = false;
            }
        }else{
            hasCamera = false;
        }
    }



    private int getFrontCameraId(){
        int camId = -1;
        int numberOfCameras = Camera.getNumberOfCameras();
        Camera.CameraInfo ci = new Camera.CameraInfo();

        for(int i = 0;i < numberOfCameras;i++){
            Camera.getCameraInfo(i,ci);
            if(ci.facing == Camera.CameraInfo.CAMERA_FACING_FRONT){
                camId = i;
            }
        }

        return camId;
    }

}