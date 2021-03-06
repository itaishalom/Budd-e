package com.shalom.itai.theservantexperience.relations;

import com.shalom.itai.theservantexperience.R;

/**
 * Created by Itai on 30/04/2017.
 */

class Acquaintances extends RelationsStatus {
   private static Acquaintances instance;
    private Acquaintances(){
        iconId = R.drawable.rel_stranger;
        RESPONSE_NUMBER = 0;
        maxValProgress = 39;
        minValProgress = 20;
        mGradeFactor = 2;
        this.relationStatus = "Acquaintances";
    }



    public static RelationsStatus getInstance(){
        if(instance==null)
        {
            instance= new Acquaintances();
        }
        return instance;
    }

}
