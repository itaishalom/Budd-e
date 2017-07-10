package com.shalom.itai.theservantexperience.services;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.shalom.itai.theservantexperience.activities.Main2Activity;
import com.shalom.itai.theservantexperience.activities.MainActivity;
import pl.droidsonroids.gif.GifImageView;
import com.shalom.itai.theservantexperience.R;
import com.shalom.itai.theservantexperience.utils.Constants;

import java.util.Timer;

/**
 * Created by Itai on 10/05/2017.
 */

public class NightActions extends Actions {
    private static Timer SoundListenTimer = new Timer();
    // --Commented out by Inspection (18/06/2017 00:17):private static boolean isUp=false;
    private static NightActions instance = null;
    private static double noiseLevel = 75.0;

    public static Actions start(Context context,int notif_icon) {
        if(instance == null){
            instance = new NightActions(context,notif_icon);
        }
       instance.StartTimers(context);
       // instance.addNotification("Jon is sleeping","shh....");
        return instance;
    }

    private NightActions(Context context,int icon_id){
        super(context, Constants.ENTITY_NAME+" is sleeping","shh....");
        mNotification_id = 1;
        mNotification_icon = icon_id;
        IS_AWAKE = false;
    }

    @Override
    public void setCustomMainActivity(GifImageView gifImageView, ConstraintLayout mainLayout, ImageView chatImage, final AppCompatActivity activity) {
       gifImageView.setImageResource(R.drawable.jon_sleeping);
        ((MainActivity)activity).mHurtButton.setVisibility(View.INVISIBLE);
        mainLayout.setBackgroundColor(Color.parseColor("#234D6E"));
        chatImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((MainActivity)activity).showDialog();
            }
        });
    }

    @Override
    public void setCustomMainActivity2(GifImageView gifImageView, ConstraintLayout mainLayout, ImageButton chatImage, final AppCompatActivity activity) {
        chatImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
         //       ((Main2Activity)activity).showDialog();
            }
        });
    }


    public static double getNoiseLevel(){
        return noiseLevel;
    }

    public static void IncNoiseLevel(){
        noiseLevel+=5;
    }

    @Override
    protected void StartTimers(Context context) {
        SoundListenTimer = new Timer();
        SoundListenTimer.scheduleAtFixedRate(new ListenToNoiseTimerTask(context), 0, 7000);
    }

    @Override
    public void StopTimers(Context context) {
        SoundListenTimer.cancel();
        SoundListenTimer.purge();
        removeNotification(context);
    }

}
