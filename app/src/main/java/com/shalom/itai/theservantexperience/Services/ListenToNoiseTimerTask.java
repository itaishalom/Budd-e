package com.shalom.itai.theservantexperience.Services;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.shalom.itai.theservantexperience.Utils.Functions;
import com.shalom.itai.theservantexperience.Utils.NoiseListener;

import java.util.List;

import static com.shalom.itai.theservantexperience.Services.BuggerService.indexActive;
import static com.shalom.itai.theservantexperience.Services.BuggerService.stopBugger;
import static com.shalom.itai.theservantexperience.Utils.Functions.checkScreenAndLock;

/**
 * Created by Itai on 09/04/2017.
 */

public class ListenToNoiseTimerTask extends ContextTimerTask {
    private NoiseListener mNoiseListener;
    private double mNoiseLevel;
    private boolean shouldContinue;
    ListenToNoiseTimerTask(Context context) {
        super(context);
        mNoiseListener = new NoiseListener();
        shouldContinue = true;
    }

    public void setNoiseLevel(double val) {
        mNoiseLevel = val;
    }

    public void setNoiseLevel() {
        setNoiseLevel(70.0);
    }

    @Override
    public void run() {
        if(!shouldContinue)
            return;
        Looper.prepare();

        mNoiseListener.start();

        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                double noise = mNoiseListener.stop();

                if(noise>50){
                    shouldContinue= false;
                    mNoiseListener.dispose();
                    Functions.popUpMessage(mContext, "Hi! You woke me up!");
                    NightService.getInstance().stopNoiseListener();
                }
            }
        }, 5000);
        Looper.loop();
    }
}
