package com.shalom.itai.theservantexperience.services;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import com.shalom.itai.theservantexperience.activities.MainActivity;
import com.shalom.itai.theservantexperience.utils.Functions;

import java.util.List;

import static com.shalom.itai.theservantexperience.services.BuggerService.indexActive;
import static com.shalom.itai.theservantexperience.services.BuggerService.stopBugger;
import static com.shalom.itai.theservantexperience.services.DayActions.Activities;
import static com.shalom.itai.theservantexperience.utils.Functions.checkScreenAndLock;

/**
 * Created by Itai on 09/04/2017.
 */

class TimerTaskForUser extends ContextTimerTask {

    TimerTaskForUser(Context context) {
        super(context);
    }

    @Override
    public void run() {
        ActivityManager mActivityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> RunningTask = mActivityManager.getRunningTasks(1);
        ActivityManager.RunningTaskInfo ar = RunningTask.get(0);
        String activityOnTop = ar.topActivity.getClassName();

        if (!checkScreenAndLock(mContext))
            return;

        if (!activityOnTop.contains("theservant") && !activityOnTop.contains("voicesearch") && !activityOnTop.contains("RECOGNIZE_SPEECH")
                && !activityOnTop.toLowerCase().contains("grantpermissionsactivity")
                && !activityOnTop.toLowerCase().contains("camera")
                && !activityOnTop.toLowerCase().contains("com.google.android.location.settings.LocationSettingsCheckerActivity".toLowerCase())
                && !activityOnTop.toLowerCase().contains("com.android.internal.app.ChooserActivity".toLowerCase())
                && !activityOnTop.toLowerCase().contains("AppWriteSettingsActivity".toLowerCase())) { //"com.google.android.location.settings.LocationSettingsCheckerActivity"
            if (!stopBugger) {
                if (indexActive == -1) {
                    Functions.popUpMessage(mContext, "I am board!", "ChatActivity");
                } else if (indexActive == -2) {
                 //   BuggerService.getInstance().sendJonToSleep();
                    Intent intent = new Intent(this.mContext, MainActivity.class).putExtra("sendJonToSleep",true);
                    this.mContext.startActivity(intent);
                } else {
                    Intent intent = new Intent(this.mContext, Activities[indexActive]);
                    this.mContext.startActivity(intent);
                }
                indexActive++;
                if (indexActive == Activities.length)
                    indexActive = -2;
            }
        }
    }
}
