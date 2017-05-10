package com.shalom.itai.theservantexperience.Activities;

       /* import com.google.android.gms.auth.api.Auth;
        import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
        import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
        import com.google.android.gms.auth.api.signin.GoogleSignInResult;
       */

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.vision.text.Text;
import com.shalom.itai.theservantexperience.ChatBot.ChatActivity;
import com.shalom.itai.theservantexperience.ChatBot.ChatListViewAdapter;
import com.shalom.itai.theservantexperience.GifImageView;
import com.shalom.itai.theservantexperience.ChatBot.MyScheduledReceiver;
import com.shalom.itai.theservantexperience.R;
import com.shalom.itai.theservantexperience.Relations.RelationsStatus;
import com.shalom.itai.theservantexperience.Services.BuggerService;

import com.shalom.itai.theservantexperience.Services.DayActions;
import com.shalom.itai.theservantexperience.Utils.Functions;
import com.shalom.itai.theservantexperience.Utils.NewsHandeling.RSSFeedParser;
import com.shalom.itai.theservantexperience.Utils.NoiseListener;


import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.graphics.Color;

import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.TelephonyManager;
import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_WIFI_STATE;
import static android.Manifest.permission.GET_ACCOUNTS;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.RECEIVE_BOOT_COMPLETED;
import static android.Manifest.permission.VIBRATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.shalom.itai.theservantexperience.Utils.Constants.CHAT_START_MESSAGE;


public class MainActivity extends AppCompatActivity {
    private boolean readyToInvalidate = false;
    public static final String TAG = "AudioRecordTest";
    private static final int REQUESTS = 100;
    private RSSFeedParser feeder;
    private boolean safeToTakePicture = false;
   // NoiseListener sm;
    private boolean permissionToRecordAccepted = false;
    private boolean permissionToCameraAccepted = false;
    private boolean permissionToConttactsAccepted = false;
    private boolean permissionToAccounts = false;
    private boolean permissionToCalendarRead = false;
    private boolean permissionToCalendarWrite = false;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA,
            Manifest.permission.READ_CONTACTS, Manifest.permission.SEND_SMS, GET_ACCOUNTS,
            Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR,RECEIVE_BOOT_COMPLETED,
            VIBRATE,WRITE_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE,ACCESS_COARSE_LOCATION,READ_PHONE_STATE,ACCESS_WIFI_STATE,INTERNET};
    private boolean isSleeping = false;
    public static MainActivity thisActivity;
    TextView signalStrength;
    TextView batteryStrength;
    ConstraintLayout mainLayout;
    GifImageView gifImageView;
    ImageView chatImage;
    ListView chatListView;
    @Override
    public void onCreate(Bundle icicle) {

        super.onCreate(icicle);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, permissions, REQUESTS);
      //  startService(new Intent(this, BuggerService.class));
        initializeGui();

      //  setBubbleFunction(false);
        //   popUpMessage();
        thisActivity = this;

     //    sm = new NoiseListener();
    //    sm.start();
    }

    /*
    private void setBubbleFunction(boolean wakeUp){
        if(!wakeUp) {
            chatImage.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.getInstance(),
                            ChatActivity.class).putExtra(CHAT_START_MESSAGE,
                            "Jon is here"));
                }
            });
        }else
        {
            chatImage.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    doSleepLogic();
                }
            });
        }
    }
*/
    private void initializeGui(){
        setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar));
        mainLayout = (ConstraintLayout) findViewById(R.id.main_layout);
        signalStrength = (TextView) findViewById(R.id.reception_status_ind);
        batteryStrength = (TextView) findViewById(R.id.battery_status_ind);
        chatListView = ( ListView ) findViewById( R.id.chat_list);
        gifImageView = (GifImageView) findViewById(R.id.GifImageView);
        chatImage = (ImageView) findViewById(R.id.chat_image);
        gifImageView.setGifImageResource(R.drawable.jon_blinks);
    }
