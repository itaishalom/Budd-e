package com.shalom.itai.theservantexperience.Services;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Looper;
import android.widget.Toast;

import com.shalom.itai.theservantexperience.Activities.TripActivity;

import java.util.List;

import static com.shalom.itai.theservantexperience.Services.BuggerService.indexActive;
import static com.shalom.itai.theservantexperience.Services.BuggerService.stopBugger;
import static com.shalom.itai.theservantexperience.Services.DayActions.Activities;
import static com.shalom.itai.theservantexperience.Utils.Functions.checkScreenAndLock;
import static com.shalom.itai.theservantexperience.Utils.Functions.getDistanceFromLatLonInKm;

/**
 * Created by Itai on 09/04/2017.
 */

public class TimerTrip extends ContextTimerTask {
    private double latDistance;
    private double lngDistance;
    private double startingDistance = -1;
    TimerTrip(Context context, double latDistance,double lngDistance) {
        super(context);
        this.latDistance= latDistance;
        this.lngDistance= lngDistance;

    }



    @Override
    public void run() {

        ActivityManager mActivityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> RunningTask = mActivityManager.getRunningTasks(1);
        ActivityManager.RunningTaskInfo ar = RunningTask.get(0);
        String activityOnTop=ar.topActivity.getClassName();

        if(!checkScreenAndLock(mContext))
            return;
        Location lc = TripActivity.getInstance().getCurrentLocation();

        double distanceFromDestination = getDistanceFromLatLonInKm(latDistance,lngDistance, lc.getLatitude(),lc.getLongitude());
        if(startingDistance == -1){
            startingDistance = distanceFromDestination;
        }else{
            if(startingDistance> distanceFromDestination){
                TripActivity.getInstance().notGettingCloser();
            }else if(Math.abs(distanceFromDestination)<1){
                TripActivity.getInstance().weAreThere();
            }
        }

    }
}
