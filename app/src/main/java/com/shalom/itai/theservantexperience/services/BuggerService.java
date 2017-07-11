package com.shalom.itai.theservantexperience.services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.shalom.itai.theservantexperience.activities.Main2Activity;
import com.shalom.itai.theservantexperience.activities.MainActivity;
import com.shalom.itai.theservantexperience.introduction.TutorialActivity;
import com.shalom.itai.theservantexperience.moods.Mood;
import com.shalom.itai.theservantexperience.moods.MoodFactory;
import com.shalom.itai.theservantexperience.moods.Sleep;
import com.shalom.itai.theservantexperience.relations.RelationsFactory;
import com.shalom.itai.theservantexperience.relations.RelationsStatus;
import com.shalom.itai.theservantexperience.utils.Constants;
import com.shalom.itai.theservantexperience.utils.Functions;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import pl.droidsonroids.gif.GifImageView;

import static com.shalom.itai.theservantexperience.utils.Constants.INITIAL_POINTS;
import static com.shalom.itai.theservantexperience.utils.Constants.LOG_SEPARATOR;
import static com.shalom.itai.theservantexperience.utils.Constants.MOOD_CHANGE_BROADCAST;
import static com.shalom.itai.theservantexperience.utils.Constants.PREFS_NAME;
import static com.shalom.itai.theservantexperience.utils.Constants.SETTINGS_BLESSES;
import static com.shalom.itai.theservantexperience.utils.Constants.SETTINGS_INSULTS;
import static com.shalom.itai.theservantexperience.utils.Constants.SETTINGS_IS_ASLEEP;
import static com.shalom.itai.theservantexperience.utils.Constants.SETTINGS_POINTS;
import static com.shalom.itai.theservantexperience.utils.Constants.SETTING_LOG;
import static com.shalom.itai.theservantexperience.utils.Constants.STATUS_CHANGE_BROADCAST;
import static com.shalom.itai.theservantexperience.utils.Functions.getBatteryLevel;
import static com.shalom.itai.theservantexperience.utils.Functions.getBatteryTemperature;
import static com.shalom.itai.theservantexperience.utils.Functions.getReceptionLevel;
import static com.shalom.itai.theservantexperience.utils.Functions.oneTimeFunctions.getUserName;
import static com.shalom.itai.theservantexperience.utils.Functions.throwRandomProb;

/**
 * Created by Itai on 09/04/2017.
 */

public class BuggerService extends Service {
    private static final String TAG = BuggerService.class.getSimpleName();
    private static boolean mIsTrip = false;
    private static boolean isServiceUP = false;
    public static boolean isFunActivityUp = false;
    public static boolean isLoginUp = false;
    private double latDistanceToDest = 0;
    private double lngDistanceToDest = 0;
    //  public static  Class[] Activities= new Class[]{SpeechRecognitionActivity.class, FunActivity.class ,DancingActivity.class, SmsSendActivity.class};
  //  public static int indexActive = 0;
    private static int SYSTEM_GlobalPoints;
    private static BuggerService mInstance;
    private static RelationsStatus currentRelationsStatus;
    private int mStartId = -1;
    private Actions currentTimeAction;
    private ArrayList<String> Arr_Insults_Sys;
    private ArrayList<String> Arr_Blesses_Sys;
    private ArrayList<String> Arr_Logger_Sys;
    public static String sessionId;
    //public static boolean startOverly = false;
    private static Mood currentMood = null;


    public void setCurrntMood(Mood m){
        currentMood = m;
    }

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
        mInstance = this;

