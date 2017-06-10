package com.shalom.itai.theservantexperience.Relations;

import com.shalom.itai.theservantexperience.R;

/**
 * Created by Itai on 30/04/2017.
 */

public class Friends extends RelationsStatus {
    private static Friends instance;
    private Friends(){
        probabilityNumber = 0.2;
        maxValProgress = 30;
        RESPONSE_NUMBER = 2;
        iconId = R.drawable.rel_friend;
        this.relationStatus ="Friends";
    }
    public static RelationsStatus getInstance(){
        if(instance==null)
        {
            instance= new Friends();
        }
        return instance;
    }
}
