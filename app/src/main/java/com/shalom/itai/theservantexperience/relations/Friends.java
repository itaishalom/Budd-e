package com.shalom.itai.theservantexperience.relations;

import com.shalom.itai.theservantexperience.R;

/**
 * Created by Itai on 30/04/2017.
 */

class Friends extends RelationsStatus {
    private static Friends instance;
    private Friends(){
        probabilityNumber = 0.2;
        maxValProgress = 60;
        minValProgress = 40;
        RESPONSE_NUMBER = 2;
        mGradeFactor = 4;
        iconId = R.drawable.rel_friend;
        this.relationStatus ="Friends";
    }
    public static RelationsStatus getInstance(){
        if(instance==null) {
            instance= new Friends();
        }
        return instance;
    }
}
