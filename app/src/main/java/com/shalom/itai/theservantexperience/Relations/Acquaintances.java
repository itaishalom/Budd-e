package com.shalom.itai.theservantexperience.Relations;

import com.shalom.itai.theservantexperience.R;

/**
 * Created by Itai on 30/04/2017.
 */

public class Acquaintances extends RelationsStatus {
   private static Acquaintances instance;
    private Acquaintances(){
        iconId = R.drawable.rel_stranger;
        RESPONSE_NUMBER = 0;
        maxValProgress = 20;
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
