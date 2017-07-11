package com.shalom.itai.theservantexperience.activities;

       /* import com.google.android.gms.auth.api.Auth;
        import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
        import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
        import com.google.android.gms.auth.api.signin.GoogleSignInResult;
       */

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.CalendarContract;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.shalom.itai.theservantexperience.R;
import com.shalom.itai.theservantexperience.chatBot.ChatActivity;
import com.shalom.itai.theservantexperience.chatBot.ChatListViewAdapter;
import com.shalom.itai.theservantexperience.chatBot.MyScheduledReceiver;
import com.shalom.itai.theservantexperience.gallery.GalleryActivity;
import com.shalom.itai.theservantexperience.moods.Mood;
import com.shalom.itai.theservantexperience.services.BuggerService;
import com.shalom.itai.theservantexperience.utils.Constants;
import com.shalom.itai.theservantexperience.utils.NewsHandeling.RSSFeedParser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

import pl.droidsonroids.gif.GifImageView;

import static com.shalom.itai.theservantexperience.utils.Constants.CHAT_START_MESSAGE;
import static com.shalom.itai.theservantexperience.utils.Constants.IS_INSTALLED;
import static com.shalom.itai.theservantexperience.utils.Constants.JonIntents.DONE_CALENDAR;
import static com.shalom.itai.theservantexperience.utils.Constants.JonIntents.INPUT_TO_SPLASH_CLASS_NAME;
import static com.shalom.itai.theservantexperience.utils.Constants.PREFS_NAME;
import static com.shalom.itai.theservantexperience.utils.Constants.SETTINGS_INITIAL_TIRED_POINTS;
import static com.shalom.itai.theservantexperience.utils.Constants.SETTINGS_TIRED_POINTS;
import static com.shalom.itai.theservantexperience.utils.Functions.createJonFolder;
import static com.shalom.itai.theservantexperience.utils.Functions.takeScreenshot;


public class MainActivity extends ToolBarActivity implements DialogCaller {

    private boolean readyToInvalidate = false;
    private static final String TAG = "MainActivity";
    private static final int REQUESTS = 100;

    //    public static MainActivity thisActivity;
    private ConstraintLayout mainLayout;
    private GifImageView gifImageView;
    private ImageView chatImage;
    private ListView chatListView;
    private ImageView memoriesImage;
    private Button mCancelTrip;
    private Vibrator mViber;
    public Button mHurtButton;

