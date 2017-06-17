package com.shalom.itai.theservantexperience.ChatBot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import com.shalom.itai.theservantexperience.activities.MainActivity;
import com.shalom.itai.theservantexperience.MessageBox;

import static com.shalom.itai.theservantexperience.Utils.Constants.MESSAGE_BOX_START_ACTIVITY;

/**
 * Created by Itai on 03/05/2017.
 */
public class MyScheduledReceiver extends BroadcastReceiver {
    @SuppressWarnings("FieldCanBeLocal")
    private final String TAG = "MyScheduledReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getBooleanExtra("BirthDay",false)){
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
            wl.acquire();
            //TODO ADD sound!
            Intent scheduledIntent = new Intent(context, MainActivity.class);
            scheduledIntent.putExtra("Birthday",true);
            context.startActivity(scheduledIntent);
            Toast.makeText(context.getApplicationContext(), "Birthday!!", Toast.LENGTH_SHORT).show();
            wl.release();
            return;
        }


        Intent scheduledIntent = new Intent(context, MessageBox.class);
        String temp = intent.getStringExtra(MESSAGE_BOX_START_ACTIVITY);
        Log.d(TAG, "onReceive: " + temp);
        scheduledIntent.putExtra(MESSAGE_BOX_START_ACTIVITY,intent.getStringExtra(MESSAGE_BOX_START_ACTIVITY));
        scheduledIntent.putExtra("START_TEXT",intent.getStringExtra("START_TEXT"));
        scheduledIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(scheduledIntent);
    }
}