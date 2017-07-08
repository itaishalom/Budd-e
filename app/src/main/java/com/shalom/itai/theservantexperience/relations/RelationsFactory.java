package com.shalom.itai.theservantexperience.relations;

/**
 * Created by Itai on 30/04/2017.
 */

public class RelationsFactory {
    public static RelationsStatus getRelationStatus(int points) {
        if (points <= Haters.getInstance().getMaxValProgress()) {
            return Haters.getInstance();
        }
        if (Acquaintances.getInstance().getMinValProgress() <= points && points <= Acquaintances.getInstance().getMaxValProgress()) {
            return Acquaintances.getInstance();
        }
        if (Friends.getInstance().getMinValProgress() <= points) {//  if(10<= points && points<20) {
            return Friends.getInstance();
        }

        return null;
    }
}
