package com.shalom.itai.theservantexperience.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shalom.itai.theservantexperience.FaceOverlayView;
import com.shalom.itai.theservantexperience.R;
import com.shalom.itai.theservantexperience.Services.BuggerService;
import com.shalom.itai.theservantexperience.Utils.SilentCamera;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import static com.shalom.itai.theservantexperience.Services.DayActions.allInsults;
import static com.shalom.itai.theservantexperience.Services.DayActions.allJokes;
import static com.shalom.itai.theservantexperience.Utils.Constants.IMAGE_READY;
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
    private SilentCamera mCamera;

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

    private void putResponseButtons(final Bitmap bitmapImage)
    {
        likeBut = (Button) findViewById(R.id.like);
        likeBut.setVisibility(VISIBLE);
        likeBut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                unlikeBut.setVisibility(INVISIBLE);
                likeBut.setVisibility(INVISIBLE);
                continueAnalyze(bitmapImage);
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
                                BuggerService.setSYSTEM_GlobalPoints(1);
                                text.setText("");
                                mCamera.clearRam();
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
                BuggerService.setSYSTEM_GlobalPoints(-1);

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
         mCamera = new SilentCamera(this);
        mCamera.getCameraInstanceSilentMode();
        mCamera.takePicture();
        return;
    }



    public void continueAnalyze(final Bitmap bitmapImage){

        timerUI= new Timer();
        timerUI.schedule(new TimerTask() {
            @Override
            public void run() {
                // 'getActivity()' is required as this is being ran from a Fragment.
                FunActivity.getInstance().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // This code will always run on the UI thread, therefore is safe to modify UI elements.
                       // Bitmap photo = BitmapFactory.decodeByteArray(path, 0, path.length);
                        FaceOverlayView mFaceOverlayView  = (FaceOverlayView) findViewById(R.id.face_overlay);


                            /*Toast.makeText(FunActivity.this, "I don't see your face!",
                                    Toast.LENGTH_LONG).show();
                            GlobalPoints -= 2;*/
                        mFaceOverlayView.setBitmap(bitmapImage);
                        double smilingProbability = mFaceOverlayView.getSmilingProb();
                        if(smilingProbability>-1) {
                            String data = "";
                            if (smilingProbability < 0.7) {
                                mFaceOverlayView.invalidateThis();
                                Toast.makeText(FunActivity.this, "you don't smile, you lied to me!",
                                        Toast.LENGTH_LONG).show();
                                BuggerService.setSYSTEM_GlobalPoints(-1);
                              data = "You told me I'm funny but you lied";
                            } else {
                                Toast.makeText(FunActivity.this, "you  smile!",
                                        Toast.LENGTH_LONG).show();
                                BuggerService.setSYSTEM_GlobalPoints(1);
                                data = "Your beautiful smile!";
                            }
                            mCamera.saveMemory(bitmapImage,data);

                        }
                        stopTimer();
                    }
                });
            }

        }, 0, 3000);
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
                //extract our message from intent
                Bitmap imageBitmap = mCamera.getImageBitmap();
                putResponseButtons(imageBitmap);
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