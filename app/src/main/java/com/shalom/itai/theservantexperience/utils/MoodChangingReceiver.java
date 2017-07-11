package com.shalom.itai.theservantexperience.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import static com.shalom.itai.theservantexperience.utils.Constants.ENTITY_NAME;
import static com.shalom.itai.theservantexperience.utils.Constants.MOOD_CHANGE_BROADCAST;
import static com.shalom.itai.theservantexperience.utils.Constants.PREFS_NAME;
import static com.shalom.itai.theservantexperience.utils.Constants.SETTING_USERNAME;

/**
 * Proudly written by Itai on 11/07/2017.
 */

public class MoodChangingReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(MOOD_CHANGE_BROADCAST)){
            SharedPreferences settings = context.getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
            String name = settings.getString(SETTING_USERNAME,"");
            Client cl = Client.getInstance(name);
            cl.sendMessage(ENTITY_NAME +"'s mood at " +name +" is now "+intent.getStringExtra("Mood") );
        }
    }
}
