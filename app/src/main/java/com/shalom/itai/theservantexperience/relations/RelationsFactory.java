package com.shalom.itai.theservantexperience.relations;

/**
 * Created by Itai on 30/04/2017.
 */

public class RelationsFactory {
    public static RelationsStatus getRelationStatus(int points)
    {
        if(points<10){
            return Haters.getInstance();
        }
        if(10<= points && points<20) {
            return Acquaintances.getInstance();
        }
        if(20<= points ) {//  if(10<= points && points<20) {
            return Friends.getInstance();
        }
 /*       if(20<=points) {
            return BestFriends.getInstance();
        }*/
        return null;
    }
}
