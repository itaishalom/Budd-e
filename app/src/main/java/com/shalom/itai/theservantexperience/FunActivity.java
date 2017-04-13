package com.shalom.itai.theservantexperience;

import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

import static com.shalom.itai.theservantexperience.BuggerService.GlobalPoints;
import static com.shalom.itai.theservantexperience.BuggerService.allInsults;
import static com.shalom.itai.theservantexperience.BuggerService.allJokes;
import static com.shalom.itai.theservantexperience.updateOS.IS_INSTALLED;
import static com.shalom.itai.theservantexperience.updateOS.PREFS_NAME;

public class FunActivity extends AppCompatActivity {
    TextView text;
    Random rand;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fun);
        rand = new Random();
        text = (TextView) findViewById(R.id.textArea);
        BuggerService.isFunActivityUp = true;
        int jokeNum = rand.nextInt(allJokes.size());
        text.setText(allJokes.get(jokeNum));
        Button likeBut = (Button) findViewById(R.id.like);
        likeBut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

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

        Button unlikeBut = (Button) findViewById(R.id.unlike);
        unlikeBut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

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
