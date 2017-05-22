package com.shalom.itai.theservantexperience.Services;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.shalom.itai.theservantexperience.Activities.DancingActivity;
import com.shalom.itai.theservantexperience.Activities.FunActivity;
import com.shalom.itai.theservantexperience.Activities.MainActivity;
import com.shalom.itai.theservantexperience.Activities.SmsSendActivity;
import com.shalom.itai.theservantexperience.Activities.SpeechRecognitionActivity;
import com.shalom.itai.theservantexperience.Activities.TripActivity;
import com.shalom.itai.theservantexperience.ChatBot.ChatActivity;
import com.shalom.itai.theservantexperience.GifImageView;
import com.shalom.itai.theservantexperience.R;
import com.shalom.itai.theservantexperience.Utils.Functions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;

import static com.shalom.itai.theservantexperience.Utils.Constants.BUG_WAIT_TIME;
import static com.shalom.itai.theservantexperience.Utils.Constants.CHAT_START_MESSAGE;
import static com.shalom.itai.theservantexperience.Utils.Constants.IS_LOCKED;
import static com.shalom.itai.theservantexperience.Utils.Constants.LOCATION_DISTNCE_CHECK;
import static com.shalom.itai.theservantexperience.Utils.Constants.LOCK_WAIT_TIME;
import static com.shalom.itai.theservantexperience.Utils.Constants.PREFS_NAME;
import static com.shalom.itai.theservantexperience.Utils.Functions.throwRandom;

/**
 * Created by Itai on 10/05/2017.
 */

public class DayActions extends Actions {
    public static ArrayList<String> allInsults;
    public static ArrayList<String> allJokes;
    public static ArrayList<String> allFacts;

    public static boolean stopBugger = false;
    public static  Class[] Activities= new Class[]{ TripActivity.class, FunActivity.class,SpeechRecognitionActivity.class ,DancingActivity.class, SmsSendActivity.class};
    public static int indexActive = 0;
    private int mId=0;
    public static int SYSTEM_oldDay;
    public static int SYSTEM_CURRENT_NUM_OF_CHATS_POINTS;
    public static int SYSTEM_NUM_OF_CHATS_POINTS;
    private static Timer timerBugger;
    private static Timer timerLock;
    private static Timer timerTrip;

    private static boolean isUp=false;
    private static DayActions instance = null;

    private DayActions(Context context,int icon_id){
        super(context);
        Calendar c = Calendar.getInstance();
        SYSTEM_CURRENT_NUM_OF_CHATS_POINTS=0;
        SYSTEM_oldDay = c.get(Calendar.DAY_OF_YEAR);
        SYSTEM_NUM_OF_CHATS_POINTS = throwRandom(3,1);
        Functions.createJokes();
        mNotification_id = 2;
        mNotification_icon = icon_id;
        IS_AWAKE = false;
    }

    public static Actions start(Context context, int icon_id) {
        if(instance==null){
            instance = new DayActions(context,icon_id);
        }
        instance.StartTimers();
        instance.addNotification("Jon is here","Thinking about something..");
        return instance;
    }

    @Override
    protected void StartTimers() {
        SharedPreferences settings = mContext.getSharedPreferences(PREFS_NAME, 0);
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
        isUp= true;
    }

    public void goToTrip(double lat, double lng){
        unbug();
        unLock();
        timerTrip = new Timer();
        timerTrip.scheduleAtFixedRate(new TimerTrip(mContext,lat,lng), 0, LOCATION_DISTNCE_CHECK);
    }

    public void unTrip(){
        if(timerTrip != null) {
            timerTrip.cancel();
            timerTrip.purge();
        }
        this.bug();
    }

    public void lock() {
        if(timerLock != null) {
            timerLock = new Timer();
            timerLock.scheduleAtFixedRate(new LockerTimer(mContext), 0, LOCK_WAIT_TIME);
        }
    }

    public void unLock() {
        if(timerLock != null) {
            timerLock.cancel();
            timerLock.purge();
        }
    }

    public  void bug() {
        stopBugger = false;
        timerBugger = new Timer();
        timerBugger.scheduleAtFixedRate(new TimerTaskForUser(mContext), 0, BUG_WAIT_TIME);
    }

    public void unbug() {
        if(timerBugger != null) {
            timerBugger.cancel();
            timerBugger.purge();
            stopBugger = true;
        }
    }




    @Override
    public void StopTimers() {
        this.unbug();
        this.unLock();
        removeNotification();
    }

    @Override
    public void setCustomMainActivity(GifImageView gifImageView, ConstraintLayout mainLayout,ImageView chatImage) {
        gifImageView.setGifImageResource(R.drawable.jon_blinks);
        Toast.makeText(mContext.getApplicationContext(), "Morning!",
                Toast.LENGTH_SHORT).show();
        mainLayout.setBackgroundColor(Color.parseColor("#04967D"));
        chatImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MainActivity.getInstance().startActivity(new Intent(MainActivity.getInstance(),
                        ChatActivity.class).putExtra(CHAT_START_MESSAGE,
                        "Jon is here"));
            }
        });
    }
}
