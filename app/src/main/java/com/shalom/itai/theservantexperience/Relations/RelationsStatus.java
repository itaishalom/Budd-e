package com.shalom.itai.theservantexperience.Relations;

/**
 * Created by Itai on 30/04/2017.
 */

public abstract class RelationsStatus {
    protected String relationStatus;
    protected int RESPONSE_NUMBER;
//    protected static RelationsStatus instance;
    int iconId;
    public int getIconId(){return iconId;}
    protected  int maxValProgress;
    public String getRelationStatus(){return relationStatus;}
    public int getMaxValProgress(){return maxValProgress;}
    public int getResponseNumber(){return RESPONSE_NUMBER;}
   // public static RelationsStatus getInstance();
}
