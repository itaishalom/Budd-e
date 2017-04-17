package com.shalom.itai.theservantexperience.Activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.shalom.itai.theservantexperience.R;
import com.shalom.itai.theservantexperience.Services.BuggerService;

import java.util.Random;

import static android.view.View.INVISIBLE;
import static com.shalom.itai.theservantexperience.Services.BuggerService.GlobalPoints;
import static com.shalom.itai.theservantexperience.Services.BuggerService.allInsults;
import static com.shalom.itai.theservantexperience.Services.BuggerService.allJokes;

public class FunActivity extends AppCompatActivity {
    TextView text;
    Random rand;
    Button likeBut;
    Button unlikeBut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fun);

        rand = new Random();
        text = (TextView) findViewById(R.id.textArea);
        BuggerService.isFunActivityUp = true;
        int jokeNum = rand.nextInt(allJokes.size());
        text.setText(allJokes.get(jokeNum));



        likeBut = (Button) findViewById(R.id.like);
        likeBut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                unlikeBut.setVisibility(INVISIBLE);
                likeBut.setVisibility(INVISIBLE);
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
        unlikeBut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                unlikeBut.setVisibility(INVISIBLE);
                likeBut.setVisibility(INVISIBLE);
                text.setText(allInsults.get(rand.nextInt(allInsults.size())));
                GlobalPoints--;

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        BuggerService.getInstance().unbug();
                        BuggerService.getInstance().lock();
                        finish();
                    }
                }, 1000);

            }
        });


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
    }

    @Override
    protected void onPause() {
        super.onPause();
        BuggerService.isFunActivityUp = false;
    }
    @Override
    protected void onResume() {
        super.onResume();
        BuggerService.isMainActivityUp = true;
    }
    @Override
    public void onStop() {
        super.onStop();
        BuggerService.isMainActivityUp = false;
    }
}


//