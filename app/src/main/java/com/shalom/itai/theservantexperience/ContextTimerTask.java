package com.shalom.itai.theservantexperience;

import android.content.Context;

import java.util.TimerTask;

/**
 * Created by Itai on 13/04/2017.
 */

public abstract class ContextTimerTask extends TimerTask {

        protected Context mContext;
        ContextTimerTask ( Context context )
        {
            this.mContext = context;
        }
}
