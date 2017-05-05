package com.shalom.itai.theservantexperience.Relations;

import com.shalom.itai.theservantexperience.R;

/**
 * Created by Itai on 30/04/2017.
 */

public class Haters extends RelationsStatus {
    private static Haters instance;
    private Haters(){
        iconId = R.drawable.favorite_outline_black;
        RESPONSE_NUMBER = 1;
        maxValProgress = 10;
        this.relationStatus = "Haters";
    }

    public static RelationsStatus getInstance(){
        if(instance==null)
        {
            instance = new Haters();
        }
        return instance;
    }
}
