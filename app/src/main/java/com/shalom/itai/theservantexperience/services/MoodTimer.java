package com.shalom.itai.theservantexperience.services;

import android.content.Context;
import android.util.Log;

import com.shalom.itai.theservantexperience.utils.Constants;

import static com.shalom.itai.theservantexperience.utils.Functions.getBatteryTemperature;
import static com.shalom.itai.theservantexperience.utils.Functions.getBatteryLevel;
import static com.shalom.itai.theservantexperience.utils.Functions.getReceptionLevel;
import static com.shalom.itai.theservantexperience.utils.Functions.throwRandom;

/**
 * Created by Itai on 09/04/2017.
 */

class MoodTimer extends ContextTimerTask {
    private int counter = 0;
    private int top = 0;

    MoodTimer(Context context) {
        super(context);
        top = throwRandom(30, 10);
    }


    @Override
    public void run() {
        counter++;
       /*
        int lastActions = BuggerService.getInstance().getLastAtionsGrade();
        int total = getBatteryLevel(mContext).getValue() + getReceptionLevel(mContext).getValue() + getBatteryTemperature(mContext).getValue()
                + BuggerService.getInstance().getRelationsStatus().getGradeFactor() + lastActions;

        Constants.Mood mood = null;
        if (total >= 18) {
            mood = lastActions > 0 ? Constants.Mood.Excited : Constants.Mood.Happy;
        } else if (total < 18 && total >= 12) {
            mood = lastActions > 0 ? Constants.Mood.Optimistic : Constants.Mood.Fine;
        } else if (total < 12 && total >= 7) {
            mood = lastActions > 0 ? Constants.Mood.Calm : Constants.Mood.Board;
        } else if (total < 7) {
            mood = lastActions > 0 ? Constants.Mood.Angrey : Constants.Mood.Sad;
        }
        Log.d("Mood", "run: " + mood);
        if (counter > top) {
            BuggerService.getInstance().restartCheckStatus();
        }
        */
    }
}
