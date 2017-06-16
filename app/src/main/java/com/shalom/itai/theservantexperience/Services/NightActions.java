package com.shalom.itai.theservantexperience.Services;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;

import com.shalom.itai.theservantexperience.Activities.MainActivity;
import pl.droidsonroids.gif.GifImageView;
import com.shalom.itai.theservantexperience.R;

import java.util.Timer;

/**
 * Created by Itai on 10/05/2017.
 */

public class NightActions extends Actions {
    private static Timer SoundListenTimer = new Timer();
    private static boolean isUp=false;
    private static NightActions instance = null;
    private static double noiseLevel = 75.0;

    public static Actions start(Context context,int notif_icon) {
        if(instance==null){
            instance = new NightActions(context,notif_icon);
        }
        instance.StartTimers();
        instance.addNotification("Jon is sleeping","shh....");
        return instance;
    }

    private NightActions(Context context,int icon_id){
        super(context);
        mNotification_id = 1;
        mNotification_icon = icon_id;
        IS_AWAKE = false;
    }

    @Override
    public void setCustomMainActivity(GifImageView gifImageView, ConstraintLayout mainLayout,ImageView chatImage) {
        gifImageView.setImageResource(R.drawable.jon_sleeping);
     /*   Toast.makeText(mContext.getApplicationContext(), "Good night!",
                Toast.LENGTH_SHORT).show();*/
        mainLayout.setBackgroundColor(Color.parseColor("#234D6E"));
        chatImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MainActivity.getInstance().showDialog();
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
    protected void StartTimers() {
        SoundListenTimer = new Timer();
        SoundListenTimer.scheduleAtFixedRate(new ListenToNoiseTimerTask(mContext), 0, 7000);
    }

    @Override
    public void StopTimers() {
        SoundListenTimer.cancel();
        SoundListenTimer.purge();
        removeNotification();
    }

}
