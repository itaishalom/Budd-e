package com.shalom.itai.theservantexperience.services;

import android.content.Context;

import java.util.TimerTask;

/**
 * Created by Itai on 13/04/2017.
 */

abstract class ContextTimerTask extends TimerTask {

        Context mContext;
        ContextTimerTask ( Context context )
        {
            this.mContext = context;
        }
}
