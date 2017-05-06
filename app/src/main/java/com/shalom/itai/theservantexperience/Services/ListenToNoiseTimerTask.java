package com.shalom.itai.theservantexperience.Services;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.shalom.itai.theservantexperience.Utils.NoiseListener;

import java.util.List;

import static com.shalom.itai.theservantexperience.Services.BuggerService.Activities;
import static com.shalom.itai.theservantexperience.Services.BuggerService.indexActive;
import static com.shalom.itai.theservantexperience.Services.BuggerService.stopBugger;
import static com.shalom.itai.theservantexperience.Utils.Functions.checkScreenAndLock;

/**
 * Created by Itai on 09/04/2017.
 */

public class ListenToNoiseTimerTask extends ContextTimerTask {
    NoiseListener mNoiseListener;
    ListenToNoiseTimerTask(Context context) {
        super(context);
         mNoiseListener = new NoiseListener();
    }



    @Override
    public void run() {
        mNoiseListener.start();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                double noise = mNoiseListener.stop();
                if(noise>100){
                    /// ?? !! ?? !! ??
                }
            }
        }, 5000);
    }
}