    // private boolean startOverly = false;
    @SuppressLint("MissingSuperCall")
    protected final void onCreate(Bundle icicle) {

        super.onCreate(icicle, R.layout.activity_main);
        initializeGui();
      //  int a = batteryTemperature(this);
      //  Log.d(TAG, "onCreate: " + a);
      //  ActivityCompat.requestPermissions(this, permissions, REQUESTS);
        //  thisActivity = this;
        readyToInvalidate = true;
        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        createJonFolder();
        //  addCalendarMeeting();
        if (!settings.getBoolean(IS_INSTALLED, false)) {
        //    setUserName();
            addCalendarMeeting();

        }
            BuggerService.getInstance().wakeUpJon();




         //   takeScreenshot(MainActivity.this, "I was born");
            /*SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
            if (!settings.getBoolean(IS_INSTALLED, false)) {
                takeScreenshot(MainActivity.this, "I was born");
                addCalendarMeeting();
                BuggerService.getInstance().writeToSettings(IS_INSTALLED, true);
            }*/
        }





    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        startService(new Intent(this, BuggerService.class));
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }


    private void initializeGui() {
        mainLayout = (ConstraintLayout) findViewById(R.id.main_layout);
        ViewTreeObserver vto = mainLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener (new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mainLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                if (! getApplicationContext().getSharedPreferences(PREFS_NAME, 0).getBoolean(IS_INSTALLED, false)) {
                    takeScreenshot(MainActivity.this, "I was born");
                    BuggerService.getInstance().writeToSettings(IS_INSTALLED, true);
                }
            }
        });
     //   signalStrength = (TextView) findViewById(R.id.reception_status_ind);
       // batteryStrength = (TextView) findViewById(R.id.battery_status_ind);
        chatListView = (ListView) findViewById(R.id.chat_list);
        Mood mood = BuggerService.getInstance().getMood();
        Log.d(TAG, "initializeGui: " + mood);
        gifImageView = (GifImageView) findViewById(R.id.GifImageView);

        gifImageView.setImageResource(R.drawable.jon_blinks);
        chatImage = (ImageView) findViewById(R.id.chat_image);
        memoriesImage = (ImageView) findViewById(R.id.memories);
        memoriesImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this,
                        SplashActivity.class).putExtra(INPUT_TO_SPLASH_CLASS_NAME, GalleryActivity.class.getSimpleName()));
                // overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                overridePendingTransition(R.anim.slide_top_in, R.anim.slide_bottom_out);
            }
        });
        mCancelTrip = (Button) findViewById(R.id.cancel_trip);
        mCancelTrip.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mCancelTrip.setVisibility(View.INVISIBLE);
                BuggerService.getInstance().unTrip();
                BuggerService.getInstance().bug();
                Toast.makeText(MainActivity.this, "I don't like it! " + BuggerService.getInstance().getRandomInsult(),
                        Toast.LENGTH_LONG).show();
            }
        });
        mViber = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        mHurtButton = (Button) findViewById(R.id.button_hurt);
        mHurtButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               startActivity(new Intent(MainActivity.this,Main2Activity.class));
                // startActivity(new Intent(MainActivity.this, ClientActivity.class));
               //BuggerService.getInstance().setSYSTEM_GlobalPoints(1,null);

                /*
                mainLayout.setBackgroundColor(Color.parseColor("#890606"));
                mViber.vibrate(1000);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        BuggerService.setSYSTEM_GlobalPoints(-1);
                        Toast.makeText(MainActivity.this, BuggerService.getInstance().getRandomInsult(), Toast.LENGTH_SHORT).show();
                        mainLayout.setBackgroundColor(Color.parseColor("#04967D"));
                    }
                }, 1000);
            */
            }
        });
    }

    private void changeLayoutAwake() {
        BuggerService.getInstance().wakeUpJon();
        gifImageView.setImageResource(R.drawable.jon_blinks);
        Toast.makeText(MainActivity.this, "Morning!",
                Toast.LENGTH_SHORT).show();
        mainLayout.setBackgroundColor(Color.parseColor("#04967D"));
    }

