package com.shalom.itai.theservantexperience.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.shalom.itai.theservantexperience.activities.Main2Activity;
import com.shalom.itai.theservantexperience.activities.MainActivity;
import com.shalom.itai.theservantexperience.R;
import com.shalom.itai.theservantexperience.utils.Constants;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Itai on 10/05/2017.
 */

abstract class Actions {
    int mNotification_id;
    int mNotification_icon;
    boolean IS_AWAKE;
    private NotificationCompat.Builder mBuilder;
    private NotificationManager mNotificationManager;

    public boolean getIsAwake() {
        return IS_AWAKE;
    }

    public abstract void setCustomMainActivity(GifImageView gifImageView, ConstraintLayout mainLayout, ImageView chatImage, AppCompatActivity activity);
    public abstract void setCustomMainActivity2(GifImageView gifImageView, ConstraintLayout mainLayout, ImageButton chatImage, AppCompatActivity activity);
    Actions(Context context, String headLine, String info) {
        addNotification(headLine, info, context);
    }

    void addNotification() {
        if (mBuilder != null){
            mNotificationManager.notify(mNotification_id, mBuilder.build());
        }
    }
    private void addNotification(String headLine, String info, Context context) {
        mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.small_icon)  //R.drawable.icon
                        .setContentTitle(headLine)
                        .setContentText(info)
                        .setOngoing(true)
                        .setAutoCancel(false);
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(context, Main2Activity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        resultIntent.putExtra(Constants.JonIntents.ACTION_MAIN_SET_NOTIFICATION, true);
// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
// Adds the back stack for the Intent (but not the Intent itself)
        //      stackBuilder.addParentStack(MainActivity.class);
        //Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        //      mBuilder.setf |= Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;
        mNotificationManager.notify(mNotification_id, mBuilder.build());
    }


    protected abstract void StartTimers(Context context);

    public abstract void StopTimers(Context context);

    public void unLock() {
    }

    public void restartCheckStatus() {
    }

    void removeNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

}
