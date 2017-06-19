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
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.shalom.itai.theservantexperience.chatBot.ChatActivity;
import com.shalom.itai.theservantexperience.chatBot.ChatListViewAdapter;
import com.shalom.itai.theservantexperience.chatBot.MyScheduledReceiver;
import com.shalom.itai.theservantexperience.R;
import com.shalom.itai.theservantexperience.services.BuggerService;
import com.shalom.itai.theservantexperience.utils.Constants;
import com.shalom.itai.theservantexperience.utils.NewsHandeling.RSSFeedParser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

import pl.droidsonroids.gif.GifImageView;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_WIFI_STATE;
import static android.Manifest.permission.GET_ACCOUNTS;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.RECEIVE_BOOT_COMPLETED;
import static android.Manifest.permission.VIBRATE;
import static android.Manifest.permission.WAKE_LOCK;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_SETTINGS;
import static com.shalom.itai.theservantexperience.utils.Constants.CHAT_START_MESSAGE;
import static com.shalom.itai.theservantexperience.utils.Constants.IS_INSTALLED;
import static com.shalom.itai.theservantexperience.utils.Constants.PREFS_NAME;
import static com.shalom.itai.theservantexperience.utils.Constants.SETTINGS_NAME;
import static com.shalom.itai.theservantexperience.utils.Constants.USER_NAME;
import static com.shalom.itai.theservantexperience.utils.Functions.createJonFolder;
import static com.shalom.itai.theservantexperience.utils.Functions.takeScreenshot;


public class MainActivity extends ToolBarActivity implements DialogCaller {

