package com.shalom.itai.theservantexperience.moods;

import com.shalom.itai.theservantexperience.R;

/**
 * Created by Itai on 08/07/2017.
 */

public class Angry extends Mood{
    private static Angry instance;

    private Angry(){
        mBackgroundId = R.drawable.angrey_background;
        mTopBackgroundColor = 0xFF471817;
    }

    public static Mood getInstance(){
        if(instance==null) {
            instance = new Angry();
        }
        return instance;
    }
}

//
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