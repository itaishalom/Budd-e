package com.shalom.itai.theservantexperience.Relations;

/**
 * Created by Itai on 30/04/2017.
 */

public class Haters extends RelationsStatus {
    private Haters(){
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
