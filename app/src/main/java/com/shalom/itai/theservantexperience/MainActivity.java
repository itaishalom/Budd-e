package com.shalom.itai.theservantexperience;
        import com.google.android.gms.vision.Frame;
        import com.google.android.gms.vision.face.Face;
        import com.google.android.gms.vision.face.FaceDetector;
        import  com.google.android.gms.vision.face.FaceDetector.Builder;
        import android.Manifest;
        import android.content.Context;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.SurfaceTexture;
        import android.hardware.Camera;

        import android.media.MediaPlayer;
        import android.media.MediaRecorder;
        import android.net.ConnectivityManager;
        import android.net.NetworkInfo;
        import android.net.Uri;
        import android.os.Bundle;
        import android.os.Environment;
        import android.os.Handler;
        import android.provider.MediaStore;
        import android.speech.RecognizerIntent;
        import android.support.annotation.NonNull;
        import android.support.v4.app.ActivityCompat;
        import android.support.v7.app.AppCompatActivity;
        import android.util.AttributeSet;
        import android.util.Log;
        import android.util.SparseArray;
        import android.view.SurfaceView;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.LinearLayout;
        import android.widget.Toast;

        import java.io.ByteArrayInputStream;
        import java.io.File;
        import java.io.FileNotFoundException;
        import java.io.FileOutputStream;
        import java.io.IOException;
        import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST= 1888;
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
    private String[] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA,Manifest.permission.READ_CONTACTS,Manifest.permission.SEND_SMS};


    public void callSpeech() {
        Intent intent = new Intent(this, speechReconition.class);
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
                break;
        }
        if (!permissionToRecordAccepted || !permissionToCameraAccepted ||!permissionToConttactsAccepted) finish();

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
    }

    public void onClick(View view) {
        Intent cameraIntent=new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAMERA_REQUEST && resultCode == RESULT_OK){

            Bitmap photo=(Bitmap) data.getExtras().get("data");
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
            }else {
                for (int i = 0; i < mFaces.size(); i++) {
                    Face face = mFaces.valueAt(i);

                    float smilingProbability = face.getIsSmilingProbability();


                    if(smilingProbability<0.8) {
                        Toast.makeText(MainActivity.this, "you don't smile",
                                Toast.LENGTH_LONG).show();
                    }else
                    {
                        Toast.makeText(MainActivity.this, "you  smile!",
                                Toast.LENGTH_LONG).show();
                    }

                }
            }

        }

    }

    private int findFrontFacingCamera() {
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                Log.d(LOG_TAG, "Camera found");
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }

    @Override
    protected void onPause() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
        super.onPause();
    }


/*
    private Camera.PictureCallback getJpegCallback() {
        Camera.PictureCallback jpeg = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                FileOutputStream fos;
                try {
                    fos = new FileOutputStream("test.jpeg");
                    fos.write(data);
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
*/




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
    }
}