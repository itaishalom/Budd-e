package com.shalom.itai.theservantexperience.Relations;

/**
 * Created by Itai on 30/04/2017.
 */

public class Strangers extends RelationsStatus {
   //private static Strangers instance;
    private Strangers(){
        this.relationStatus = "Strangers";
    }

    public static RelationsStatus getInstance(){
        if(instance==null)
        {
            instance= new Strangers();
        }
        return instance;
    }

}