/*
    private void doSleepLogic()
    {
        if(isSleeping)
        {//
            showDialog();

        }
        else {
            changeToSleepingJon();
        }
    }
*/
    /*
    private void changeToSleepingJon()
    {
        BuggerService.getInstance().sendJonToSleep();
        gifImageView.setGifImageResource(R.drawable.jon_sleeping);
        Toast.makeText(MainActivity.this, "Good night!",
                Toast.LENGTH_SHORT).show();
        mainLayout.setBackgroundColor(Color.parseColor("#234D6E"));
        isSleeping = !isSleeping;
    }
*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options, menu);
        MenuItem te =menu.findItem(R.id.action_favorite);
        te.setIcon(BuggerService.getInstance().getRelationsStatus().getIconId());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final ConstraintLayout relationsLayout =(ConstraintLayout) findViewById(R.id.relation_layout);
        final ConstraintLayout moodLayout =(ConstraintLayout) findViewById(R.id.mood_layout);

        switch (item.getItemId()) {
            case R.id.action_settings:
         //       startActivity(new Intent(MainActivity.getInstance(), GameActivity.class));
                return true;
            case R.id.action_mood:
                if(moodLayout.getVisibility() == View.VISIBLE)
                    moodLayout.setVisibility(View.INVISIBLE);
                else {
                    moodLayout.setVisibility(View.VISIBLE);
                    relationsLayout.setVisibility(View.INVISIBLE);
                    invalidateStatusParam();

                }
                //       showDialog();
                return true;
            case R.id.action_favorite:
//                ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar3);
                if(relationsLayout.getVisibility() == View.VISIBLE)
                    relationsLayout.setVisibility(View.INVISIBLE);
                else {
                    invalidateRelationsData();
                    relationsLayout.setVisibility(View.VISIBLE);
                    moodLayout.setVisibility(View.INVISIBLE);
                }
                //       showDialog();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void invalidateStatusParam()
    {
        signalStrength.setText(String.valueOf(getReceptionLevel()));
        batteryStrength.setText(String.valueOf( getBatteryLevel()));
    }


    private void invalidateRelationsData(){

        RelationsStatus status = BuggerService.getInstance().getRelationsStatus();
        ProgressBar pb = (ProgressBar) findViewById(R.id.progressBarRelations);
        pb.setMax(status.getMaxValProgress());
        pb.setProgress(BuggerService.getInstance().getSYSTEM_GlobalPoints());
        TextView friendshipLevel = (TextView) findViewById(R.id.friendship_level_ind);
        friendshipLevel.setText(status.getRelationStatus());
    }

//getRelationStatus
    private int getBatteryLevel(){
        BatteryManager bm = (BatteryManager)getSystemService(BATTERY_SERVICE);
        return bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
    }

    private int getReceptionLevel(){

        WifiManager mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        int numberOfLevels=5;
        if (mWifiManager.isWifiEnabled()) {
            int linkSpeed = mWifiManager.getConnectionInfo().getRssi();
            int level = WifiManager.calculateSignalLevel(linkSpeed, numberOfLevels);
            return level;
        }
        int num =-1000;
        TelephonyManager telephonyManager = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        Object obj = telephonyManager.getAllCellInfo().get(0);
        if (obj instanceof CellInfoLte) {

            CellInfoLte cellinfogsm = (CellInfoLte)obj;
            CellSignalStrengthLte cellSignalStrengthLTE = cellinfogsm.getCellSignalStrength();
            num = cellSignalStrengthLTE.getDbm();
        }
        else if (obj instanceof CellInfoWcdma)
        {
            CellInfoWcdma cellinfogsm = (CellInfoWcdma) obj;
            CellSignalStrengthWcdma cellSignalStrengthWCDMA = cellinfogsm.getCellSignalStrength();
            num = cellSignalStrengthWCDMA.getDbm();
        }
        else if (obj instanceof CellInfoCdma)
        {
            CellInfoCdma cellinfogsm = (CellInfoCdma) obj;
            CellSignalStrengthCdma cellSignalStrengthCDMA = cellinfogsm.getCellSignalStrength();
            num = cellSignalStrengthCDMA.getDbm();
        }
        return num;
    }


    public void showDialog() {
        DialogFragment newFragment = MyAlertDialogFragment
                .newInstance(R.string.alert_dialog_two_buttons_title);
        newFragment.show(getSupportFragmentManager(),"dialog");
    }

private void changeLayoutAwake()
{
    BuggerService.getInstance().wakeUpJon();
    gifImageView.setGifImageResource(R.drawable.jon_blinks);
    Toast.makeText(MainActivity.this, "Morning!",
            Toast.LENGTH_SHORT).show();
    mainLayout.setBackgroundColor(Color.parseColor("#04967D"));
    isSleeping = !isSleeping;
}


    public void doPositiveClick() {
        changeLayoutAwake();
        Log.i("FragmentAlertDialog", "Positive click!");

        talkAboutWakeUp();
    }

    public void doNegativeClick() {
        // Do stuff here.
        Log.i("FragmentAlertDialog", "Negative click!");
    }

    public void callSpeech() {
        Intent intent = new Intent(this, SpeechRecognitionActivity.class);

        startActivity(intent);
    }



    public void callSms() {
        Intent intent = new Intent(this, SmsSendActivity.class);

        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUESTS:
                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                permissionToCameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                permissionToConttactsAccepted= grantResults[2] == PackageManager.PERMISSION_GRANTED;
                permissionToAccounts= grantResults[3] == PackageManager.PERMISSION_GRANTED;
                permissionToCalendarRead =  grantResults[5] == PackageManager.PERMISSION_GRANTED;
                permissionToCalendarWrite =  grantResults[6] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted || !permissionToCameraAccepted ||!permissionToConttactsAccepted || !permissionToCalendarWrite || !permissionToCalendarRead) finish();
        Functions.fadingText(this,R.id.jon_text);
        readyToInvalidate= true;
        BuggerService.getInstance().wakeUpJon();
        //      addCalendarMeeting();
    }



    public void onBackPressed() {

        this.finish();
        super.onBackPressed();
    }


    public static MainActivity getInstance()
    {
        return thisActivity;
    }

    @Override
    protected void onPause() {

        super.onPause();
        BuggerService.isMainActivityUp = false;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
/*
        setBubbleFunction(true);
        if(intent.getBooleanExtra("sleep",false)) {
            changeToSleepingJon();
            return;
        }
    */
        if(intent.getBooleanExtra("wakeUpOptions",false)){
            forceWakeUp();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        BuggerService.isMainActivityUp = true;
        invalidateOptionsMenu ();
        if(readyToInvalidate && BuggerService.getIsServiceUP()) {
            invalidateRelationsData();
            invalidateStatusParam();
            BuggerService.getInstance().onRefresh(gifImageView,mainLayout,chatImage);
        }

        //   mSensorManager.registerListener(mShakeListener, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onStop() {
        super.onStop();
   /*     Toast.makeText(MainActivity.this, "I am still here!",
                Toast.LENGTH_LONG).show();*/
    }


    private void forceWakeUp() {
        isSleeping = false;
        List<String> legendList= new ArrayList<String>();
        legendList.add("Put hear plugs and go back to bad");
        legendList.add("Stay awake");
        legendList.add("Ohhh... Sorry");
        chatListView.setAdapter( new ChatListViewAdapter(this, R.layout.layout_for_listview, legendList ) );
        chatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //   String o = (String) parent.getItemAtPosition(position);
                //   Toast.makeText(MainActivity.getInstance(), o, Toast.LENGTH_SHORT).show();
                switch (position){
                    case 0:
                        Toast.makeText(MainActivity.getInstance(),"Ok.. But it keep it down!", Toast.LENGTH_LONG).show();
                        BuggerService.setSYSTEM_GlobalPoints(-1);
                        BuggerService.getInstance().sendJonToSleep(gifImageView,mainLayout,chatImage);
                        break;
                    case 1:
                        Toast.makeText(MainActivity.getInstance(),"Ok", Toast.LENGTH_LONG).show();
                        BuggerService.setSYSTEM_GlobalPoints(-2);
                        chatListView.setAdapter( new ChatListViewAdapter(MainActivity.getInstance(), R.layout.layout_for_listview, new ArrayList<String>()) );
                        break;
                    case 2:
                        Toast.makeText(MainActivity.getInstance(),"You silly, I'm going back to sleep", Toast.LENGTH_LONG).show();
                        BuggerService.setSYSTEM_GlobalPoints(-1);
                        BuggerService.getInstance().sendJonToSleep(gifImageView,mainLayout,chatImage);
                        chatListView.setAdapter( new ChatListViewAdapter(MainActivity.getInstance(), R.layout.layout_for_listview, new ArrayList<String>()) );
                        break;
                }
                cleanListOptions();
            }
        });
    }

    private void cleanListOptions()
    {
        chatListView.setAdapter( new ChatListViewAdapter(MainActivity.getInstance(), R.layout.layout_for_listview, new ArrayList<String>()) );
    }

    private void talkAboutWakeUp() {
        List<String> legendList= new ArrayList<String>();
        legendList.add("Sorry to wake you up, Did you hear the news?");
        legendList.add("Stop sleeping you piece of metal");
        legendList.add("Ohhh... oops");
        chatListView.setAdapter( new ChatListViewAdapter(this, R.layout.layout_for_listview, legendList ) );
        chatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //   String o = (String) parent.getItemAtPosition(position);
                //   Toast.makeText(MainActivity.getInstance(), o, Toast.LENGTH_SHORT).show();
                switch (position){
                    case 0:
                        Toast.makeText(MainActivity.getInstance(),"What happened? Checking Ynet..", Toast.LENGTH_LONG).show();
                        BuggerService.setSYSTEM_GlobalPoints(1);
                        talkAboutNews();
                        break;
                    case 1:
                        Toast.makeText(MainActivity.getInstance(),"You woke me up for that? I'm going back to sleep", Toast.LENGTH_LONG).show();
                        BuggerService.setSYSTEM_GlobalPoints(-2);
                        BuggerService.getInstance().sendJonToSleep(gifImageView,mainLayout,chatImage);
                        break;
                    case 2:
                        Toast.makeText(MainActivity.getInstance(),"You silly, I'm going back to sleep", Toast.LENGTH_LONG).show();
                        BuggerService.setSYSTEM_GlobalPoints(-1);
                        BuggerService.getInstance().sendJonToSleep(gifImageView,mainLayout,chatImage);
                        break;
                }
            cleanListOptions();
            }
        });
    }

    private void talkAboutNews()
    {
        RSSFeedParser feeder = new RSSFeedParser();

        Thread thread = feeder.fetchXML("http://www.ynet.co.il/Integration/StoryRss3254.xml");
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<RSSFeedParser.Entry> entries = feeder.vals;
        List<String> newsList= new ArrayList<String>();
        for(int i =0;i<entries.size();i++ ){
            newsList.add(entries.get(i).toString());
        }
        chatListView.setAdapter( new ChatListViewAdapter(this, R.layout.layout_for_listview, newsList ) );
        chatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //   String o = (String) parent.getItemAtPosition(position);
                String toStartWith = chatListView.getItemAtPosition(position).toString()+". What you think about it??";
                Toast.makeText(MainActivity.getInstance(), "Wow.. can't sleep now..", Toast.LENGTH_SHORT).show();
                cleanListOptions();
                startActivity(new Intent(MainActivity.getInstance(),
                        ChatActivity.class).putExtra(CHAT_START_MESSAGE,toStartWith));
            }
        });
    }

}