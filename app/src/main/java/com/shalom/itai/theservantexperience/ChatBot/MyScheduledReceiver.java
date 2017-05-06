package com.shalom.itai.theservantexperience.ChatBot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.shalom.itai.theservantexperience.MessageBox;

import static com.shalom.itai.theservantexperience.Utils.Constants.MESSAGE_BOX_START_ACTIVITY;

/**
 * Created by Itai on 03/05/2017.
 */
public class MyScheduledReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        Intent scheduledIntent = new Intent(context, MessageBox.class);
        scheduledIntent.putExtra(MESSAGE_BOX_START_ACTIVITY,intent.getStringExtra(MESSAGE_BOX_START_ACTIVITY));
        scheduledIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(scheduledIntent);
    }
}