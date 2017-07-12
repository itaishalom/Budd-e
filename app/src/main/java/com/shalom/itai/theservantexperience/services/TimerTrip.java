package com.shalom.itai.theservantexperience.services;

import android.app.ActivityManager;
import android.content.Context;
import android.location.Location;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.shalom.itai.theservantexperience.R;
import com.shalom.itai.theservantexperience.activities.TripActivity;

import java.util.List;

import static com.shalom.itai.theservantexperience.utils.Functions.checkScreenAndLock;
import static com.shalom.itai.theservantexperience.utils.Functions.getDistanceFromLatLonInKm;

/**
 * Created by Itai on 09/04/2017.
 */

class TimerTrip extends ContextTimerTask {
    private double latDistance;
    private double lngDistance;
    private double startingDistance = -1;
    TimerTrip(Context context, double latDistance,double lngDistance) {
        super(context);
        this.latDistance= latDistance;
        this.lngDistance= lngDistance;

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

    @Override
    public void run() {

        ActivityManager mActivityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> RunningTask = mActivityManager.getRunningTasks(1);
        ActivityManager.RunningTaskInfo ar = RunningTask.get(0);
      //  String activityOnTop=ar.topActivity.getClassName();
        playSound();
        if(!checkScreenAndLock(mContext))
            return;
        Location lc = TripActivity.getInstance().getCurrentLocation();

        double distanceFromDestination = getDistanceFromLatLonInKm(latDistance,lngDistance, lc.getLatitude(),lc.getLongitude());
        if(startingDistance == -1){
            startingDistance = distanceFromDestination;
        }else{
            //distanceFromDestination = 0.001;
            if(startingDistance> distanceFromDestination){
                TripActivity.getInstance().notGettingCloser();
            }else if(Math.abs(distanceFromDestination)<0.01){
                TripActivity.getInstance().weAreThere();
            }
        }

    }
}
