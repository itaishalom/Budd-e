package com.shalom.itai.theservantexperience.Relations;

/**
 * Created by Itai on 30/04/2017.
 */

public abstract class RelationsStatus {
    String relationStatus;
    int RESPONSE_NUMBER;
    double probabilityNumber =0.0;
//    protected static RelationsStatus instance;
    int iconId;
    public int getIconId(){return iconId;}
    int maxValProgress;
    public String getRelationStatus(){return relationStatus;}
    public int getMaxValProgress(){return maxValProgress;}
    public int getResponseNumber(){return RESPONSE_NUMBER;}
    public double getProbabilityNumber(){return probabilityNumber;}
   // public static RelationsStatus getInstance();
}
