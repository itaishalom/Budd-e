package com.shalom.itai.theservantexperience.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.shalom.itai.theservantexperience.FaceOverlayView;
import com.shalom.itai.theservantexperience.R;
import com.shalom.itai.theservantexperience.Services.BuggerService;
import com.shalom.itai.theservantexperience.Utils.SilentCamera;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import com.shalom.itai.theservantexperience.Utils.Functions;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.shalom.itai.theservantexperience.Services.BuggerService.GlobalPoints;
import static com.shalom.itai.theservantexperience.Services.BuggerService.allInsults;
import static com.shalom.itai.theservantexperience.Services.BuggerService.allJokes;
import static com.shalom.itai.theservantexperience.Utils.Constants.SHOW_IMSULT_TIME;


//TODO more interaction
public class FunActivity extends AppCompatActivity {
    TextView text;
    Random rand;
    Button likeBut;
    Button unlikeBut;
    private BroadcastReceiver mReceiver;
    public static FunActivity thisActivity;
    private Timer timerUI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fun);




        rand = new Random();
        text = (TextView) findViewById(R.id.textArea);
        BuggerService.isFunActivityUp = true;
        int jokeNum = rand.nextInt(allJokes.size());
        text.setText("Q:"+allJokes.get(jokeNum));





        Button quitBut = (Button) findViewById(R.id.quit);
        quitBut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                BuggerService b = BuggerService.getInstance();
                if(b!=null)
                {
                    Intent in = (new Intent(getApplicationContext(), BuggerService.class));
                    b.stopService(in);
                }
                finish();
            }
        });

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                analayze();
            }
        }, 4000);
    thisActivity = this;

    }

    private void putResponseButtons(final String pathToImage)
    {
        likeBut = (Button) findViewById(R.id.like);
        likeBut.setVisibility(VISIBLE);
        likeBut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                unlikeBut.setVisibility(INVISIBLE);
                likeBut.setVisibility(INVISIBLE);
                continueAnalyze(pathToImage);
                text.setText("Thanks!!");
                final Animation animationFadeIn = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fadein);
                text.startAnimation(animationFadeIn);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final Animation animationFadeOut = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fadeout);
                        text.startAnimation(animationFadeOut);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                GlobalPoints++;
                                text.setText("");
                                finish();
                            }
                        }, 2000);
                    }
                }, 5000);
            }
        });

        unlikeBut = (Button) findViewById(R.id.unlike);
        unlikeBut.setVisibility(VISIBLE);
        unlikeBut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                unlikeBut.setVisibility(INVISIBLE);
                likeBut.setVisibility(INVISIBLE);
                text.setText(allInsults.get(rand.nextInt(allInsults.size())));
                ImageView imageViewLaugh = (ImageView) findViewById(R.id.imageLaugh);
                imageViewLaugh.setVisibility(INVISIBLE);
                ImageView imageViewAngry = (ImageView) findViewById(R.id.imageAngry);
                imageViewAngry.setVisibility(VISIBLE);
                GlobalPoints--;

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        BuggerService.getInstance().unbug();
                        BuggerService.getInstance().lock();
                        finish();
                    }
                }, SHOW_IMSULT_TIME);

            }
        });

    }


    public static FunActivity getInstance()
    {
        return thisActivity;
    }



    public void analayze() {
        SilentCamera c = new SilentCamera(this);
        c.getCameraInstance();
        c.takePicture();
        return;
    }

    public void continueAnalyze(final String path){

        timerUI= new Timer();
        timerUI.schedule(new TimerTask() {
            @Override
            public void run() {
                // 'getActivity()' is required as this is being ran from a Fragment.
                FunActivity.getInstance().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // This code will always run on the UI thread, therefore is safe to modify UI elements.
                        Bitmap photo = Functions.getImageBitmap(path);
                        FaceOverlayView mFaceOverlayView;
                        mFaceOverlayView = (FaceOverlayView) findViewById(R.id.face_overlay);
                   //    mFaceOverlayView.setBitmap(photo);

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
                            return;
                            /*Toast.makeText(FunActivity.this, "I don't see your face!",
                                    Toast.LENGTH_LONG).show();
                            GlobalPoints -= 2;*/
                        }else {//mFaces.size()
                            for (int i = 0; i <1 ; i++) {
                                Face face = mFaces.valueAt(i);

                                float smilingProbability = face.getIsSmilingProbability();


                                if(smilingProbability<0.7) {
                                    mFaceOverlayView.setBitmap(photo);
                                    Toast.makeText(FunActivity.this, "you don't smile, you lied to me!",
                                            Toast.LENGTH_LONG).show();
                                    GlobalPoints--;
                                }else
                                {
                                    Toast.makeText(FunActivity.this, "you  smile!",
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
        Toast.makeText(FunActivity.this, "end!",
                Toast.LENGTH_LONG).show();
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
                String pathToImage = intent.getStringExtra("path");
                putResponseButtons(pathToImage);
                //log our message value
              //  Log.i("InchooTutorial", msg_for_me);

            }
        };
        //registering our receiver
        this.registerReceiver(mReceiver, intentFilter);
    }
    @Override
    public void onStop() {
        super.onStop();
        BuggerService.isMainActivityUp = false;
    }

    public void onBackPressed() {
        Toast.makeText(this, "Please answer before you leave!", Toast.LENGTH_LONG).show();
    }

}


//