package com.shalom.itai.theservantexperience.moods;

/**
 * Created by Itai on 08/07/2017.
 */

public class Calm extends Mood {
    private static Calm instance;

    public static Mood getInstance(){
        if(instance==null)
        {
            instance = new Calm();
        }
        return instance;
    }
}
