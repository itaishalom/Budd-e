package com.shalom.itai.theservantexperience.services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.shalom.itai.theservantexperience.activities.MainActivity;
import com.shalom.itai.theservantexperience.introduction.TutorialActivity;
import com.shalom.itai.theservantexperience.relations.RelationsFactory;
import com.shalom.itai.theservantexperience.relations.RelationsStatus;
import com.shalom.itai.theservantexperience.utils.Constants;
import com.shalom.itai.theservantexperience.utils.Functions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import pl.droidsonroids.gif.GifImageView;

import static com.shalom.itai.theservantexperience.utils.Constants.INITIAL_POINTS;
import static com.shalom.itai.theservantexperience.utils.Constants.PREFS_NAME;
import static com.shalom.itai.theservantexperience.utils.Constants.SETTINGS_BLESSES;
import static com.shalom.itai.theservantexperience.utils.Constants.SETTINGS_INSULTS;
import static com.shalom.itai.theservantexperience.utils.Constants.SETTINGS_IS_ASLEEP;
import static com.shalom.itai.theservantexperience.utils.Constants.SETTINGS_POINTS;
import static com.shalom.itai.theservantexperience.utils.Constants.SETTING_USERNAME;
import static com.shalom.itai.theservantexperience.utils.Constants.USER_NAME;
import static com.shalom.itai.theservantexperience.utils.Functions.throwRandomProb;

/**
 * Created by Itai on 09/04/2017.
 */

