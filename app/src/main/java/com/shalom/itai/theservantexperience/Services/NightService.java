/*
package com.shalom.itai.theservantexperience.Services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;


import com.shalom.itai.theservantexperience.Activities.MainActivity;
import com.shalom.itai.theservantexperience.R;

import java.util.Timer;

import static com.shalom.itai.theservantexperience.Utils.Constants.IS_LOCKED;
import static com.shalom.itai.theservantexperience.Utils.Constants.LISTEN_SOUND_INTERVAL;
import static com.shalom.itai.theservantexperience.Utils.Constants.PREFS_NAME;
import static com.shalom.itai.theservantexperience.Utils.Functions.throwRandom;

*/
/**
 * Created by Itai on 06/05/2017.
 *//*


public class NightService extends Service {
    private boolean isServiceUP;
    private int mId=1;
    private static NightService mInstance;
    private static Timer SoundListenTimer = new Timer();
    private int flag;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
       addNotification();
        isServiceUP= true;
        startService();
        return Service.START_STICKY;
    }

    private void startService()
    {
        startNoiseListener();
        */
/*
        currentStatus = RelationsFactory.getRelationStatus(SYSTEM_GlobalPoints);
        Calendar c = Calendar.getInstance();
        SYSTEM_oldDay = c.get(Calendar.DAY_OF_YEAR);
        SYSTEM_NUM_OF_CHATS_POINTS = thorowRandom(3,1);
        Functions.createJokes();
        *//*
//    new CheckRunningActivity(getBaseContext()).start();
    }

    public boolean stopService(Intent name) {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        isServiceUP = false;
        if(name.getBooleanExtra("IS_MY_STOP",false))
            this.stopSelf();
        return super.stopService(name);
    }
    public void onCreate() {
        super.onCreate();
        mInstance = this;

    }



    private void startNoiseListener() {
        SoundListenTimer = new Timer();
        SoundListenTimer.scheduleAtFixedRate(new ListenToNoiseTimerTask(this), 0, throwRandom(1,1)*LISTEN_SOUND_INTERVAL);
    }


    public static NightService getInstance(){
        return mInstance;
    }

    private void addNotification()
    {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.icon)
                        .setContentTitle("Jon is sleeping")
                        .setContentText("shh....")
                        .setOngoing(true);
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
        //Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        //   mBuilder.flags |= Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;
        mNotificationManager.notify(mId, mBuilder.build());
    }




    public void stopNoiseListener() {
        SoundListenTimer.cancel();
        SoundListenTimer.purge();
        BuggerService.getInstance().wakeUpJon();
    }
}
*/