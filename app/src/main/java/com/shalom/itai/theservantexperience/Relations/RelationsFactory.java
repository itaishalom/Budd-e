package com.shalom.itai.theservantexperience.Relations;

/**
 * Created by Itai on 30/04/2017.
 */

public class RelationsFactory {
    public RelationsStatus getRelationStatus(int points)
    {
        if(points<0){
            return new Haters();
        }
        if(0<= points && points<10) {
            return new Strangers();
        }
        if(10<= points && points<20) {
            return new Friends();
        }
        if(20<=points) {
            return new Friends();
        }
        return null;
    }
}