        currentRelationsStatus = RelationsFactory.getRelationStatus(SYSTEM_GlobalPoints);
        currentMood = changeMood();
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
                Intent startMainActivity = new Intent(getApplicationContext(), Main2Activity.class);
                startMainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMainActivity);
                mStartId = startId;
            } else if (intent.getBooleanExtra(Constants.JonIntents.UPD_BUG_RUN_TUT, false)) {
                startActivity(new Intent(this, TutorialActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
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

    public static void setSYSTEM_GlobalPoints(int pts, String logMessage) {
        if (logMessage != null)
            getInstance().pushEventToLogger(logMessage + LOG_SEPARATOR + Integer.signum(pts));
        int oldNumOfPoints = SYSTEM_GlobalPoints;
        if (oldNumOfPoints + pts <= 0) {
            SYSTEM_GlobalPoints = 0;
        } else {
            SYSTEM_GlobalPoints += pts;
        }
        Log.d(TAG, "setSYSTEM_GlobalPoints: " + SYSTEM_GlobalPoints);
        //    Toast.makeText(getInstance(),"points "+SYSTEM_GlobalPoints,Toast.LENGTH_LONG);
        if (oldNumOfPoints != SYSTEM_GlobalPoints) {
            mInstance.writeToSettings(SETTINGS_POINTS, SYSTEM_GlobalPoints);
            RelationsStatus oldStatus = currentRelationsStatus;
            Mood oldMood = currentMood;
            currentRelationsStatus = RelationsFactory.getRelationStatus(SYSTEM_GlobalPoints);

            currentMood = getInstance().changeMood();

            if (oldStatus != currentRelationsStatus) {
                Log.d(TAG, "setSYSTEM_GlobalPoints: Changed status");
                Intent i = new Intent(STATUS_CHANGE_BROADCAST);//.putExtra("path", pathToImage);
                getInstance().sendBroadcast(i);
                //     Toast.makeText(getInstance(),"Changed status",Toast.LENGTH_LONG);
            }
            if (oldMood != currentMood) {
                Log.d(TAG, "setSYSTEM_GlobalPoints: Changed status");
                Intent i = new Intent(MOOD_CHANGE_BROADCAST).putExtra("Mood",currentMood.getClass().getSimpleName());//.putExtra("path", pathToImage);
                getInstance().sendBroadcast(i);
                //     Toast.makeText(getInstance(),"Changed status",Toast.LENGTH_LONG);
            }
        }
    }

    public static int getSYSTEM_GlobalPoints() {
        return SYSTEM_GlobalPoints;
    }


    public void writeToSettings(String settingString, Object data) {
        Functions.writeToSettings(settingString, data, this);
    }


    private void loadPoints() {
        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        SYSTEM_GlobalPoints = settings.getInt(SETTINGS_POINTS, INITIAL_POINTS);
        //   SYSTEM_GlobalPoints -=7;
    }

    private void loadUserName() {
        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
    }


    private void loadInsultsAndBless() {
        Arr_Insults_Sys = new ArrayList<>();
        Arr_Logger_Sys = new ArrayList<>();
        Arr_Blesses_Sys = new ArrayList<>();

        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        Set<String> insults = settings.getStringSet(SETTINGS_INSULTS, null);
        if (insults != null && !insults.isEmpty()) {
            for (String insult : insults) {
                Arr_Insults_Sys.add(insult);
            }
        } else {
            Functions.createInsults(Arr_Insults_Sys);
        }

        Set<String> bless = settings.getStringSet(SETTINGS_BLESSES, null);
        if (bless != null && !bless.isEmpty()) {
            for (String bles : bless) {
                Arr_Blesses_Sys.add(bles);
            }
        } else {
            Functions.createBlesses(Arr_Blesses_Sys);
        }

        Set<String> logs = settings.getStringSet(SETTING_LOG, null);
        if (logs != null && !logs.isEmpty()) {
            for (String log : logs) {
                Arr_Logger_Sys.add(log);
            }
        }

    }

    private void save(ArrayList<String> arr, String query, String SETTING) {

        query = query.replaceAll("(?i)" + Constants.ENTITY_NAME, getUserName(this));
        if (!arr.contains(query)) {
            arr.add(query);
        }
        Set s = new HashSet(arr);
        writeToSettings(SETTING, s);
    }

    public void saveInsults(String insult) {
        save(Arr_Insults_Sys, insult, SETTINGS_INSULTS);
    }

    public void saveBless(String bless) {
        save(Arr_Blesses_Sys, bless, SETTINGS_BLESSES);
    }


    public String getRandomInsult() {
        if (Arr_Insults_Sys != null && !Arr_Insults_Sys.isEmpty())
            return Arr_Insults_Sys.get(Functions.throwRandom(Arr_Insults_Sys.size(), 0));
        return "";
    }

    public String getRandomBless() {
        if (Arr_Blesses_Sys != null && !Arr_Blesses_Sys.isEmpty())
            return Arr_Blesses_Sys.get(Functions.throwRandom(Arr_Blesses_Sys.size(), 0));
        return "";
    }


    @Override
    public boolean stopService(Intent name) {
        isServiceUP = false;
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
        if(currentTimeAction instanceof DayActions)
            return ;// Already awake
        if (currentTimeAction != null)
            currentTimeAction.StopTimers(this);
        currentTimeAction = DayActions.start(getApplicationContext(), 0);
        currentMood = getInstance().changeMood();
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
            ((DayActions) currentTimeAction).unTrip();
        }
    }

    public void sendJonToSleep() {
        if(currentTimeAction instanceof NightActions)
            return ;// Already awake
        if (currentTimeAction != null) {
            currentTimeAction.StopTimers(this);
        }
        currentTimeAction = NightActions.start(getApplicationContext(), 0);
        currentMood = Sleep.getInstance();
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

    public void onRefresh2(GifImageView gifImageView, ConstraintLayout mainLayout, ImageButton chatImage, AppCompatActivity activity) {
        currentTimeAction.setCustomMainActivity2(gifImageView, mainLayout, chatImage, activity);
    }

    public double shouldIDoThis() {
        return getRelationsStatus().getProbabilityNumber() + throwRandomProb();
    }

    public boolean shouldIBeNice() {
        return getRelationsStatus().getProbabilityNumber() + throwRandomProb() >= 0.5;
    }

    /*
        public void createLogger() {
            Arr_Logger_Sys = new ArrayList<>();
            pushEventToLogger("I was born" + LOG_SEPARATOR + "+");
        }
    */
    private void pushEventToLogger(String event) {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
        save(Arr_Logger_Sys, now + LOG_SEPARATOR + event, SETTING_LOG);
    }

    public int getLastActionsGrade() {
        int iters = 0;
        int counter = 0;
        for (int i = Arr_Logger_Sys.size() - 1; i >= 0; i--) {
            String[] event = Arr_Logger_Sys.get(i).split(LOG_SEPARATOR);
            counter += Integer.parseInt(event[2]);
            iters++;
            if (iters == 3) {
                break;
            }
        }
        return counter;
    }

    public Mood changeMood() {
        int lastActions = BuggerService.getInstance().getLastActionsGrade();
        int total = getBatteryLevel(this).getValue() + getBatteryTemperature(this).getValue()
                + BuggerService.getInstance().getRelationsStatus().getGradeFactor() + lastActions;
        try {
            total += getReceptionLevel(this).getValue();
        } catch (Exception e) {
            return MoodFactory.getMoodStatus(total, lastActions);
        }
//        int total = getBatteryLevel(this).getValue() + getReceptionLevel(this).getValue() + getBatteryTemperature(this).getValue()
        //              + BuggerService.getInstance().getRelationsStatus().getGradeFactor() + lastActions;
        return MoodFactory.getMoodStatus(total, lastActions);
    }

    public Mood getMood() {
        return currentMood;
    }

}

