package com.shalom.itai.theservantexperience.Relations;

/**
 * Created by Itai on 30/04/2017.
 */

public class RelationsFactory {
    public static RelationsStatus getRelationStatus(int points)
    {
        if(points<0){
            return Haters.getInstance();
        }
        if(0<= points && points<10) {
            return Strangers.getInstance();
        }
        if(10<= points && points<20) {
            return Friends.getInstance();
        }
        if(20<=points) {
            return BestFriends.getInstance();
        }
        return null;
    }
}
