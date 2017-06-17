package com.shalom.itai.theservantexperience.services;

import android.content.Context;

import static com.shalom.itai.theservantexperience.Utils.Functions.getBatteryLevel;
import static com.shalom.itai.theservantexperience.Utils.Functions.getReceptionLevel;
import static com.shalom.itai.theservantexperience.Utils.Functions.throwRandom;

/**
 * Created by Itai on 09/04/2017.
 */

class MoodTimer extends ContextTimerTask {
    private int counter = 0;
    private int top = 0;
    MoodTimer(Context context) {
        super(context);
        top = throwRandom(30,10);
    }


    @Override
    public void run() {
        counter++;
        int total = getBatteryLevel(mContext) + getReceptionLevel(mContext);
        if (total >= 8) {
            BuggerService.setSYSTEM_GlobalPoints(2);
        } else if (total < 8 && total >= 5) {
            BuggerService.setSYSTEM_GlobalPoints(1);
        } else if (total < 5 && total >= 3) {
            BuggerService.setSYSTEM_GlobalPoints(-1);
        } else if (total < 3) {
            BuggerService.setSYSTEM_GlobalPoints(-2);
        }
        if( counter> top){
            BuggerService.getInstance().restartCheckStatus();
        }
    }
}
