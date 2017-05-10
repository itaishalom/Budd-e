package com.shalom.itai.theservantexperience.Services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.ImageView;

import com.shalom.itai.theservantexperience.Activities.DancingActivity;
import com.shalom.itai.theservantexperience.Activities.FunActivity;
import com.shalom.itai.theservantexperience.Activities.MainActivity;
import com.shalom.itai.theservantexperience.Activities.SmsSendActivity;
import com.shalom.itai.theservantexperience.Activities.SpeechRecognitionActivity;
import com.shalom.itai.theservantexperience.GifImageView;
import com.shalom.itai.theservantexperience.R;
import com.shalom.itai.theservantexperience.Relations.RelationsFactory;
import com.shalom.itai.theservantexperience.Relations.RelationsStatus;
import com.shalom.itai.theservantexperience.Utils.Functions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;

import static com.shalom.itai.theservantexperience.Utils.Constants.*;
import static com.shalom.itai.theservantexperience.Utils.Functions.*;

/**
 * Created by Itai on 09/04/2017.
 */

public class BuggerService extends Service {
    private static boolean isServiceUP =false;
    public static boolean isMainActivityUp = false;
    public static boolean isFunActivityUp = false;
    public static boolean isLoginUp = false;

    public static boolean stopBugger = false;
  //  public static  Class[] Activities= new Class[]{SpeechRecognitionActivity.class, FunActivity.class ,DancingActivity.class, SmsSendActivity.class};
    public static int indexActive = 0;
    private static int SYSTEM_GlobalPoints = 10;     //TODO READ FROM CONF!
    private static BuggerService mInstance;
    private static RelationsStatus currentStatus;
    private int mStartId =-1;
    public Actions currentAction;
    public DayActions dayActions;
    public NightActions nightActions;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /** Called when the service is being created. */
    @Override
    public void onCreate() {
        super.onCreate();
        currentStatus = RelationsFactory.getRelationStatus(SYSTEM_GlobalPoints);

        mInstance = this;
        startService();
    }
    private void startService() {

        //    new CheckRunningActivity(getBaseContext()).start();
    }


    public  RelationsStatus getRelationsStatus()
    {
        return currentStatus;
    }

    public static BuggerService getInstance(){
        return mInstance;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
       /* SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        boolean isLocked = settings.getBoolean(IS_LOCKED, false);*/
        currentAction = DayActions.start(getApplicationContext(),0);
        isServiceUP= true;
        if (intent.getBooleanExtra("runMainActivity",false))
        {
            Intent startMainActivity = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(startMainActivity);
            mStartId = startId;
        }
        return Service.START_STICKY;
    }

    public static boolean getIsServiceUP()
    {
        return isServiceUP;
    }


    public static void setSYSTEM_GlobalPoints(int pts){
        SYSTEM_GlobalPoints = SYSTEM_GlobalPoints + pts;
        currentStatus = RelationsFactory.getRelationStatus(SYSTEM_GlobalPoints);
    }

    public static int getSYSTEM_GlobalPoints(){
        return SYSTEM_GlobalPoints;
    }



    @Override
    public boolean stopService(Intent name) {
      /*  this.unbug();
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();

      */
      currentAction.StopTimers();

    /*if(NightService.getInstance()!=null) {
        Intent down = (new Intent(getApplicationContext(), NightService.class));
        down.putExtra("IS_MY_STOP", true);

        NightService.getInstance().stopService(down);
    }
        if(MorningService.getInstance()!=null) {
            Intent down2 = (new Intent(getApplicationContext(), MorningService.class));
            down2.putExtra("IS_MY_STOP", true);
            MorningService.getInstance().stopService(down2);
        }
        */
        stopSelf(mStartId);

      return super.stopService(name);

    }

    private void initializeConstants() {

    }

    public void unLock()
    {
        ((DayActions)currentAction).unLock();
    }

    public void lock()
    {
        ((DayActions)currentAction).lock();
    }

    public void bug()
    {
        ((DayActions)currentAction).bug();
    }

    public void unbug()
    {
        ((DayActions)currentAction).unbug();
    }

    public void wakeUpJon() {
        currentAction.StopTimers();
        currentAction = DayActions.start(getApplicationContext(),0);

    }

    public void sendJonToSleep() {
        currentAction.StopTimers();
        currentAction = NightActions.start(getApplicationContext(),0);
    }

    public void sendJonToSleep(GifImageView gifImageView, ConstraintLayout mainLayout,ImageView chatImage) {
        this.sendJonToSleep();
        this.onRefresh(gifImageView, mainLayout, chatImage);
    }



    public void stopNoiseListener() {
        currentAction.StopTimers();
    }

    public void onRefresh(GifImageView gifImageView, ConstraintLayout mainLayout,ImageView chatImage){
        currentAction.setCustomMainActivity(gifImageView, mainLayout, chatImage);
    }

}