    private boolean readyToInvalidate = false;
    private static final String TAG = "AudioRecordTest";
    private static final int REQUESTS = 100;
    private boolean permissionToRecordAccepted = false;
    private boolean permissionToCameraAccepted = false;
    private boolean permissionToConttactsAccepted = false;
    private boolean permissionToAccounts = false;
    private boolean permissionToCalendarRead = false;
    private boolean permissionToCalendarWrite = false;
    private boolean permissionToSettings = false;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA,
            Manifest.permission.READ_CONTACTS, Manifest.permission.SEND_SMS, GET_ACCOUNTS,
            Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR, RECEIVE_BOOT_COMPLETED,
            VIBRATE, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE, ACCESS_COARSE_LOCATION, READ_PHONE_STATE,
            ACCESS_WIFI_STATE, INTERNET, WRITE_SETTINGS, WAKE_LOCK};
    private boolean isSleeping = false;
    //    public static MainActivity thisActivity;
    private TextView signalStrength;
    private TextView batteryStrength;
    private ConstraintLayout mainLayout;
    private GifImageView gifImageView;
    private ImageView chatImage;
    private ListView chatListView;
    private ImageView memoriesImage;
    private Button mCancelTrip;
    private Vibrator mViber;
    public Button mHurtButton;

    @SuppressLint("MissingSuperCall")
    protected final void onCreate(Bundle icicle) {
        super.onCreate(icicle, R.layout.activity_main);
        initializeGui();
        ActivityCompat.requestPermissions(this, permissions, REQUESTS);
        //  thisActivity = this;
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
        signalStrength = (TextView) findViewById(R.id.reception_status_ind);
        batteryStrength = (TextView) findViewById(R.id.battery_status_ind);
        chatListView = (ListView) findViewById(R.id.chat_list);
        gifImageView = (pl.droidsonroids.gif.GifImageView) findViewById(R.id.GifImageView);
        chatImage = (ImageView) findViewById(R.id.chat_image);
        gifImageView.setImageResource(R.drawable.jon_blinks);
        memoriesImage = (ImageView) findViewById(R.id.memories);
        memoriesImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this,
                        SplashActivity.class));
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
                Toast.makeText(MainActivity.this, "I dont like it! "+ BuggerService.getInstance().getRandomInsult(),
                        Toast.LENGTH_LONG).show();
            }
        });
         mViber = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        mHurtButton = (Button) findViewById(R.id.button_hurt);
        mHurtButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
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
            }
        });
    }

    private void changeLayoutAwake() {
        BuggerService.getInstance().wakeUpJon();
        gifImageView.setImageResource(R.drawable.jon_blinks);
        Toast.makeText(MainActivity.this, "Morning!",
                Toast.LENGTH_SHORT).show();
        mainLayout.setBackgroundColor(Color.parseColor("#04967D"));
        isSleeping = !isSleeping;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUESTS:
                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                permissionToCameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                permissionToConttactsAccepted = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                permissionToAccounts = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                permissionToCalendarRead = grantResults[5] == PackageManager.PERMISSION_GRANTED;
                permissionToCalendarWrite = grantResults[6] == PackageManager.PERMISSION_GRANTED;
                break;
            case REQUESTS + 1:
                permissionToSettings = grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
        if (!permissionToRecordAccepted || !permissionToCameraAccepted || !permissionToConttactsAccepted || !permissionToCalendarWrite || !permissionToCalendarRead)
            finish();
        readyToInvalidate = true;
        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        createJonFolder();
        //  addCalendarMeeting();
        if (!settings.getBoolean(IS_INSTALLED, false)) {
            setUserName();
            takeScreenshot(MainActivity.this, "I was born");
            addCalendarMeeting();
            BuggerService.getInstance().writeToSettings(IS_INSTALLED, true);
        }
        BuggerService.getInstance().wakeUpJon();
    }

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
            values.put(CalendarContract.Events.TITLE, "Jon's birthday!");
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
                ActivityCompat.requestPermissions(this, permissions, REQUESTS);
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
                    Intent intent = new Intent(BuggerService.getInstance(), MainActivity.class).putExtra("shit", true);
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
        intent.putExtra(CalendarContract.Events.TITLE, "Jon's birthday!");
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
        BuggerService.isMainActivityUp = false;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getBooleanExtra("sendJonToSleep", true)) {
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
                setUserName();
                takeScreenshot(MainActivity.this, "I was born");
                addCalendarMeeting();
                BuggerService.getInstance().writeToSettings(IS_INSTALLED, true);
            }
        }
    }

    @Override
    protected void onResume() {

        super.onResume();
        if(!isMyServiceRunning(BuggerService.class)){

        }
        if(BuggerService.getInstance().getIsTrip()){
            mCancelTrip.setVisibility(View.VISIBLE);
        }
        Intent intent = getIntent();
        if (intent.getBooleanExtra(Constants.JonIntents.ACTION_MAIN_SET_NOTIFICATION, false)) {
            BuggerService.getInstance().setNotif();
        }
        BuggerService.isMainActivityUp = true;
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
        isSleeping = false;
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
                        BuggerService.setSYSTEM_GlobalPoints(-1);
                        BuggerService.getInstance().sendJonToSleep(gifImageView, mainLayout, chatImage, MainActivity.this);
                        break;
                    case 1:
                        if (BuggerService.getInstance().shouldIDoThis() >= 0.5) {
                            Toast.makeText(MainActivity.this, "Ok", Toast.LENGTH_LONG).show();
                            BuggerService.setSYSTEM_GlobalPoints(-1);
                        } else {
                            Toast.makeText(MainActivity.this, "Nope", Toast.LENGTH_LONG).show();
                            BuggerService.getInstance().sendJonToSleep(gifImageView, mainLayout, chatImage, MainActivity.this);
                        }
                        chatListView.setAdapter(new ChatListViewAdapter(MainActivity.this, R.layout.layout_for_listview, new ArrayList<String>()));
                        break;
                    case 2:
                        Toast.makeText(MainActivity.this, BuggerService.getInstance().getRandomInsult() + " I'm going back to sleep", Toast.LENGTH_LONG).show();
                        BuggerService.setSYSTEM_GlobalPoints(-2);
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
        legendList.add("Stop sleeping you piece of metal");
        legendList.add("Ohhh... oops");
        chatListView.setAdapter(new ChatListViewAdapter(this, R.layout.layout_for_listview, legendList));
        chatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Toast.makeText(MainActivity.this, "What happened? Checking Ynet..", Toast.LENGTH_LONG).show();
                        BuggerService.setSYSTEM_GlobalPoints(1);
                        cleanListOptions();
                        talkAboutNews();
                        break;
                    case 1:
                        Toast.makeText(MainActivity.this, BuggerService.getInstance().getRandomInsult() + " I'm going back to sleep", Toast.LENGTH_LONG).show();
                        BuggerService.setSYSTEM_GlobalPoints(-2);
                        BuggerService.getInstance().sendJonToSleep(gifImageView, mainLayout, chatImage, MainActivity.this);
                        cleanListOptions();
                        break;
                    case 2:
                        if (BuggerService.getInstance().shouldIBeNice()) {
                            Toast.makeText(MainActivity.this, "It's ok, I'll stay with you", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MainActivity.this, BuggerService.getInstance().getRandomInsult() + " I'm going back to sleep", Toast.LENGTH_LONG).show();
                            BuggerService.setSYSTEM_GlobalPoints(-1);
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