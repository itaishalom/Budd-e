package com.shalom.itai.theservantexperience.moods;

import com.shalom.itai.theservantexperience.R;

/**
 * Created by Itai on 08/07/2017.
 */

public class Happy extends Mood{
    private static Happy instance;

    private Happy(){
        mBackgroundId = R.drawable.happy_background;
        mTopBackgroundColor = "#faf2b4";
        mBuddePng =R.drawable.happy_png;
        mBuddeGif = R.drawable.happy_gif;
    }


    public static Mood getInstance(){
        if(instance==null)
        {
            instance = new Happy();
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