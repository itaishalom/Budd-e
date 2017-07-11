package com.shalom.itai.theservantexperience.moods;

import com.shalom.itai.theservantexperience.R;

/**
 * Created by Itai on 08/07/2017.
 */

public class Calm extends Mood {
    private static Calm instance;

    private Calm(){
        mBackgroundId = R.drawable.calm_background;
        mTopBackgroundColor = "#f6b57a";
        mBuddePng =R.drawable.calm_png;
        mBuddeGif =R.drawable.calm_png;
    }

    public static Mood getInstance(){
        if(instance==null)
        {
            instance = new Calm();
        }
        return instance;
    }
}
