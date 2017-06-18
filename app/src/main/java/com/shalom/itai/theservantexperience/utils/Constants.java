package com.shalom.itai.theservantexperience.utils;

/**
 * Created by Itai on 17/04/2017.
 */

public class Constants {
    public static final int BUG_WAIT_TIME = 10000;
    public static final int LOCATION_DISTNCE_CHECK = 5000;
    public static final int LOCK_WAIT_TIME = 500;
    public static final int SHOW_IMSULT_TIME = 7000;
    public static final String PREFS_NAME = "MySettings";
    public static final String IS_INSTALLED = "isInstalled";
    public static final String IS_LOCKED = "isLocked";
    public static final String CHAT_START_MESSAGE = "startConversation";
    public static final String CHAT_QUICK_REPLY = "answerToConversation";
    public static final String MESSAGE_BOX_START_ACTIVITY = "StartActivityFromPopUp";
    public static final int LISTEN_SOUND_INTERVAL = 1000;
    public static final int SECOND = 1000;
    public static final int MINUTE = 60000;
    public static final String IMAGE_BYTE_ARRAY = "imageByteArray";

    public static class JonIntents{
        public static final String UPD_BUG_RUN_TUT = "runTutorial";
        public static final String UPD_BUG_RUN_MAIN = "runMain";
        public static final String ACTION_MAIN_SET_NOTIFICATION = "ACTION_MAIN_SET_NOTIFICATION";

    }



    public static String USER_NAME ;// "imageByteArray";
    public static final String SAVE_IMAGE =  "toSaveImage";
    public static final String IMAGE_READY = "android.intent.action.ImageReady";
    public static final int INITIAL_POINTS = 10;
    public static String Directory ;
    
    ////SETTINGS:
    public static final String SETTINGS_POINTS = "GLOBAL_POINTS";
    public static final String SETTINGS_NAME = "SETTINGS_NAME";
    public static final String SETTINGS_IS_ASLEEP = "SETTINGS_SLEEP";
    public static final String SETTINGS_INSULTS = "SETTINGS_INSULTS";
    public static final String SETTINGS_BLESSES = "SETTINGS_BLESSES";
    public static final String SETTING_USERNAME = "SETTING_USERNAME";
    public static final String SETTING_SHOW_EXPLAIN_GAME = "SETTING_SHOW_EXPLAIN_GAME";
}
