package com.shalom.itai.theservantexperience.Services;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.shalom.itai.theservantexperience.R;
import com.shalom.itai.theservantexperience.Utils.Functions;

import java.util.ArrayList;
import java.util.Timer;

import static com.shalom.itai.theservantexperience.Utils.Constants.*;

/**
 * Created by Itai on 09/04/2017.
 */

public class BuggerService extends Service {
    public static boolean isMainActivityUp = false;
    public static boolean isFunActivityUp = false;
    public static boolean isLoginUp = false;
    public static ArrayList<String> allInsults;
    public static ArrayList<String> allJokes;
    public static ArrayList<String> allFacts;
    private static Timer timerBugger = new Timer();
    private static Timer timerLock = new Timer();
    public static boolean stopBugger = false;
    private int mId=0;
    public static int GlobalPoints = 0;     //TODO READ FROM CONF!
    private static BuggerService mInstance;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /** Called when the service is being created. */
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        startService();
    }
    private void startService()
    {
        Functions.createJokes();
    //    new CheckRunningActivity(getBaseContext()).start();

    }

public static BuggerService getInstance(){
    return mInstance;
}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        addNotification();
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        boolean isLocked = settings.getBoolean(IS_LOCKED, false);
        if(isLocked) {
            this.lock();
        }
        else
        {
            this.bug();
        }
/*
            SharedPreferences.Editor editor = settings.edit();
           editor.putBoolean(IS_INSTALLED, true);
            editor.commit();
*/

        return Service.START_STICKY;
    }

    public  void lock()
    {
        timerLock = new Timer();
        timerLock.scheduleAtFixedRate(new LockerTimer(this), 0, LOCK_WAIT_TIME);
    }

    public void unLock() {
        timerLock.cancel();
        timerLock.purge();
    }
    public  void bug()
    {
        stopBugger = false;
        timerBugger = new Timer();
        timerBugger.scheduleAtFixedRate(new TimerTaskForUser(this), 0, BUG_WAIT_TIME);
    }

    public void unbug() {
        timerBugger.cancel();
        timerBugger.purge();
        stopBugger = true;

    }
    private void addNotification()
    {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.icon)
                        .setContentTitle("Jon is here")
                        .setContentText("Thinking about something....")
                        .setOngoing(true);
// Creates an explicit intent for an Activity in your app
      //  Intent resultIntent = new Intent(this, MainActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
   //     stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
    //    stackBuilder.addNextIntent(resultIntent);
        //PendingIntent resultPendingIntent =
    //            stackBuilder.getPendingIntent(
    //                    0,
      //                  PendingIntent.FLAG_UPDATE_CURRENT
      //          );
   //     mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
     //   mBuilder.flags |= Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;
        mNotificationManager.notify(mId, mBuilder.build());
    }


    @Override
    public boolean stopService(Intent name) {
        this.unbug();
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        return super.stopService(name);

    }

}

