package com.shalom.itai.theservantexperience.moods;

import com.shalom.itai.theservantexperience.R;

/**
 * Created by Itai on 08/07/2017.
 */

public class Excited extends Mood{
    private static Excited instance;


    private Excited(){
        mBackgroundId = R.drawable.excited_background;
        mTopBackgroundColor = "#fbed8d";
        mBuddePng =R.drawable.excited_png;
        mBuddeGif =R.drawable.excited_gif;
    }

    public static Mood getInstance(){
        if(instance==null)
        {
            instance = new Excited();
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