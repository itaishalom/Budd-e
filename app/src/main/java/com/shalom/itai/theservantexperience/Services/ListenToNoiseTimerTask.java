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
        mNoiseLevel = 75.0;
     //   mNoiseListener = new NoiseListener();
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
        mNoiseListener = new NoiseListener();
        mNoiseListener.start();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        double noise = mNoiseListener.stop();

        if(noise>NightActions.getNoiseLevel()) {
            shouldContinue = false;
            mNoiseListener.dispose();
            Functions.popUpMessage(mContext, "Hi! You woke me up!");
            BuggerService.getInstance().wakeUpJon();
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
