package com.shalom.itai.theservantexperience.Activities;

       /* import com.google.android.gms.auth.api.Auth;
        import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
        import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
        import com.google.android.gms.auth.api.signin.GoogleSignInResult;
       */

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.shalom.itai.theservantexperience.FaceOverlayView;
import com.shalom.itai.theservantexperience.R;
import com.shalom.itai.theservantexperience.Services.BuggerService;
import com.shalom.itai.theservantexperience.ShakeListener;
import com.shalom.itai.theservantexperience.SmsSend;
import com.shalom.itai.theservantexperience.TakeImageThread;
import com.shalom.itai.theservantexperience.Utils.Functions;
import com.shalom.itai.theservantexperience.Utils.SilentCamera;


import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.hardware.Camera;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.media.MediaPlayer;
import android.media.MediaRecorder;

import android.net.Uri;
import android.os.Bundle;

import android.os.Environment;
import android.os.Handler;

import android.os.Vibrator;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.util.Patterns;
import android.util.SparseArray;

import android.view.View;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;


import static android.Manifest.permission.GET_ACCOUNTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECEIVE_BOOT_COMPLETED;
import static android.Manifest.permission.VIBRATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.shalom.itai.theservantexperience.Services.BuggerService.GlobalPoints;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    private static final int CAMERA_REQUEST = 1888;
    public static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUESTS = 100;
    //  private static final int REQUEST_CAMERA_PERMISSION = 300;
    private static String mFileName = null;
    private boolean safeToTakePicture = false;
    private RecordButton mRecordButton = null;
    private MediaRecorder mRecorder = null;

    private PlayButton mPlayButton = null;
    private MediaPlayer mPlayer = null;
    Camera camera;
    private int cameraId = 0;
    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private boolean permissionToCameraAccepted = false;
    private boolean permissionToConttactsAccepted = false;
    private boolean permissionToAccounts = false;
    private boolean permissionToCalendarRead = false;
    private boolean permissionToCalendarWrite = false;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA,
            Manifest.permission.READ_CONTACTS, Manifest.permission.SEND_SMS, GET_ACCOUNTS,
            Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR,RECEIVE_BOOT_COMPLETED,VIBRATE,WRITE_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE};
    private GoogleApiClient mGoogleApiClient;
    private String userName = "";

    public static MainActivity thisActivity;
    private Timer timerUI;
    public void callSpeech() {
        Intent intent = new Intent(this, SpeechRecognitionActivity.class);
      /*
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        */
        startActivity(intent);
    }



    public void callSms() {
        Intent intent = new Intent(this, SmsSend.class);
      /*
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        */
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUESTS:
                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                permissionToCameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                permissionToConttactsAccepted= grantResults[2] == PackageManager.PERMISSION_GRANTED;
                permissionToAccounts= grantResults[3] == PackageManager.PERMISSION_GRANTED;
                permissionToCalendarRead =  grantResults[5] == PackageManager.PERMISSION_GRANTED;
                permissionToCalendarWrite =  grantResults[6] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted || !permissionToCameraAccepted ||!permissionToConttactsAccepted || !permissionToCalendarWrite || !permissionToCalendarRead) finish();
        Intent service = new Intent(this, BuggerService.class);
        this.startService(service);
        if(BuggerService.getInstance()!=null)
            BuggerService.getInstance().bug();
        Functions.fadingText(this,R.id.textView2);
  //      addCalendarMeeting();
    }

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {

            Toast.makeText(MainActivity.this, mFileName.toString(),
                    Toast.LENGTH_LONG).show();
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    class RecordButton extends android.widget.Button {
        boolean mStartRecording = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onRecord(mStartRecording);
                if (mStartRecording) {
                    setText("Stop recording");
                } else {
                    setText("Start recording");
                }
                mStartRecording = !mStartRecording;
            }
        };

        public RecordButton(Context ctx) {
            super(ctx);
            setText("Start recording");
            setOnClickListener(clicker);
        }
    }

    class PlayButton extends android.widget.Button {
        boolean mStartPlaying = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onPlay(mStartPlaying);
                if (mStartPlaying) {
                    setText("Stop playing");
                } else {
                    setText("Start playing");
                }
                mStartPlaying = !mStartPlaying;
            }
        };

        public PlayButton(Context ctx) {
            super(ctx);
            setText("Start playing");
            setOnClickListener(clicker);
        }
    }

    public void onBackPressed() {

        this.finish();
        super.onBackPressed();
    }

    @Override
    public void onCreate(Bundle icicle) {

        super.onCreate(icicle);
        setContentView(R.layout.activity_main);


        // Record to the external cache directory for visibility
        mFileName = getExternalCacheDir().getAbsolutePath();
        mFileName += "/audiorecordtest.3gp";

        ActivityCompat.requestPermissions(this, permissions, REQUESTS);
   //      FaceOverlayView mFaceOverlayView = (FaceOverlayView) findViewById(R.id.face_overlay);
   //     mFaceOverlayView.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.me));
        Button button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                callSpeech();
            }
        });
        Button buttonSms = (Button) findViewById(R.id.button3);
        buttonSms.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                callSms();
            }
        });

        Button buttonUpdate = (Button) findViewById(R.id.button4);
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

          //      callUpdate();
            }
        });