public class BuggerService extends Service {
    private static boolean mIsTrip = false;
    private static boolean isServiceUP = false;
    public static boolean isMainActivityUp = false;
    public static boolean isFunActivityUp = false;
    public static boolean isLoginUp = false;
    private double latDistanceToDest = 0;
    private double lngDistanceToDest = 0;
    public static boolean stopBugger = false;
    //  public static  Class[] Activities= new Class[]{SpeechRecognitionActivity.class, FunActivity.class ,DancingActivity.class, SmsSendActivity.class};
    public static int indexActive = -2;
    private static int SYSTEM_GlobalPoints;
    private static BuggerService mInstance;
    private static RelationsStatus currentRelationsStatus;
    private int mStartId = -1;
    private Actions currentTimeAction;
    private ArrayList<String> SYSTEM_Insults;
    private ArrayList<String> SYSTEM_Blesses;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * Called when the service is being created.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        loadPoints();
        loadInsultsAndBless();
        loadUserName();
        currentRelationsStatus = RelationsFactory.getRelationStatus(SYSTEM_GlobalPoints);
        mInstance = this;
    }


    public void setDistanceToDest(double lat, double lng) {
        latDistanceToDest = lat;
        lngDistanceToDest = lng;
    }

    public double getDistanceLat() {
        return latDistanceToDest;
    }

    public double getDistanceLng() {
        return lngDistanceToDest;
    }

    public RelationsStatus getRelationsStatus() {
        if (currentRelationsStatus == null) {
            loadPoints();
            RelationsFactory.getRelationStatus(SYSTEM_GlobalPoints);
            return currentRelationsStatus;
        } else {
            return currentRelationsStatus;
        }
    }

    public static BuggerService getInstance() {
        return mInstance;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        isServiceUP = true;
        if (intent != null) {
            if (intent.getBooleanExtra(Constants.JonIntents.UPD_BUG_RUN_MAIN, false)) {
                Intent startMainActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(startMainActivity);
                mStartId = startId;
            } else if (intent.getBooleanExtra(Constants.JonIntents.UPD_BUG_RUN_TUT, false)) {
                startActivity(new Intent(this, TutorialActivity.class));
            } else if (settings.getBoolean(SETTINGS_IS_ASLEEP, false)) {
                sendJonToSleep();
            } else {
                wakeUpJon();
            }
        } else {
            wakeUpJon();
        }
        return Service.START_STICKY;
    }

    public static boolean getIsServiceUP() {
        return isServiceUP;
    }

    public static void setSYSTEM_GlobalPoints(int pts) {
        SYSTEM_GlobalPoints = SYSTEM_GlobalPoints + pts;
        // mInstance.savePoints();
        mInstance.writeToSettings(SETTINGS_POINTS, SYSTEM_GlobalPoints);
        currentRelationsStatus = RelationsFactory.getRelationStatus(SYSTEM_GlobalPoints);
    }

    public static int getSYSTEM_GlobalPoints() {
        return SYSTEM_GlobalPoints;
    }

    /*
        private  void savePoints(){
            SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt(SETTINGS_POINTS, SYSTEM_GlobalPoints);
            editor.commit();
        }
    */
    public void writeToSettings(String settingString, Object data) {
        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        if (data instanceof Integer) {
            editor.putInt(settingString, (int) data);
        } else if (data instanceof Float) {
            editor.putFloat(settingString, (float) data);
        } else if (data instanceof String) {
            editor.putString(settingString, (String) data);
        } else if (data instanceof Boolean) {
            editor.putBoolean(settingString, (Boolean) data);
        } else if (data instanceof Set) {
            editor.putStringSet(settingString, (Set) data);
        }

        editor.commit();
    }


    private void loadPoints() {
        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        SYSTEM_GlobalPoints = settings.getInt(SETTINGS_POINTS, INITIAL_POINTS);
    }

    private void loadUserName() {
        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        USER_NAME = settings.getString(SETTING_USERNAME, "");
    }


    private void loadInsultsAndBless() {
        SYSTEM_Insults = new ArrayList<>();
        Functions.createInsults(SYSTEM_Insults);
        SYSTEM_Blesses = new ArrayList<>();
        Functions.createBlesses(SYSTEM_Blesses);
        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        Set<String> insults = settings.getStringSet(SETTINGS_INSULTS, null);
        if (insults != null) {
            for (String insult : insults) {
                SYSTEM_Insults.add(insult);
            }
        }

        Set<String> bless = settings.getStringSet(SETTINGS_BLESSES, null);
        if (bless != null) {
            for (String bles : bless) {
                SYSTEM_Blesses.add(bles);
            }
        }

    }

    private void save(ArrayList<String> arr, String query, String SETTING) {
        query = query.replaceAll("(?i)jon", USER_NAME);
        if (!arr.contains(query)) {
            arr.add(query);
        }
        Set s = new HashSet(arr);
        writeToSettings(SETTING, s);
    }

    public void saveInsults(String insult) {
        save(SYSTEM_Insults, insult, SETTINGS_INSULTS);
    }

    public void saveBless(String bless) {
        save(SYSTEM_Blesses, bless, SETTINGS_BLESSES);
    }


    public String getRandomInsult() {
        if (SYSTEM_Insults != null && !SYSTEM_Insults.isEmpty())
            return SYSTEM_Insults.get(Functions.throwRandom(SYSTEM_Insults.size(), 0));
        return "";
    }

    public String getRandomBless() {
        if (SYSTEM_Blesses != null && !SYSTEM_Blesses.isEmpty())
            return SYSTEM_Blesses.get(Functions.throwRandom(SYSTEM_Blesses.size(), 0));
        return "";
    }


    @Override
    public boolean stopService(Intent name) {
        currentTimeAction.StopTimers(this);
        stopSelf(mStartId);
        return super.stopService(name);

    }


    public void unLock() {
        (currentTimeAction).unLock();
    }

    public void lock() {
        ((DayActions) currentTimeAction).lock(this);
    }

    public void bug() {
        ((DayActions) currentTimeAction).bug(this);
    }

    public void unbug() {
        ((DayActions) currentTimeAction).unbug();
    }

    public void wakeUpJon() {
        if (currentTimeAction != null)
            currentTimeAction.StopTimers(this);
        currentTimeAction = DayActions.start(getApplicationContext(), 0);
        writeToSettings(SETTINGS_IS_ASLEEP, false);
    }

    public void setNotif() {
        currentTimeAction.addNotification();
    }

    public void goToTrip() {
        mIsTrip = true;
        if (currentTimeAction instanceof DayActions) {
            ((DayActions) currentTimeAction).goToTrip(latDistanceToDest, lngDistanceToDest, this);
        }
    }

    public boolean getIsTrip() {
        return mIsTrip;
    }

    public void unTrip() {
        mIsTrip = false;
        if (currentTimeAction instanceof DayActions) {
            ((DayActions) currentTimeAction).unTrip(this);
        }
    }

    public void sendJonToSleep() {
        if (currentTimeAction != null) {
            currentTimeAction.StopTimers(this);
        }
        currentTimeAction = NightActions.start(getApplicationContext(), 0);
        writeToSettings(SETTINGS_IS_ASLEEP, true);
    }

    public void sendJonToSleep(pl.droidsonroids.gif.GifImageView gifImageView, ConstraintLayout mainLayout, ImageView chatImage, MainActivity activity) {
        this.sendJonToSleep();
        this.onRefresh(gifImageView, mainLayout, chatImage, activity);
    }

    public void restartCheckStatus() {
        currentTimeAction.restartCheckStatus();
    }


    public void stopNoiseListener() {
        currentTimeAction.StopTimers(this);
    }

    public void onRefresh(GifImageView gifImageView, ConstraintLayout mainLayout, ImageView chatImage, AppCompatActivity activity) {
        currentTimeAction.setCustomMainActivity(gifImageView, mainLayout, chatImage, activity);
    }

    public double shouldIDoThis() {
        return getRelationsStatus().getProbabilityNumber() + throwRandomProb();
    }

    public boolean shouldIBeNice() {
        return getRelationsStatus().getProbabilityNumber() + throwRandomProb() >= 0.5;
    }
}

