package com.shalom.itai.theservantexperience.moods;

import com.shalom.itai.theservantexperience.R;

/**
 * Created by Itai on 08/07/2017.
 */

public class Sleep extends Mood{
    private static Sleep instance;

    private Sleep(){
        mBackgroundId = R.drawable.sleep_background;
        mBuddeGif = R.drawable.sleep_gif;
        mBuddePng = R.drawable.sleep_png;
        mTopBackgroundColor = "#000000";
    }

    public static Mood getInstance(){
        if(instance==null)
        {
            instance = new Sleep();
        }
        return instance;
    }
}


/*
        Sad(0),
        Angry(1),
        Board(2),
        Calm(3),
        Fine(4),
        Optimistic(5),
        Happy(6),
        Excited(7);
 */