package com.shalom.itai.theservantexperience.Services;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import com.shalom.itai.theservantexperience.Activities.FunActivity;

import java.util.List;

import static com.shalom.itai.theservantexperience.Services.BuggerService.stopBugger;

/**
 * Created by Itai on 09/04/2017.
 */

public class TimerTaskForUser extends ContextTimerTask {

    TimerTaskForUser (Context context) {
        super(context);
    }
    @Override
    public void run() {
        ActivityManager mActivityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> RunningTask = mActivityManager.getRunningTasks(1);
        ActivityManager.RunningTaskInfo ar = RunningTask.get(0);
        String activityOnTop=ar.topActivity.getClassName();

        if(!activityOnTop.contains("theservant") && !activityOnTop.toLowerCase().contains("grantpermissionsactivity"))

        // if(!BuggerService.isFunActivityUp && !BuggerService.isMainActivityUp)
        {
            if(!stopBugger){
            Intent intent = new Intent(this.mContext, FunActivity.class);
            this.mContext.startActivity(intent);}
        }
    }
}
