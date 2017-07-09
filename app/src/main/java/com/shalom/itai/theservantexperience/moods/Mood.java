package com.shalom.itai.theservantexperience.moods;

import com.shalom.itai.theservantexperience.R;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Itai on 08/07/2017.
 */

public abstract class Mood {
    protected int mTopBackgroundColor = 0xFFF6B478;
    protected int mBackgroundId = R.drawable.optimistic_background;
    protected int mBuddeGif =R.drawable.optimistic_gif;
    protected int mBuddePng =R.drawable.optimistic_gif;
    public int getBackground(){
        return mBackgroundId;
    }
    public int getTopBackgroundColor(){
        return mTopBackgroundColor;
    }
    public int getGif(){
        return mBuddeGif;
    }
    public int getPng(){
        return mBuddePng;
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