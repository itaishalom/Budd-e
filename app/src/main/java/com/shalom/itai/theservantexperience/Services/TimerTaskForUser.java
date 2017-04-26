package com.shalom.itai.theservantexperience.Services;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.shalom.itai.theservantexperience.Activities.DancingActivity;
import com.shalom.itai.theservantexperience.Activities.FunActivity;
import com.shalom.itai.theservantexperience.Activities.MainActivity;
import com.shalom.itai.theservantexperience.Activities.SmsSendActivity;
import com.shalom.itai.theservantexperience.Activities.SpeechRecognitionActivity;

import java.util.List;

import static com.shalom.itai.theservantexperience.Services.BuggerService.stopBugger;

/**
 * Created by Itai on 09/04/2017.
 */

public class TimerTaskForUser extends ContextTimerTask {
    Class[] Activities;
    int indexActive = 0;
    TimerTaskForUser (Context context) {
        super(context);
        Activities= new Class[]{SpeechRecognitionActivity.class, FunActivity.class ,DancingActivity.class, SmsSendActivity.class};

    }



    @Override
    public void run() {
        ActivityManager mActivityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> RunningTask = mActivityManager.getRunningTasks(1);
        ActivityManager.RunningTaskInfo ar = RunningTask.get(0);
        String activityOnTop=ar.topActivity.getClassName();
/*
        if(BuggerService.pathToLastImage !="")
        {
            FunActivity.getInstance().continueAnalyze();
        }*/
//

        if(!activityOnTop.contains("theservant") && !activityOnTop.contains("voicesearch") && !activityOnTop.contains("RECOGNIZE_SPEECH") && !activityOnTop.toLowerCase().contains("grantpermissionsactivity") && !activityOnTop.toLowerCase().contains("camera"))

        // if(!BuggerService.isFunActivityUp && !BuggerService.isMainActivityUp)
        {
            if(!stopBugger){
                Intent intent = new Intent(this.mContext, Activities[indexActive]);
                indexActive++;
                if (indexActive==Activities.length)
                    indexActive = 0;
                this.mContext.startActivity(intent);}
        }
    }
}
