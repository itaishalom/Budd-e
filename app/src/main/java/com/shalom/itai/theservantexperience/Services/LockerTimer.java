package com.shalom.itai.theservantexperience.Services;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

import com.shalom.itai.theservantexperience.Activities.LoginActivity;

import java.util.List;

import static com.shalom.itai.theservantexperience.Services.BuggerService.isLoginUp;
import static com.shalom.itai.theservantexperience.Utils.Functions.checkScreenAndLock;

/**
 * Created by Itai on 13/04/2017.
 */

public class LockerTimer extends ContextTimerTask {

    private boolean waitingToStart = false;
    LockerTimer(Context context) {
        super(context);
    }

    @Override
    public void run() {
        ActivityManager mActivityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> RunningTask = mActivityManager.getRunningTasks(1);
        ActivityManager.RunningTaskInfo ar = RunningTask.get(0);
        String activityOnTop=ar.topActivity.getClassName();


        if(!checkScreenAndLock(mContext))
            return;

        if(!activityOnTop.toLowerCase().contains("login"))
        {
            Intent intent = new Intent(this.mContext, LoginActivity.class);
            this.mContext.startActivity(intent);

            return;
        }

        if( !isLoginUp)
        {
            isLoginUp = true;
            Intent intent = new Intent(this.mContext, LoginActivity.class);

            this.mContext.startActivity(intent);
        }
    }
}
