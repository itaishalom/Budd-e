package com.shalom.itai.theservantexperience.moods;

import com.shalom.itai.theservantexperience.R;

/**
 * Created by Itai on 08/07/2017.
 */

public class Optimistic extends Mood{
    private static Optimistic instance;

    private Optimistic(){
        mBackgroundId = R.drawable.optimistic_background;
        mBuddeGif = R.drawable.optimistic_png;
        mBuddePng = R.drawable.optimistic_png;
        mTopBackgroundColor = "#f4b47a";
    }

    public static Mood getInstance(){
        if(instance==null) {
            instance = new Optimistic();
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