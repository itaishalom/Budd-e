package com.shalom.itai.theservantexperience.relations;

/**
 * Created by Itai on 30/04/2017.
 */

public abstract class RelationsStatus {
    String relationStatus;
    int RESPONSE_NUMBER;
    protected double probabilityNumber = 0.0;
    protected int mGradeFactor = 0;
    protected int maxValProgress;
    protected int minValProgress;

    protected int iconId;

    public int getIconId() {
        return iconId;
    }

    public int getGradeFactor(){
        return this.mGradeFactor;
    }

    public String getRelationStatus() {
        return relationStatus;
    }

    public int getMaxValProgress() {
        return maxValProgress;
    }

    public int getMinValProgress() {
        return minValProgress;
    }

    public int getResponseNumber() {
        return RESPONSE_NUMBER;
    }

    public double getProbabilityNumber() {
        return probabilityNumber;
    }
    // public static RelationsStatus getInstance();
}
