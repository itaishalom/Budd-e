package com.shalom.itai.theservantexperience.moods;

import com.shalom.itai.theservantexperience.R;

/**
 * Created by Itai on 08/07/2017.
 */

public class Sad extends Mood{
    private static Sad instance;

    private Sad(){
        mBackgroundId = R.drawable.sad_background;
        mBuddeGif = R.drawable.sad_png;
        mBuddePng = R.drawable.sad_png;
        mTopBackgroundColor = "#000000";
    }

    public static Mood getInstance(){
        if(instance==null)
        {
            instance = new Sad();
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