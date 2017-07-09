package com.shalom.itai.theservantexperience.utils;

/**
 * Created by Itai on 17/04/2017.
 */

public class Constants {
    public static final String ENTITY_NAME = "Budd-e";
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
    public static final String LOG_SEPARATOR = " <> ";

    public static class JonIntents{
        public static final String UPD_BUG_RUN_TUT = "runTutorial";
        public static final String UPD_BUG_RUN_MAIN = "runMain";
        public static final String ACTION_MAIN_SET_NOTIFICATION = "ACTION_MAIN_SET_NOTIFICATION";
        public static final String INPUT_TO_SPLASH_CLASS_NAME = "CLASS_NAME";
        public static final String DONE_CALENDAR = "DONE_CALENDAR";
        public static final String ASK_TO_PLAY = "ASK_TO_PLAY";
    }

    public enum Status {
        Bad(0),
        OK(1),
        Fine(2),
        Good(3),
        Awesome(4),
        Excellent(5);
        private final int value;
        private Status(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }

/*    public enum Mood {
        Sad(0),
        Angry(1),
        Board(2),
        Calm(3),
        Fine(4),
        Optimistic(5),
        Happy(6),
        Excited(7);
        private final int value;
        private Mood(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }*/

    public static String USER_NAME ;// "imageByteArray";
    public static final String SAVE_IMAGE =  "toSaveImage";
    public static final String IMAGE_READY = "android.intent.action.ImageReady";
    public static final String STATUS_CHANGE_BROADCAST = "android.intent.action.StatusChanged";
    public static final String MOOD_CHANGE_BROADCAST = "android.intent.action.MoodChanged";
    public static final int INITIAL_POINTS = 20;
    public static String Directory ;
    
    ////SETTINGS:
    public static final String SETTINGS_POINTS = "GLOBAL_POINTS";
    public static final String SETTINGS_NAME = "SETTINGS_NAME";
    public static final String SETTINGS_IS_ASLEEP = "SETTINGS_SLEEP";
    public static final String SETTINGS_INSULTS = "SETTINGS_INSULTS";
    public static final String SETTINGS_BLESSES = "SETTINGS_BLESSES";
    public static final String SETTING_USERNAME = "SETTING_USERNAME";
    public static final String SETTING_SHOW_EXPLAIN_GAME = "SETTING_SHOW_EXPLAIN_GAME";
    public static final String SETTING_LOG = "SETTING_LOG";
    public static final String SETTINGS_IS_TUTORIAL_DONE = "TUTORIAL_DONE";
    public static final String SETTINGS_IS_OPEN_VIDEO_DONE = "SETTINGS_IS_OPEN_VIDEO_DONE";

    public static final String SETTINGS_USER_WINS = "SETTINGS_USER_WINS";
    public static final String SETTINGS_USER_LOOSE = "SETTINGS_USER_LOOSE";
}
