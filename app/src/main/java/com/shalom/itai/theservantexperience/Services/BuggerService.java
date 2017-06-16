package com.shalom.itai.theservantexperience.Services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.widget.ImageView;

import com.shalom.itai.theservantexperience.Activities.MainActivity;
import com.shalom.itai.theservantexperience.GifImageView;
import com.shalom.itai.theservantexperience.Relations.RelationsFactory;
import com.shalom.itai.theservantexperience.Relations.RelationsStatus;
import com.shalom.itai.theservantexperience.Utils.Functions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static com.shalom.itai.theservantexperience.Utils.Constants.INITIAL_POINTS;
import static com.shalom.itai.theservantexperience.Utils.Constants.PREFS_NAME;
import static com.shalom.itai.theservantexperience.Utils.Constants.SETTINGS_BLESSES;
import static com.shalom.itai.theservantexperience.Utils.Constants.SETTINGS_INSULTS;
import static com.shalom.itai.theservantexperience.Utils.Constants.SETTINGS_IS_ASLEEP;
import static com.shalom.itai.theservantexperience.Utils.Constants.SETTINGS_POINTS;
import static com.shalom.itai.theservantexperience.Utils.Constants.SETTING_USERNAME;
import static com.shalom.itai.theservantexperience.Utils.Constants.USER_NAME;
import static com.shalom.itai.theservantexperience.Utils.Functions.throwRandomProb;

/**
 * Created by Itai on 09/04/2017.
 */

public class BuggerService extends Service {

    private static boolean isServiceUP = false;
    public static boolean isMainActivityUp = false;
    public static boolean isFunActivityUp = false;
    public static boolean isLoginUp = false;
    private double latDistanceToDest = 0;
    private double lngDistanceToDest = 0;
    public static boolean stopBugger = false;
    //  public static  Class[] Activities= new Class[]{SpeechRecognitionActivity.class, FunActivity.class ,DancingActivity.class, SmsSendActivity.class};
    public static int indexActive = 0;
    private static int SYSTEM_GlobalPoints;
    private static BuggerService mInstance;
    private static RelationsStatus currentStatus;
    private int mStartId = -1;
    public Actions currentAction;
    private SharedPreferences settings;
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
        currentStatus = RelationsFactory.getRelationStatus(SYSTEM_GlobalPoints);
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
        return currentStatus;
    }

    public static BuggerService getInstance() {
        return mInstance;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        isServiceUP = true;
        if (intent.getBooleanExtra("runMainActivity", false)) {
            Intent startMainActivity = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(startMainActivity);
            mStartId = startId;
        } else if (settings.getBoolean(SETTINGS_IS_ASLEEP, false)) {
            sendJonToSleep();
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
        currentStatus = RelationsFactory.getRelationStatus(SYSTEM_GlobalPoints);
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


    public void loadPoints() {
        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        SYSTEM_GlobalPoints = settings.getInt(SETTINGS_POINTS, INITIAL_POINTS);
    }

    private void loadUserName(){
        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        USER_NAME = settings.getString(SETTING_USERNAME, "");
    }


    public void loadInsultsAndBless(){
        SYSTEM_Insults = new ArrayList<>();
        Functions.createInsults(SYSTEM_Insults);
        SYSTEM_Blesses = new ArrayList<>();
        Functions.createBlesses(SYSTEM_Blesses);
        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        Set<String> insults = settings.getStringSet(SETTINGS_INSULTS, null);
        if(insults !=null ){
            Iterator<String> itr = insults.iterator();
            while (itr.hasNext()){
                SYSTEM_Insults.add(itr.next());
            }
        }

        Set<String> bless = settings.getStringSet(SETTINGS_BLESSES, null);
        if(bless !=null ){
            Iterator<String> itr = bless.iterator();
            while (itr.hasNext()){
                SYSTEM_Blesses.add(itr.next());
            }
        }

    }

    private void save(ArrayList<String> arr, String query,String SETTING){
        query = query.replaceAll("(?i)jon", USER_NAME);
        if(!arr.contains(query)){
            arr.add(query);
        }
        Set s = new HashSet(arr);
        writeToSettings(SETTING,s);
    }

    public void saveInsults(String insult){
            save(SYSTEM_Insults,insult,SETTINGS_INSULTS);
    }

    public void saveBless(String bless){
        save(SYSTEM_Blesses,bless,SETTINGS_BLESSES);
    }


    public String getRandomInsult(){
        if(SYSTEM_Insults !=null && !SYSTEM_Insults.isEmpty())
            return SYSTEM_Insults.get(Functions.throwRandom(SYSTEM_Insults.size(),0));
        return "";
    }

    public String getRandomBless(){
        if(SYSTEM_Blesses !=null && !SYSTEM_Blesses.isEmpty())
            return SYSTEM_Blesses.get(Functions.throwRandom(SYSTEM_Blesses.size(),0));
        return "";
    }


    @Override
    public boolean stopService(Intent name) {
        currentAction.StopTimers();
        stopSelf(mStartId);
        return super.stopService(name);

    }

    private void initializeConstants() {

    }

    public void unLock() {
        (currentAction).unLock();
    }

    public void lock() {
        ((DayActions) currentAction).lock();
    }

    public void bug() {
        ((DayActions) currentAction).bug();
    }

    public void unbug() {
        ((DayActions) currentAction).unbug();
    }

    public void wakeUpJon() {
        if (currentAction != null)
            currentAction.StopTimers();
        currentAction = DayActions.start(getApplicationContext(), 0);
        writeToSettings(SETTINGS_IS_ASLEEP, false);
    }

    public void goToTrip() {
        if (currentAction instanceof DayActions) {
            ((DayActions) currentAction).goToTrip(latDistanceToDest, lngDistanceToDest);
        }
    }

    public void unTrip() {
        if (currentAction instanceof DayActions) {
            ((DayActions) currentAction).unTrip();
        }
    }

    public void sendJonToSleep() {
        if (currentAction != null) {
            currentAction.StopTimers();
        }
        currentAction = NightActions.start(getApplicationContext(), 0);
        writeToSettings(SETTINGS_IS_ASLEEP, true);
    }

    public void sendJonToSleep(GifImageView gifImageView, ConstraintLayout mainLayout, ImageView chatImage) {
        this.sendJonToSleep();
        this.onRefresh(gifImageView, mainLayout, chatImage);
    }

    public void restartCheckStatus() {
        currentAction.restartCheckStatus();
    }


    public void stopNoiseListener() {
        currentAction.StopTimers();
    }

    public void onRefresh(GifImageView gifImageView, ConstraintLayout mainLayout, ImageView chatImage) {
        currentAction.setCustomMainActivity(gifImageView, mainLayout, chatImage);
    }

    public double shouldIDoThis(){
        return getRelationsStatus().getProbabilityNumber() +throwRandomProb();
    }

}

