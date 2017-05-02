package com.shalom.itai.theservantexperience.Relations;

/**
 * Created by Itai on 30/04/2017.
 */

public class Friends extends RelationsStatus {
    private static Friends instance;
    private Friends(){
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