/*

    private void setUserName() {
        if (!USER_NAME.isEmpty()) {
            return;
        }
        Cursor c = null;
        try {
            c = getApplication().getContentResolver().query(ContactsContract.Profile.CONTENT_URI, null, null, null, null);
            if (c == null) {
                throw new Exception("cursor null");
            }
            c.moveToFirst();
            String name = (c.getString(c.getColumnIndex("display_name")));
            if (name != null && !name.isEmpty()) {
                USER_NAME = name;
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            Log.d(TAG, "setUserName: failed getting user name with cursor.. trying from mail");
            String name = getPrimaryEmail();
            if (!name.isEmpty()) {
                USER_NAME = name;
            } else {
                Log.d(TAG, "setUserName: failed getting user name from mail");
                // TODO ASK FOR NAME
            }
        } finally {
            if (c != null)
                c.close();
            if (USER_NAME != null && !USER_NAME.isEmpty()) {
                BuggerService.getInstance().writeToSettings(SETTINGS_NAME, USER_NAME);
            }
        }
    }
*/

    private String getPrimaryEmail() {
        try {
            AccountManager accountManager = AccountManager.get(this);
            if (accountManager == null)
                return "";
            Account[] accounts = accountManager.getAccounts();
            Pattern emailPattern = Patterns.EMAIL_ADDRESS;
            for (Account account : accounts) {
                // make sure account.name is an email address before adding to the list
                if (emailPattern.matcher(account.name).matches()) {

                    return account.name.split("@")[0].replaceAll("\\P{L}", " ").trim();
                }
            }
            return "";
        } catch (SecurityException e) {
            // exception will occur if app doesn't have GET_ACCOUNTS permission
            return "";
        }
    }

    /**
     * Sets alarm to one hour from now......
     */
    private void setAlarm() {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(this, MyScheduledReceiver.class);
        i.putExtra("BirthDay", true);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (60 * 60 * 1000), pi); // Millisec * Second * Minute
    }

    /**
     * This Adds to Jon's birthday to the user's calendar
     */
    private void addCalendarMeeting() {

        try {
            ContentResolver cr = getContentResolver();
            ContentValues values = new ContentValues();
            Calendar cal = Calendar.getInstance();
            values.put(CalendarContract.Events.DTSTART, cal.getTimeInMillis() + 60 * 60 * 1000);
            values.put(CalendarContract.Events.TITLE, Constants.ENTITY_NAME+"'s birthday!");
            values.put(CalendarContract.Events.DESCRIPTION, "Happy birthday to me!");
            //  TimeZone timeZone = TimeZone.getDefault();
            values.put(CalendarContract.Events.EVENT_TIMEZONE, "UTC/GMT +2:00");
            // default calendar
            values.put(CalendarContract.Events.CALENDAR_ID, 1);
            values.put(CalendarContract.Events.RRULE, "FREQ=YEARLY");
            values.put("hasAlarm", 1);
            values.put(CalendarContract.Events.DURATION, "+P1H");
            values.put(CalendarContract.Events.HAS_ALARM, 1);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
             //   ActivityCompat.requestPermissions(this, permissions, REQUESTS);
                return;
            }
            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
            if (uri == null) {
                secondTryCalendar();
            }
            setAlarm();
            Toast.makeText(getApplicationContext(), "My birthday!!", Toast.LENGTH_SHORT).show();
            Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("com.google.android.calendar");
            startActivity(LaunchIntent);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    MainActivity.this.finish();
                    Intent intent = new Intent(BuggerService.getInstance(), MainActivity.class).putExtra(DONE_CALENDAR, true);
                    startActivity(intent);
                }
            }, 4000);
        } catch (Exception e) {
            secondTryCalendar();
        }
    }

    private void secondTryCalendar() {
        Calendar cal = Calendar.getInstance();
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, cal.getTimeInMillis() + 60 * 60 * 1000);
        intent.putExtra(CalendarContract.Events.RRULE, "FREQ=YEARLY");
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, cal.getTimeInMillis() + 60 * 60 * 2000);
        intent.putExtra(CalendarContract.Events.TITLE, Constants.ENTITY_NAME+"'s birthday!");
        Toast.makeText(getApplicationContext(), "My birthday!!", Toast.LENGTH_SHORT).show();
        setAlarm();
        startActivity(intent);
    }


    public void showDialog() {
        DialogFragment newFragment = MyAlertDialogFragment
                .newInstance(R.string.alert_dialog_Wake_up_buttons_title, "Wake up!", "Shh...", getClass().getName());
        newFragment.show(getSupportFragmentManager(), "dialog");
    }


    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }

    /*
        public static MainActivity getInstance() {
            return thisActivity;
        }
    */
    @Override
    protected void onPause() {
        super.onPause();
       // startOverlay(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (!isMyServiceRunning(BuggerService.class)) {
            Log.d(TAG, "onNewIntent: rerunning bugger");     
        }