/*
        LinearLayout ll = new LinearLayout(this);
        mRecordButton = new RecordButton(this);
        ll.addView(mRecordButton,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0));
        mPlayButton = new PlayButton(this);
        ll.addView(mPlayButton,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0));
        setContentView(ll);
  */
//    BuggerService.isMainActivityUp=false;
 //   finish();

        // ShakeListener initialization


      //  analayze();
        Intent service = new Intent(this, BuggerService.class);
        this.startService(service);
        thisActivity = this;

    }

public static MainActivity getInstance()
{
    return thisActivity;
}

    public void onClick(View view) {
        analayze();
        //   Intent cameraIntent=new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
      //  startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    public Bitmap getImageBitmap(Context context,String name){
        try{
            File file = new File(name);
            if(file.exists()) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bitmap = BitmapFactory.decodeFile(name, options);
                return bitmap;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void analayze() {


        //   (new Thread(new TakeImageThread(getApplicationContext()))).start();
        SilentCamera c = new SilentCamera(this);
        c.getCameraInstance();
        //c.prepareCamera();
        //  String path = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"2.jpg";
        c.takePicture();
        // String path = c.getLastImagePath();
        //   c.releaseCamera();
        return;
    }
    public void continueAnalyze(){
        final String path ="";
        timerUI= new Timer();
        timerUI.schedule(new TimerTask() {
            @Override
            public void run() {
                // 'getActivity()' is required as this is being ran from a Fragment.
                MainActivity.getInstance().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // This code will always run on the UI thread, therefore is safe to modify UI elements.

                        Bitmap photo = getImageBitmap(getApplicationContext(),path);
                       // BuggerService.pathToLastImage = "";
                        //Bitmap photo=(Bitmap) data.getExtras().get("data");
                        FaceOverlayView mFaceOverlayView;  mFaceOverlayView = (FaceOverlayView) findViewById(R.id.face_overlay);
                        mFaceOverlayView.setBitmap(photo);

                        SparseArray<Face> mFaces =null;
                        FaceDetector detector = new FaceDetector.Builder( getApplicationContext() )
                                .setTrackingEnabled(true)
                                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
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
                            Toast.makeText(MainActivity.this, "I don't see your face!",
                                    Toast.LENGTH_LONG).show();
                            GlobalPoints -= 2;
                        }else {
                            for (int i = 0; i < mFaces.size(); i++) {
                                Face face = mFaces.valueAt(i);

                                float smilingProbability = face.getIsSmilingProbability();


                                if(smilingProbability<0.8) {
                                    Toast.makeText(MainActivity.this, "you don't smile",
                                            Toast.LENGTH_LONG).show();
                                    GlobalPoints--;
                                }else
                                {
                                    Toast.makeText(MainActivity.this, "you  smile!",
                                            Toast.LENGTH_LONG).show();
                                    GlobalPoints++;
                                }

                            }
                        }
                        stopTimer();
                    }
                });
            }

        }, 0, 3000); // End of your timer code.
    }

    private void stopTimer()
    {
        timerUI.cancel();
        timerUI.purge();
        Toast.makeText(MainActivity.this, "end!",
                Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAMERA_REQUEST && resultCode == RESULT_OK){
            SilentCamera c = new SilentCamera(this);
            c.getCameraInstance();
            //c.prepareCamera();
          //  String path = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"2.jpg";
            c.takePicture();
      //      String path = c.getLastImagePath();
            c.releaseCamera();
            String path = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"1~01";
            Bitmap photo = getImageBitmap(this,path);
            //Bitmap photo=(Bitmap) data.getExtras().get("data");
            FaceOverlayView mFaceOverlayView;  mFaceOverlayView = (FaceOverlayView) findViewById(R.id.face_overlay);
            mFaceOverlayView.setBitmap(photo);

            SparseArray<Face> mFaces =null;
            FaceDetector detector = new FaceDetector.Builder( getApplicationContext() )
                    .setTrackingEnabled(true)
                    .setLandmarkType(FaceDetector.ALL_LANDMARKS)
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
                Toast.makeText(MainActivity.this, "I don't see your face!",
                        Toast.LENGTH_LONG).show();
                GlobalPoints -= 2;
            }else {
                for (int i = 0; i < mFaces.size(); i++) {
                    Face face = mFaces.valueAt(i);

                    float smilingProbability = face.getIsSmilingProbability();


                    if(smilingProbability<0.8) {
                        Toast.makeText(MainActivity.this, "you don't smile",
                                Toast.LENGTH_LONG).show();
                        GlobalPoints--;
                    }else
                    {
                        Toast.makeText(MainActivity.this, "you  smile!",
                                Toast.LENGTH_LONG).show();
                        GlobalPoints++;
                    }

                }
            }

        }

    }


    @Override
    protected void onPause() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
        super.onPause();
        BuggerService.isMainActivityUp = false;
    }




    @Override
    protected void onResume() {
        super.onResume();
        BuggerService.isMainActivityUp = true;
     //   mSensorManager.registerListener(mShakeListener, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }

        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
        BuggerService.isMainActivityUp = false;
    }



}