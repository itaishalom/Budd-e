package com.shalom.itai.theservantexperience.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.shalom.itai.theservantexperience.Services.BuggerService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Itai on 21/04/2017.
 */

public class SilentCamera {
    private String TAG = "SilentCamera";
    private Context context;

    private boolean hasCamera;

    private Camera camera;
    private int cameraId;
    private String latestImage;

    public SilentCamera(Context c) {
        context = c.getApplicationContext();

        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            cameraId = getFrontCameraId();

            if (cameraId != -1) {
                hasCamera = true;
                open();
            } else {
                hasCamera = false;
            }
        } else {
            hasCamera = false;
        }
    }

    public Camera.Parameters getParameters() {
        return camera.getParameters();
    }

    public void stopPreview() {
        camera.stopPreview();
    }

    public boolean hasCamera() {
        return hasCamera;
    }

    public void getCameraInstanceSilentMode() {

                prepareCamera();
    }

    private void open() {
        camera = null;
        if (hasCamera) {
            try {
                camera = Camera.open(cameraId);
            } catch (Exception e) {
                hasCamera = false;
            }
        }
    }

    public void getCameraInstanceLoudMode(SurfaceHolder surface, int width, int height) {

                prepareCameraLoud(surface,width,height);
    }

    public void takePicture() {
        if (hasCamera) {
            camera.takePicture(null, null, mPicture);
        }
    }

    public void takePicture(Context context) {
      this.context = context;
        takePicture();
    }

    public void releaseCamera() {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    private int getFrontCameraId() {
        int camId = -1;
        int numberOfCameras = Camera.getNumberOfCameras();
        Camera.CameraInfo ci = new Camera.CameraInfo();

        for (int i = 0; i < numberOfCameras; i++) {
            Camera.getCameraInfo(i, ci);
            if (ci.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                camId = i;
            }
        }
        return camId;
    }

    private void prepareCamera() {
        //  SurfaceView view = new SurfaceView(context);
        mySurfaceView view = new mySurfaceView(context);
        try {
            camera.setPreviewDisplay(view.getHolder());

        } catch (IOException e) {
            e.printStackTrace();
        }
        SurfaceTexture st = new SurfaceTexture(MODE_PRIVATE);
        try {
            camera.setPreviewTexture(st);
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.startPreview();

        Camera.Parameters params = camera.getParameters();
        params.setJpegQuality(50);
        params.setRotation(270);
        //    params.setRotation(90);
        camera.setParameters(params);
    }

    private void prepareCameraLoud(SurfaceHolder surface, int width, int height) {
        //  SurfaceView view = new SurfaceView(context);
        //    mySurfaceView view = new mySurfaceView(context);
        Camera.Parameters parameters = camera.getParameters();
        parameters.setPreviewSize(width, height);
        parameters.setRotation(270);
        parameters.setJpegQuality(50);
        camera.setParameters(parameters);
        camera.setDisplayOrientation(90);
        try {
            camera.setPreviewDisplay(surface);
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.startPreview();
    }


    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            releaseCamera();
            Intent i = new Intent("android.intent.action.ImageReady").putExtra("path", data);
            try {
                context.sendBroadcast(i);
            }catch(Exception e){
                Log.i("ISSUE IN SEND","issue not send");
                sendAgain(data);
            }

        }
    };

    private void sendAgain(byte[] data){
        Intent i = new Intent("android.intent.action.ImageReady").putExtra("path", data);
        try {
            context.sendBroadcast(i);
        }catch(Exception e){
            Log.i("ISSUE IN SEND","issue not send");
            sendAgain(data);
        }
    }

    /*
    private File getOutputMediaFile() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyCameraApp");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        latestImage = mediaFile.getAbsolutePath();
        this.releaseCamera();
        Log.d(TAG, "New Image: " + latestImage);

        Intent i = new Intent("android.intent.action.ImageReady").putExtra("path", latestImage);
        context.sendBroadcast(i);


        return mediaFile;
    }
    */
}