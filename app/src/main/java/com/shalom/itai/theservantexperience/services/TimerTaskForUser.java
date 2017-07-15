package com.shalom.itai.theservantexperience.services;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import com.shalom.itai.theservantexperience.MessageBox;
import com.shalom.itai.theservantexperience.R;
import com.shalom.itai.theservantexperience.activities.Main2Activity;
import com.shalom.itai.theservantexperience.utils.Functions;

import java.util.List;

import static com.shalom.itai.theservantexperience.services.DayActions.Activities;
import static com.shalom.itai.theservantexperience.utils.Constants.BUG_INDEX;
import static com.shalom.itai.theservantexperience.utils.Constants.FORCED_CLOSED;
import static com.shalom.itai.theservantexperience.utils.Constants.PREFS_NAME;
import static com.shalom.itai.theservantexperience.utils.Constants.SETTINGS_CALLED_MAIN_ONCE;
import static com.shalom.itai.theservantexperience.utils.Constants.SETTINGS_INITIAL_TIRED_POINTS;
import static com.shalom.itai.theservantexperience.utils.Constants.SETTINGS_TIRED_POINTS;
import static com.shalom.itai.theservantexperience.utils.Functions.checkScreenAndLock;

/**
 * Created by Itai on 09/04/2017.
 */

class TimerTaskForUser extends ContextTimerTask {

    TimerTaskForUser(Context context) {
        super(context);
    }

    private boolean wakingUpLogic(float tired) {

        if (tired < 20 && tired > 5) {
            if (Functions.throwRandom(4, 1) >= 3) {
                //       BuggerService.getInstance().sendJonToSleep();
                return true;
            }
        }
        if (tired <= 5 && tired > 1) {
            if (Functions.throwRandom(4, 2) >= 3) {
                //     BuggerService.getInstance().sendJonToSleep();
                return true;
            }
        }
        if (tired <= 1) {
            //   BuggerService.getInstance().sendJonToSleep();
            return true;
        }
        return false;
    }

    @Override
    public void run() {
        SharedPreferences settings = mContext.getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        int tired = settings.getInt(SETTINGS_TIRED_POINTS, SETTINGS_INITIAL_TIRED_POINTS);
        boolean shouldSleep = wakingUpLogic(tired);
        if (tired > 0)
            Functions.writeToSettings(SETTINGS_TIRED_POINTS, tired - 1, mContext);

        ActivityManager mActivityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> RunningTask = mActivityManager.getRunningTasks(1);
        ActivityManager.RunningTaskInfo ar = RunningTask.get(0);
        String activityOnTop = ar.topActivity.getClassName();
        if (activityOnTop.contains("theservant") && activityOnTop.contains("MessageBox")) {
            return;
        }
        /*
        if (!checkScreenAndLock(mContext)) {
           playSound();
            if (shouldSleep) {
                BuggerService.getInstance().sendJonToSleep();
                Intent intent = new Intent(this.mContext, Main2Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.mContext.startActivity(intent);
                return;
            }else{
                boolean isCalled = settings.getBoolean(SETTINGS_CALLED_MAIN_ONCE, false);
                if (!isCalled) {
                    Functions.writeToSettings(SETTINGS_CALLED_MAIN_ONCE,true,mContext);
                    Intent intent = new Intent(this.mContext, Main2Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    this.mContext.startActivity(intent);
                }
                return;
            }
        }
        */
        if(settings.getBoolean(FORCED_CLOSED, false)){
            Functions.writeToSettings(FORCED_CLOSED,false,mContext.getApplicationContext());
            return;
        }
        if (!activityOnTop.contains("theservant") && !activityOnTop.contains("voicesearch") && !activityOnTop.contains("RECOGNIZE_SPEECH")
                && !activityOnTop.toLowerCase().contains("grantpermissionsactivity")
                && !activityOnTop.toLowerCase().contains("camera")
                && !activityOnTop.toLowerCase().contains("com.google.android.location.settings.LocationSettingsCheckerActivity".toLowerCase())
                && !activityOnTop.toLowerCase().contains("com.android.internal.app.ChooserActivity".toLowerCase())
                && !activityOnTop.toLowerCase().contains("AppWriteSettingsActivity".toLowerCase())
                 || !checkScreenAndLock(mContext)){ //"com.google.android.location.settings.LocationSettingsCheckerActivity"
            playSound();
            if(!checkScreenAndLock(mContext)) {
                if (activityOnTop.contains("theservant")) {
                        return;
                }
            }
                                /*
                if (indexActive == -1) {
                    Functions.popUpMessage(mContext, "I am board!", "ChatActivity");
                } else if (indexActive == -2) {
                 //   BuggerService.getInstance().sendJonToSleep();
                    Intent intent = new Intent(this.mContext, MainActivity.class).putExtra("sendJonToSleep",true);
                    this.mContext.startActivity(intent);
                } else {
                */
            if (shouldSleep) {

                BuggerService.getInstance().sendJonToSleep();
                Intent intent = new Intent(this.mContext, Main2Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.mContext.startActivity(intent);
                return;
            }
            int indexActive = settings.getInt(BUG_INDEX, 0);
            if (indexActive == Activities.length)
                indexActive = 0;
            Intent intent = new Intent(this.mContext, Activities[indexActive]);


            Functions.writeToSettings(BUG_INDEX, indexActive + 1, mContext);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.mContext.startActivity(intent);
        }
    }

    private void playSound(){
        MediaPlayer mediaPlayer = MediaPlayer.create(mContext, R.raw.ring_message);
        AudioManager am =
                (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        am.setStreamVolume(
                AudioManager.STREAM_MUSIC,
                am.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                0);
        mediaPlayer.start();
    }

}
