package com.shalom.itai.theservantexperience;

       /* import com.google.android.gms.auth.api.Auth;
        import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
        import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
        import com.google.android.gms.auth.api.signin.GoogleSignInResult;
       */

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import android.hardware.Camera;

import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.media.MediaPlayer;
import android.media.MediaRecorder;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;

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


import java.io.IOException;

import java.util.regex.Pattern;


import static android.Manifest.permission.GET_ACCOUNTS;
import static android.Manifest.permission.RECEIVE_BOOT_COMPLETED;
import static com.shalom.itai.theservantexperience.updateOS.IS_INSTALLED;
import static com.shalom.itai.theservantexperience.updateOS.PREFS_NAME;


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
            Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR,RECEIVE_BOOT_COMPLETED};
    private GoogleApiClient mGoogleApiClient;
    private String userName = "";

/*
    private void buildGoogleApiClient(String accountName) {
        GoogleSignInOptions.Builder gsoBuilder = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail();

        if (accountName != null) {
            gsoBuilder.setAccountName(accountName);
        }

        if (mGoogleApiClient != null) {
            mGoogleApiClient.stopAutoManage(this);
        }

        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.CREDENTIALS_API)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gsoBuilder.build());

        mGoogleApiClient = builder.build();
    }

    private void googleSilentSignIn() {
        // Try silent sign-in with Google Sign In API
        buildGoogleApiClient(null);
        OptionalPendingResult<GoogleSignInResult> opr =
                Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            GoogleSignInResult gsr = opr.get();
            handleGoogleSignIn(gsr);
        } else {
            //   showProgress();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    //      hideProgress();
                    userName = handleGoogleSignIn(googleSignInResult);
                }
            });
        }
    }




    private String handleGoogleSignIn(GoogleSignInResult gsr) {


        boolean isSignedIn = (gsr != null) && gsr.isSuccess();
        if (isSignedIn) {
            // Display signed-in UI
            GoogleSignInAccount gsa = gsr.getSignInAccount();
            String status = String.format("Signed in as %s (%s)", gsa.getDisplayName(),
                    gsa.getEmail());
            return gsa.getDisplayName();
        }
        return "";
    }*/

    public static String getPrimaryEmail(Context context) {
        try {
            AccountManager accountManager = AccountManager.get(context);
            if (accountManager == null)
                return "";
            Account[] accounts = accountManager.getAccounts();
            Pattern emailPattern = Patterns.EMAIL_ADDRESS;
            for (Account account : accounts) {
                // make sure account.name is an email address before adding to the list
                if (emailPattern.matcher(account.name).matches()) {
                    return account.name.split("@")[0];
                }
            }
            return "";
        } catch (SecurityException e) {
            // exception will occur if app doesn't have GET_ACCOUNTS permission
            return "";
        }
    }

    public void callSpeech() {
        Intent intent = new Intent(this, speechReconition.class);
      /*
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        */
        startActivity(intent);
    }

    public void callUpdate() {
        Intent intent = new Intent(this, updateActivity.class);
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

    private void addCalendarMeeting() {
        ContentResolver cr = this.getContentResolver();
        ContentValues values = new ContentValues();
        Calendar cal = Calendar.getInstance();
        values.put(CalendarContract.Events.DTSTART, cal.getTimeInMillis() + 60 * 60 * 1000);
        values.put(CalendarContract.Events.TITLE, "Jon's birthday!");
        values.put(CalendarContract.Events.DESCRIPTION, "Happy birthday to me!");

        TimeZone timeZone = TimeZone.getDefault();
        values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());

        // default calendar
        values.put(CalendarContract.Events.CALENDAR_ID, 1);

        values.put(CalendarContract.Events.RRULE, "FREQ=YEARLY");
        //for one hour
        values.put(CalendarContract.Events.DURATION, "+P1H");

        values.put(CalendarContract.Events.HAS_ALARM, 1);

        // insert event to calendar

        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
        Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("com.google.android.calendar");
        startActivity(LaunchIntent);
    }

    private void addCalendarMeeting2()
    {
        Calendar cal = Calendar.getInstance();
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("beginTime", cal.getTimeInMillis());
        intent.putExtra("allDay", true);
        intent.putExtra("rrule", "FREQ=YEARLY");
        intent.putExtra("endTime", cal.getTimeInMillis()+60*60*1000);
        intent.putExtra("title", "A Test Event from android app");
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
        final Animation animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
        TextView  welcome_text = (TextView) findViewById(R.id.textView2);
        Intent service = new Intent(this, BuggerService.class);
        this.startService(service);
        BuggerService.getInstance().bug();
        welcome_text.setText("Hello "+ getPrimaryEmail(this)+"\n I am Jon, your new friend");
        welcome_text.startAnimation(animationFadeIn);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                final Animation animationFadeOut = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fadeout);
                TextView  welcome_text = (TextView) findViewById(R.id.textView2);
                welcome_text.startAnimation(animationFadeOut);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        TextView  welcome_text = (TextView) findViewById(R.id.textView2);
                        welcome_text.setText("");
                        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                        boolean isInstalled = settings.getBoolean(IS_INSTALLED, false);
                        if(!isInstalled) {

                            SharedPreferences.Editor editor = settings.edit();
                            editor.putBoolean(IS_INSTALLED, true);
                            editor.commit();
                            addCalendarMeeting();
                        }
                    }
                }, 2000);
            }
        }, 5000);
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

                callUpdate();
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
        Intent service = new Intent(this, BuggerService.class);
        this.startService(service);


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

        BuggerService.isMainActivityUp = false;
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
    protected void onResume() {
        super.onResume();
        BuggerService.isMainActivityUp = true;
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