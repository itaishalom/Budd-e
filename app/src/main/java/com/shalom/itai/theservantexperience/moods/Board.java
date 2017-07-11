package com.shalom.itai.theservantexperience.moods;

import com.shalom.itai.theservantexperience.R;

/**
 * Created by Itai on 08/07/2017.
 */

public class Board extends Mood{
    private static Board instance;

    private Board(){
        mBackgroundId = R.drawable.board_background;
        mTopBackgroundColor = "#f6b57a";
        mBuddePng =R.drawable.board_png;
        mBuddeGif =R.drawable.board_png;
    }

    public static Mood getInstance(){
        if(instance==null) {
            instance = new Board();
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