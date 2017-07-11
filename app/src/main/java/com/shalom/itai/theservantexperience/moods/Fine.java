package com.shalom.itai.theservantexperience.moods;

import com.shalom.itai.theservantexperience.R;

/**
 * Created by Itai on 08/07/2017.
 */

public class Fine extends Mood{
    private static Fine instance;

    private Fine(){
        mBackgroundId = R.drawable.fine_background;
        mTopBackgroundColor = "#fedd29";
        mBuddePng =R.drawable.fine_png;
        mBuddeGif  =R.drawable.fine_png;
    }


    public static Mood getInstance(){
        if(instance==null)
        {
            instance = new Fine();
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