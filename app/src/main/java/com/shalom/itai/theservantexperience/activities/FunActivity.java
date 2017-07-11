package com.shalom.itai.theservantexperience.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shalom.itai.theservantexperience.FaceOverlayView;
import com.shalom.itai.theservantexperience.R;
import com.shalom.itai.theservantexperience.services.BuggerService;
import com.shalom.itai.theservantexperience.utils.SilentCamera;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import static com.shalom.itai.theservantexperience.services.DayActions.allInsults;
import static com.shalom.itai.theservantexperience.services.DayActions.allJokes;
import static com.shalom.itai.theservantexperience.utils.Constants.IMAGE_READY;
import static com.shalom.itai.theservantexperience.utils.Constants.SHOW_IMSULT_TIME;


//TODO more interaction
public class FunActivity extends ToolBarActivityNew {
    private TextView text;
    private Random rand;
    private ImageButton likeBut;
    private ImageButton unlikeBut;
    private BroadcastReceiver mReceiver;
    //private static FunActivity thisActivity;
    private Timer timerUI;
    private SilentCamera mCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState,R.layout.activity_fun,R.menu.tool_bar_game_options,true,-1);
        //setContentView(R.layout.activity_fun);

//    super.onCreate(savedInstanceState, R.layout.activity_matches_game_2, R.menu.tool_bar_game_options,false,-1);
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
      //  thisActivity = this;

    }

    private void putResponseButtons(final Bitmap bitmapImage)
    {
        likeBut = (ImageButton) findViewById(R.id.like);
        likeBut.setVisibility(VISIBLE);
        likeBut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                unlikeBut.setVisibility(INVISIBLE);
                likeBut.setVisibility(INVISIBLE);
                continueAnalyze(bitmapImage);
                text.setText("Thanks!!");
                 findViewById(R.id.check_this_joke).setVisibility(INVISIBLE);
                final Animation animationFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
                text.startAnimation(animationFadeIn);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final Animation animationFadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout);
                        text.startAnimation(animationFadeOut);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                BuggerService.setSYSTEM_GlobalPoints(1,"You liked my joke");
                                text.setText("");
                                mCamera.clearRam();
                                finish();
                            }
                        }, 2000);
                    }
                }, 5000);
            }
        });

        unlikeBut = (ImageButton) findViewById(R.id.unlike);
        unlikeBut.setVisibility(VISIBLE);
        unlikeBut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                unlikeBut.setVisibility(INVISIBLE);
                likeBut.setVisibility(INVISIBLE);
                text.setText(allInsults.get(rand.nextInt(allInsults.size())));
                ImageView imageViewAngry = (ImageView) findViewById(R.id.imageAngry);
                imageViewAngry.setVisibility(VISIBLE);
                BuggerService.setSYSTEM_GlobalPoints(-1, "You didn't like my joke");

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

 /*   private static FunActivity getInstance()
    {
        return thisActivity;
    }
*/
    private void analayze() {
         mCamera = new SilentCamera(this);
        mCamera.getCameraInstanceSilentMode();
        mCamera.takePicture();
        return;
    }



    private void continueAnalyze(final Bitmap bitmapImage){

        timerUI= new Timer();
        timerUI.schedule(new TimerTask() {
            @Override
            public void run() {
                // 'getActivity()' is required as this is being ran from a Fragment.
                FunActivity.this.runOnUiThread(new Runnable() {
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
                            String data;
                            if (smilingProbability < 0.7) {
                                mFaceOverlayView.invalidateThis();
                                Toast.makeText(FunActivity.this, "you don't smile, you lied to me!",
                                        Toast.LENGTH_LONG).show();
                                BuggerService.setSYSTEM_GlobalPoints(-1,"You told me my joke was funny but you lied");
                              data = "You told me I'm funny but you lied";
                            } else {
                                Toast.makeText(FunActivity.this, "you  smile!",
                                        Toast.LENGTH_LONG).show();
                                BuggerService.setSYSTEM_GlobalPoints(1,"You laughed at my joke");
                                data = "Your beautiful smile!";
                            }
                            SilentCamera.saveMemory(bitmapImage,data);

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
    }

    public void onBackPressed() {
        Toast.makeText(this, "Please answer before you leave!", Toast.LENGTH_LONG).show();
    }

}


//