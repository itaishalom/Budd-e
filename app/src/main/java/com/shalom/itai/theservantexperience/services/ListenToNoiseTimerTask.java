package com.shalom.itai.theservantexperience.services;

import android.content.Context;
import android.content.SharedPreferences;

import com.shalom.itai.theservantexperience.utils.Functions;
import com.shalom.itai.theservantexperience.utils.NoiseListener;

import static com.shalom.itai.theservantexperience.utils.Constants.INITIAL_NOISE_LEVEL;
import static com.shalom.itai.theservantexperience.utils.Constants.PREFS_NAME;
import static com.shalom.itai.theservantexperience.utils.Constants.SETTINGS_INITIAL_TIRED_POINTS;
import static com.shalom.itai.theservantexperience.utils.Constants.SETTINGS_MAX_TIRED_POINTS;
import static com.shalom.itai.theservantexperience.utils.Constants.SETTINGS_NOISE_LEVEL;
import static com.shalom.itai.theservantexperience.utils.Constants.SETTINGS_TIRED_POINTS;

/**
 * Created by Itai on 09/04/2017.
 */

class ListenToNoiseTimerTask extends ContextTimerTask {
    private boolean shouldContinue;

    ListenToNoiseTimerTask(Context context) {
        super(context);
        //   mNoiseListener = new NoiseListener();
        shouldContinue = true;
    }




    @Override
    public void run() {
        if (!shouldContinue)
            return;
        SharedPreferences settings = mContext.getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        int noiseLevelSettings = settings.getInt(SETTINGS_NOISE_LEVEL, INITIAL_NOISE_LEVEL);

        int tired= settings.getInt(SETTINGS_TIRED_POINTS, SETTINGS_INITIAL_TIRED_POINTS);
        if (tired >= SETTINGS_MAX_TIRED_POINTS-30 && tired < SETTINGS_MAX_TIRED_POINTS-10) {
            if (Functions.throwRandom(10, 1) >= 8) {
                Functions.writeToSettings(SETTINGS_NOISE_LEVEL,INITIAL_NOISE_LEVEL,mContext);
                BuggerService.getInstance().wakeUpJon();
                return;
            }
        }
        if (tired >= SETTINGS_MAX_TIRED_POINTS-10 && tired < SETTINGS_MAX_TIRED_POINTS-1) {
            if (Functions.throwRandom(4, 2) >= 3) {
                Functions.writeToSettings(SETTINGS_NOISE_LEVEL,INITIAL_NOISE_LEVEL,mContext);
                BuggerService.getInstance().wakeUpJon();
                return;
            }
        }
        if (tired >=SETTINGS_MAX_TIRED_POINTS-1){
            Functions.writeToSettings(SETTINGS_NOISE_LEVEL,INITIAL_NOISE_LEVEL,mContext);
            BuggerService.getInstance().wakeUpJon();
            return;
        }
        if(tired <SETTINGS_MAX_TIRED_POINTS)
            Functions.writeToSettings(SETTINGS_TIRED_POINTS,tired+1,mContext);


        NoiseListener mNoiseListener = new NoiseListener();
        try {
            mNoiseListener.start();
        }catch(Exception e){
            return;
        }
            try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        double noise = mNoiseListener.stop();

        if (noise > noiseLevelSettings) {
            shouldContinue = false;
            mNoiseListener.dispose();
            Functions.popUpMessage(mContext, "Hi! You woke me up!","MainActivity");

            return;
        }
/*
        Looper.prepare();
        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                Looper.loop();

                double noise = mNoiseListener.stop();

                if(noise>100){
                    shouldContinue= false;
                    mNoiseListener.dispose();
                    Functions.popUpMessage(mContext, "Hi! You woke me up!");
                    BuggerService.getInstance().wakeUpJon();
                    return;
                }
                Looper.loop();
            }
        }, 3000);
        Looper.loop();
        */
    }
}
