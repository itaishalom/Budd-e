package com.shalom.itai.theservantexperience.services;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.shalom.itai.theservantexperience.activities.BoardActivity;
import com.shalom.itai.theservantexperience.activities.DancingActivity;
import com.shalom.itai.theservantexperience.activities.FunActivity;
import com.shalom.itai.theservantexperience.activities.IncomingCallActivity;
import com.shalom.itai.theservantexperience.activities.MainActivity;
import com.shalom.itai.theservantexperience.activities.MatchesGameActivity;
import com.shalom.itai.theservantexperience.activities.SelfieV2;
import com.shalom.itai.theservantexperience.activities.SmsSendActivity;
import com.shalom.itai.theservantexperience.activities.SpeechRecognitionActivity;
import com.shalom.itai.theservantexperience.activities.TripActivity;
import com.shalom.itai.theservantexperience.chatBot.ChatActivity;
import com.shalom.itai.theservantexperience.R;
import com.shalom.itai.theservantexperience.utils.ClientActivity;
import com.shalom.itai.theservantexperience.utils.Functions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;

import pl.droidsonroids.gif.GifImageView;

import static com.shalom.itai.theservantexperience.utils.Constants.BUG_WAIT_TIME;
import static com.shalom.itai.theservantexperience.utils.Constants.CHAT_START_MESSAGE;
import static com.shalom.itai.theservantexperience.utils.Constants.IS_LOCKED;
import static com.shalom.itai.theservantexperience.utils.Constants.LOCATION_DISTNCE_CHECK;
import static com.shalom.itai.theservantexperience.utils.Constants.LOCK_WAIT_TIME;
import static com.shalom.itai.theservantexperience.utils.Constants.MINUTE;
import static com.shalom.itai.theservantexperience.utils.Constants.PREFS_NAME;
import static com.shalom.itai.theservantexperience.utils.Functions.throwRandom;

/**
 * Created by Itai on 10/05/2017.
 */

public class DayActions extends Actions {
    public static ArrayList<String> allInsults;
    public static ArrayList<String> allJokes;
    public static ArrayList<String> allFacts;

    private static boolean stopBugger = false;
    public static  Class[] Activities= new Class[]{ClientActivity.class,IncomingCallActivity.class, BoardActivity.class ,MatchesGameActivity.class,SelfieV2.class,FunActivity.class,TripActivity.class, SpeechRecognitionActivity.class ,DancingActivity.class, SmsSendActivity.class};
    public static int indexActive = 0;
    private int mId=0;
    public static int SYSTEM_oldDay;
    public static int SYSTEM_CURRENT_NUM_OF_CHATS_POINTS;
    public static int SYSTEM_NUM_OF_CHATS_POINTS;
    private static Timer timerBugger;
    private static Timer timerLock;
    private static Timer timerTrip;
    private static Timer moodTimer;

    private static DayActions instance = null;

    private DayActions(Context context,int icon_id){
        super(context,"Jon is here","Thinking about something..");
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
        instance.StartTimers(context);
  //     instance.addNotification("Jon is here","Thinking about something..",context);
        return instance;
    }

    @Override
    protected void StartTimers(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        boolean isLocked = settings.getBoolean(IS_LOCKED, false);
        if(isLocked) {
            this.lock(context);
        }
        else
        {
            this.bug(context);
            this.checkStatus(context);
        }
    }

    public void goToTrip(double lat, double lng,Context context){
        unbug();
        unLock();
        timerTrip = new Timer();
        timerTrip.scheduleAtFixedRate(new TimerTrip(context,lat,lng), 0, LOCATION_DISTNCE_CHECK);
    }

    public void unTrip(Context context){
        if(timerTrip != null) {
            timerTrip.cancel();
            timerTrip.purge();
        }
      //  this.bug(context);
    }

    public void lock(Context context) {
        if(timerLock != null) {
            timerLock = new Timer();
            timerLock.scheduleAtFixedRate(new LockerTimer(context), 0, LOCK_WAIT_TIME);
        }
    }

    public void unLock() {
        if(timerLock != null) {
            timerLock.cancel();
            timerLock.purge();
        }
    }

    public  void bug(Context context) {
        stopBugger = false;
        timerBugger = new Timer();
        timerBugger.scheduleAtFixedRate(new TimerTaskForUser(context), 0, BUG_WAIT_TIME);
    }

    public void unbug() {
        if(timerBugger != null) {
            timerBugger.cancel();
            timerBugger.purge();
            stopBugger = true;
        }
    }

    private void checkStatus(Context context){
        moodTimer = new Timer();
        moodTimer.scheduleAtFixedRate(new MoodTimer(context), 0, MINUTE * throwRandom(30,15));
    }
    public void restartCheckStatus(Context context){
        this.unCheckStatus();
        this.checkStatus(context);
    }

    private void unCheckStatus(){
        if(moodTimer != null) {
            moodTimer.cancel();
            moodTimer.purge();
        }
    }

    @Override
    public void StopTimers(Context context) {
        this.unbug();
        this.unLock();
        this.unCheckStatus();
        removeNotification(context);
    }

    @Override
    public void setCustomMainActivity(GifImageView gifImageView, ConstraintLayout mainLayout, ImageView chatImage, final AppCompatActivity activity) {
        gifImageView.setImageResource(R.drawable.jon_blinks);
        ((MainActivity)activity).mHurtButton.setVisibility(View.VISIBLE);
        /*Toast.makeText(mContext.getApplicationContext(), "Morning!",
                Toast.LENGTH_SHORT).show();*/
        mainLayout.setBackgroundColor(Color.parseColor("#04967D"));
        chatImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                activity.startActivity(new Intent(activity,
                        ChatActivity.class).putExtra(CHAT_START_MESSAGE,
                        "Jon is here"));
               // MainActivity.getInstance().overridePendingTransition(R.anim.slide_bottom_in, R.anim.slide_top_out);
                activity.overridePendingTransition(R.anim.slide_bottom_in, R.anim.slide_top_out);
            }
        });
    }
}