/*
        if (intent.getBooleanExtra("STOP_OVERLAY", false)) {
            Intent myService = new Intent(this, OverlyService.class);
            stopService(myService);
        }
  */
        if (intent.getBooleanExtra("sendJonToSleep", false)) {
            BuggerService.getInstance().sendJonToSleep(gifImageView, mainLayout, chatImage, MainActivity.this);

        }

        if (intent.getBooleanExtra("FROM_NOTIF", false)) {
            BuggerService.getInstance().setNotif();
        } else if (intent.getBooleanExtra("wakeUpOptions", false)) {
            forceWakeUp();
        } else if (intent.getBooleanExtra("tutorial_done", false)) {
            SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
            createJonFolder();
            if (!settings.getBoolean(IS_INSTALLED, false)) {
              //  setUserName();
        //        BuggerService.getInstance().createLogger();
                takeScreenshot(MainActivity.this, "I was born");
                addCalendarMeeting();
                BuggerService.getInstance().writeToSettings(IS_INSTALLED, true);
            }
        }
    }

    @Override
    protected void onResume() {

        super.onResume();



     //   stopOverlay(this);

        if (!isMyServiceRunning(BuggerService.class)) {

        }
        if (BuggerService.getInstance().getIsTrip()) {
            mCancelTrip.setVisibility(View.VISIBLE);
        }
        Intent intent = getIntent();
        if (intent.getBooleanExtra(Constants.JonIntents.ACTION_MAIN_SET_NOTIFICATION, false)) {
            BuggerService.getInstance().setNotif();
        }
        invalidateOptionsMenu();
        if (readyToInvalidate && BuggerService.getIsServiceUP()) {
            invalidateRelationsData();
            invalidateStatusParam();
            BuggerService.getInstance().onRefresh(gifImageView, mainLayout, chatImage, this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    private void forceWakeUp() {
        BuggerService.getInstance().wakeUpJon();
        List<String> legendList = new ArrayList<>();
        legendList.add("Put hear plugs and go back to bad");
        legendList.add("Stay awake");
        legendList.add("Ohhh... Sorry");
        chatListView.setAdapter(new ChatListViewAdapter(this, R.layout.layout_for_listview, legendList));
        chatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Toast.makeText(MainActivity.this, "Ok.. But it keep it down!", Toast.LENGTH_LONG).show();
                        BuggerService.setSYSTEM_GlobalPoints(-1,"You woke me up by making noise");
                        BuggerService.getInstance().sendJonToSleep(gifImageView, mainLayout, chatImage, MainActivity.this);
                        break;
                    case 1:
                        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
                        int tired = settings.getInt(SETTINGS_TIRED_POINTS, SETTINGS_INITIAL_TIRED_POINTS);
                        if (tired >= SETTINGS_INITIAL_TIRED_POINTS) {
                            Toast.makeText(MainActivity.this, "Ok", Toast.LENGTH_LONG).show();
                            BuggerService.setSYSTEM_GlobalPoints(-1,"You woke me up by making noise");
                        } else {
                            Toast.makeText(MainActivity.this, "Nope, too tired", Toast.LENGTH_LONG).show();
                            BuggerService.getInstance().sendJonToSleep(gifImageView, mainLayout, chatImage, MainActivity.this);
                        }
                        chatListView.setAdapter(new ChatListViewAdapter(MainActivity.this, R.layout.layout_for_listview, new ArrayList<String>()));
                        break;
                    case 2:
                        Toast.makeText(MainActivity.this, BuggerService.getInstance().getRandomInsult() + " I'm going back to sleep", Toast.LENGTH_LONG).show();
                        BuggerService.setSYSTEM_GlobalPoints(-2,"You woke me up by making noise");
                        BuggerService.getInstance().sendJonToSleep(gifImageView, mainLayout, chatImage, MainActivity.this);
                        chatListView.setAdapter(new ChatListViewAdapter(MainActivity.this, R.layout.layout_for_listview, new ArrayList<String>()));
                        break;
                }
                cleanListOptions();
            }
        });
    }

    private void cleanListOptions() {
        chatListView.setAdapter(null);
    }

    private void talkAboutWakeUp() {
        List<String> legendList = new ArrayList<>();
        legendList.add("Sorry to wake you up, Did you hear the news?");
        legendList.add("Stop sleeping you idiot");
        legendList.add("Ohhh... oops");
        chatListView.setAdapter(new ChatListViewAdapter(this, R.layout.layout_for_listview, legendList));
        chatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Toast.makeText(MainActivity.this, "What happened? Checking Ynet..", Toast.LENGTH_LONG).show();
                        BuggerService.setSYSTEM_GlobalPoints(1, "Woke me up to tell me something important");
                        cleanListOptions();
                        talkAboutNews();
                        break;
                    case 1:
                        Toast.makeText(MainActivity.this, BuggerService.getInstance().getRandomInsult() + " I'm going back to sleep", Toast.LENGTH_LONG).show();
                        BuggerService.setSYSTEM_GlobalPoints(-2, "You woke me up for nothing");
                        BuggerService.getInstance().sendJonToSleep(gifImageView, mainLayout, chatImage, MainActivity.this);
                        cleanListOptions();
                        break;
                    case 2:
                        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
                        int tired = settings.getInt(SETTINGS_TIRED_POINTS, SETTINGS_INITIAL_TIRED_POINTS);
                        if (tired>SETTINGS_INITIAL_TIRED_POINTS) {
                            Toast.makeText(MainActivity.this, "It's ok, I'll stay with you", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MainActivity.this, BuggerService.getInstance().getRandomInsult() + " I'm going back to sleep", Toast.LENGTH_LONG).show();
                            BuggerService.setSYSTEM_GlobalPoints(-1, "You woke me up for nothing");
                            BuggerService.getInstance().sendJonToSleep(gifImageView, mainLayout, chatImage, MainActivity.this);
                            cleanListOptions();
                        }
                        break;
                }
            }
        });
    }

    private void talkAboutNews() {
        RSSFeedParser feeder = new RSSFeedParser();

        Thread thread = feeder.fetchXML("http://www.ynet.co.il/Integration/StoryRss3254.xml");
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<RSSFeedParser.Entry> entries = feeder.vals;
        List<String> newsList = new ArrayList<>();
        for (int i = 0; i < entries.size(); i++) {
            newsList.add(entries.get(i).toString());
        }
        chatListView.setAdapter(new ChatListViewAdapter(this, R.layout.layout_for_listview, newsList));
        chatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (BuggerService.getInstance().shouldIBeNice()) {
                    String toStartWith = chatListView.getItemAtPosition(position).toString() + ". What you think about it??";
                    Toast.makeText(MainActivity.this, "Wow.. can't sleep now..", Toast.LENGTH_SHORT).show();
                    cleanListOptions();
                    startActivity(new Intent(MainActivity.this,
                            ChatActivity.class).putExtra(CHAT_START_MESSAGE, toStartWith));
                } else {
                    Toast.makeText(MainActivity.this, "I don't care, let me sleep.", Toast.LENGTH_SHORT).show();
                    BuggerService.getInstance().sendJonToSleep(gifImageView, mainLayout, chatImage, MainActivity.this);
                    cleanListOptions();
                }
            }
        });
    }

    @Override
    public void doPositive() {
        changeLayoutAwake();
        talkAboutWakeUp();
    }

    @Override
    public void doNegative() {
        Toast.makeText(MainActivity.this, "...ZZZzzzZZZzzz!", Toast.LENGTH_SHORT).show();
    }

/*

    public final static int REQUEST_CODE = 1231;

    public void checkDrawOverlayPermission() {
        */
/** check if we already  have permission to draw over other apps *//*

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                */
/** if not construct intent to request permission *//*

                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                */
/** request permission via start activity for result *//*

                startActivityForResult(intent, REQUEST_CODE);
            } else {
                BuggerService.startOverly = true;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        */
/** check if received result code
         is equal our requested code for draw permission  *//*

        if (requestCode == REQUEST_CODE) {
       */
/* if so check once again if we have permission *//*

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    BuggerService.startOverly = true;
                    // continue here - permission was granted
                }
            }
        }
    }
*/

/*
    private void checkSettingsPermissions() {
        if (!Settings.System.canWrite(getApplicationContext())) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + this.getPackageName()));
                this.startActivityForResult(intent, MainActivity.REQUESTS + 1);
            }
        }
    }


    public void callSpeech() {
        Intent intent = new Intent(this, SpeechRecognitionActivity.class);

        startActivity(intent);
    }


    public void callSms() {
        Intent intent = new Intent(this, SmsSendActivity.class);

        startActivity(intent);
    }
*/
}