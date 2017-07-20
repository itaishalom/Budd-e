package com.shalom.itai.theservantexperience.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.speech.RecognizerIntent;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.shalom.itai.theservantexperience.R;
import com.shalom.itai.theservantexperience.chatBot.ChatActivity;
import com.shalom.itai.theservantexperience.chatBot.ChatListViewAdapter;
import com.shalom.itai.theservantexperience.moods.Angry;
import com.shalom.itai.theservantexperience.moods.Board;
import com.shalom.itai.theservantexperience.moods.Calm;
import com.shalom.itai.theservantexperience.moods.Excited;
import com.shalom.itai.theservantexperience.moods.Fine;
import com.shalom.itai.theservantexperience.moods.Happy;
import com.shalom.itai.theservantexperience.moods.Mood;
import com.shalom.itai.theservantexperience.moods.Optimistic;
import com.shalom.itai.theservantexperience.moods.Sad;
import com.shalom.itai.theservantexperience.moods.Sleep;
import com.shalom.itai.theservantexperience.services.BuggerService;
import com.shalom.itai.theservantexperience.services.DayActions;
import com.shalom.itai.theservantexperience.utils.Client;
import com.shalom.itai.theservantexperience.utils.Constants;
import com.shalom.itai.theservantexperience.utils.Functions;
import com.shalom.itai.theservantexperience.utils.NewsHandeling.RSSFeedParser;

import java.util.ArrayList;
import java.util.List;

import static com.shalom.itai.theservantexperience.utils.Constants.CHAT_START_MESSAGE;
import static com.shalom.itai.theservantexperience.utils.Constants.ENTITY_NAME;
import static com.shalom.itai.theservantexperience.utils.Constants.FULL_INT_ALPHA;
import static com.shalom.itai.theservantexperience.utils.Constants.HALF_INT_ALPHA;
import static com.shalom.itai.theservantexperience.utils.Constants.INITIAL_NOISE_LEVEL;
import static com.shalom.itai.theservantexperience.utils.Constants.IS_INSTALLED;
import static com.shalom.itai.theservantexperience.utils.Constants.JonIntents.ASK_TO_PLAY;
import static com.shalom.itai.theservantexperience.utils.Constants.JonIntents.JUST_WOKE_UP;
import static com.shalom.itai.theservantexperience.utils.Constants.PREFS_NAME;
import static com.shalom.itai.theservantexperience.utils.Constants.SAY_LOVE;
import static com.shalom.itai.theservantexperience.utils.Constants.SETTINGS_CALLED_MAIN_ONCE;
import static com.shalom.itai.theservantexperience.utils.Constants.SETTINGS_INITIAL_TIRED_POINTS;
import static com.shalom.itai.theservantexperience.utils.Constants.SETTINGS_IS_ASLEEP;
import static com.shalom.itai.theservantexperience.utils.Constants.SETTINGS_NOISE_LEVEL;
import static com.shalom.itai.theservantexperience.utils.Constants.SETTINGS_TIRED_POINTS;
import static com.shalom.itai.theservantexperience.utils.Constants.SMS_SEND;
import static com.shalom.itai.theservantexperience.utils.Functions.checkScreenAndLock;
import static com.shalom.itai.theservantexperience.utils.Functions.createJonFolder;
import static com.shalom.itai.theservantexperience.utils.Functions.getContacts;
import static com.shalom.itai.theservantexperience.utils.Functions.getUserName;
import static com.shalom.itai.theservantexperience.utils.Functions.oneTimeFunctions.addCalendarMeeting;
import static com.shalom.itai.theservantexperience.utils.Functions.sendSMS;
import static com.shalom.itai.theservantexperience.utils.Functions.takeScreenshot;


public class Main2Activity extends ToolBarActivityNew implements DialogCaller {
    private int LOVE_REQUEST = 1212;
    public final static String TAG = "Main2Activity";
    private ImageButton openPoke;
    private ImageButton openChat;
    private ImageButton openGame;
    private ImageButton openTrip;
    private ImageButton openMore;
    private ListView chatListView;
    private int animationTime = 70;
    private boolean isShown = false;
    private int mAnimationIndex;
    AlphaAnimation mOnAnimation;
    AlphaAnimation mOffAnimation;
    ArrayList<ImageButton> allImageButtons;
    boolean startedAnimation = false;
    private ImageButton currendPressed;
    private Vibrator mViber;
    private boolean wasClosedFromOutside = false;
    private boolean readyToInvalidate;
    private boolean isPokeOpen = false;
    private int moodIndex = 0;
    private String selectContact;
    private Mood[] moodArr = new Mood[]{Sad.getInstance(), Angry.getInstance(), Fine.getInstance(), Optimistic.getInstance(), Board.getInstance(), Calm.getInstance(), Happy.getInstance(), Excited.getInstance(), Sleep.getInstance()};
    private final View.OnTouchListener changeColorListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int color;// = Color.CYAN;
            if (chatListView.getVisibility() == View.VISIBLE)
                return true;
            try {
                Bitmap bmp = Bitmap.createBitmap(v.getDrawingCache());
                color = bmp.getPixel((int) event.getX(), (int) event.getY());
            } catch (Exception e) {
                color = Color.CYAN;
            }
            if (color == Color.TRANSPARENT) {
                return false;
            } else {
                if (!startedAnimation) {
                    startedAnimation = !startedAnimation;
                    if (!isShown) {
                        allImageButtons.get(0).clearAnimation();
                        allImageButtons.get(0).startAnimation(mOnAnimation);
                        turnOfAllActionsBar();
                    } else {
                        allImageButtons.get(allImageButtons.size() - 1).clearAnimation();
                        allImageButtons.get(allImageButtons.size() - 1).startAnimation(mOffAnimation);
                    }
                    isShown = !isShown;
                }
                return true;
            }

        }
    };

    private final View.OnTouchListener imageButtonListner = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                final int x = (int) event.getX();
                final int y = (int) event.getY();

                //now map the coords we got to the
                //bitmap (because of scaling)
                ImageButton imageView = null;//= ((ImageButton) v);
                try {

                    for (ImageButton view : allImageButtons) {
                        Bitmap bitmap = ((BitmapDrawable) view.getDrawable()).getBitmap();

                        int pixel = bitmap.getPixel(x, y);
                        int alpha = Color.alpha(pixel);
                        //now check alpha for transparency
                        if (alpha != 0) {
                            imageView = view;
                            break;
                        }
                    }

                    if (imageView != null) {
                        if (imageView.getImageAlpha() != 255) {
                            imageView.setImageAlpha(255);
                            SharedPreferences preferences = getSharedPreferences(PREFS_NAME, 0);
                            if (preferences.getBoolean(SETTINGS_IS_ASLEEP, false)) {
                                currendPressed = imageView;
                                showDialog();
                                return true;
                            }
                            if (imageView == openMore) {
                              startActivity(new Intent(Main2Activity.this,AboutActivity.class));
                                /*
                                Toast.makeText(Main2Activity.this, "Not available yet", Toast.LENGTH_SHORT);
                                if (moodIndex == moodArr.length)
                                    moodIndex = 0;
                                BuggerService.getInstance().setCurrntMood(moodArr[moodIndex]);
                                moodIndex++;
                                refreshLayout();
                                */
                                /*
                                //    forceWakeUp();
                                if (moodIndex == moodArr.length)
                                    moodIndex = 0;
                                BuggerService.getInstance().sendMessage("Budd-E's mood at itai is now " + moodArr[moodIndex].getClass().getSimpleName().toLowerCase());
                                //     BuggerService.getInstance().setCurrntMood(moodArr[moodIndex]);
                                moodIndex++;
                                refreshLayout();
                                */
                                return true;
                            }
                            if (imageView == openChat) {
                                //refreshLayout2();
                                Main2Activity.this.startActivity(new Intent(Main2Activity.this, ChatActivity.class).putExtra(CHAT_START_MESSAGE, ENTITY_NAME + " is here"));
                                Main2Activity.this.overridePendingTransition(R.anim.slide_bottom_in, R.anim.slide_top_out);
                            }
                            if (imageView == openGame) {
                                //    refreshLayout3();
                                Main2Activity.this.startActivity(new Intent(Main2Activity.this, MatchesGameActivity.class).putExtra(ASK_TO_PLAY, false));
                                Main2Activity.this.overridePendingTransition(R.anim.slide_bottom_in, R.anim.slide_top_out);
                            }
                            if (imageView == openTrip) {
                                if (BuggerService.getInstance().getIsTrip()) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
                                    builder.setCancelable(false);
                                    builder.setMessage("Cancel trip?")
                                            .setTitle("Trip");
                                    // Add the buttons
                                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            BuggerService.getInstance().unTrip();
                                            BuggerService.getInstance().bug();
                                            BuggerService.setSYSTEM_GlobalPoints(-1, "Cancelled trip");
                                            Toast.makeText(Main2Activity.this, "I don't like it! " + BuggerService.getInstance().getRandomInsult(),
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    });
                                    builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            return;
                                        }
                                    });
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                }
                                Main2Activity.this.startActivity(new Intent(Main2Activity.this, TripActivity.class));
                                Main2Activity.this.overridePendingTransition(R.anim.slide_bottom_in, R.anim.slide_top_out);
                            }
                            if (imageView == openPoke) {
                                isPokeOpen = true;
                                openPokes();
                            }
                            // MainActivity.getInstance().overridePendingTransition(R.anim.slide_bottom_in, R.anim.slide_top_out);
                            //   Main2Activity.this.overridePendingTransition(R.anim.slide_bottom_in, R.anim.slide_top_out);

                        } else {
                            imageView.setImageAlpha(128);
                        }
                    }
                } catch (IllegalArgumentException e) {
                    return true;
                }
            }
            return true; //we've handled the event
        }
    };

    private void openPokes() {
        if (chatListView == null) {
            chatListView = (ListView) findViewById(R.id.chat_list);
        }
        close();
        mGifImageView.setImageAlpha(HALF_INT_ALPHA);
        chatListView.setVisibility(View.VISIBLE);
        BuggerService.getInstance().wakeUpJon();
        List<String> legendList = new ArrayList<>();
        legendList.add("Poke");
        legendList.add("Hurt");
        chatListView.setAdapter(new ChatListViewAdapter(this, R.layout.layout_for_listview, legendList));

        chatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Toast.makeText(Main2Activity.this, "Hahaha :)", Toast.LENGTH_LONG).show();

                        break;
                    case 1:
                        mainLayout.setBackgroundColor(Color.parseColor("#890606"));
                        mViber.vibrate(1000);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                BuggerService.setSYSTEM_GlobalPoints(-1, "you hurt me");
                                Toast.makeText(Main2Activity.this, BuggerService.getInstance().getRandomInsult(), Toast.LENGTH_SHORT).show();
                                Main2Activity.this.refreshLayout();
                            }
                        }, 1000);
                        break;
                }
                isPokeOpen = false;
                cleanListOptions();
            }
        });
    }


    private void close() {
        if (isShown) {
            if (startedAnimation) {
                for (int i = 0; i < allImageButtons.size(); i++)
                    (allImageButtons.get(i)).clearAnimation();
                mAnimationIndex = allImageButtons.size() - 1;
            }
            //   startedAnimation = true;

            allImageButtons.get(allImageButtons.size() - 1).clearAnimation();
            allImageButtons.get(allImageButtons.size() - 1).startAnimation(mOffAnimation);
            isShown = false;
        }
    }


    @Override
    protected void hideBeneath(ConstraintLayout layoutAppeared) {
        if (isShown) {
            close();
            wasClosedFromOutside = true;
        }
    }

    @Override
    protected void showBeneath() {
        if (wasClosedFromOutside && !isShown) {
            if (startedAnimation) {
                for (int i = 0; i < allImageButtons.size(); i++)
                    (allImageButtons.get(i)).clearAnimation();
                // mAnimationIndex = 0;
            }
            startedAnimation = true;
            allImageButtons.get(0).clearAnimation();
            allImageButtons.get(0).startAnimation(mOnAnimation);
            isShown = true;
            wasClosedFromOutside = false;
        }
    }


    @Override
    public void onBackPressed() {
        if (isPokeOpen) {
            cleanListOptions();
            isPokeOpen = false;
            return;
        }else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_main_new, R.menu.tool_bar_options, true, -1);
        // getSupportActionBar().setIcon(R.drawable.title);
        // Functions.oneTimeFunctions.setUserName(this, TAG);

        initializeAnimations();
        initializeGui();
        mViber = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        createJonFolder();
        if (!settings.getBoolean(IS_INSTALLED, false)) {
            Functions.oneTimeFunctions.setUserName(this, TAG);

        }
        if (BuggerService.getInstance() != null) {

            if (settings.getBoolean(SETTINGS_IS_ASLEEP, false))
                BuggerService.getInstance().sendJonToSleep();
            else
                BuggerService.getInstance().wakeUpJon();
        }
    }

    private void popUpForRequest() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(R.string.say_love)
                .setTitle(R.string.alert_dialog_love);
        // Add the buttons
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Functions.writeToSettings(SAY_LOVE, false, Main2Activity.this);
                startListen();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void startListen() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
        startActivityForResult(intent, LOVE_REQUEST);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOVE_REQUEST && resultCode == RESULT_OK) {
            ArrayList arrayList = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (!arrayList.get(0).equals("I love you")) {
                Toast.makeText(getApplicationContext(), "you can do better", Toast.LENGTH_LONG).show();
                startListen();
            } else {
                Toast.makeText(getApplicationContext(), "I love you too!!!", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    private void startSmsSession() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Who is your best friend? Choose from the list")
                .setTitle("Friends");
        // Add the buttons
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Functions.writeToSettings(SMS_SEND, false, Main2Activity.this);
                prepareList();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void prepareList() {
        if (chatListView == null) {
            chatListView = (ListView) findViewById(R.id.chat_list);
        }
        close();
        mGifImageView.setImageAlpha(HALF_INT_ALPHA);
        chatListView.setVisibility(View.VISIBLE);

        List<String> legendList = getContacts(this);

        chatListView.setAdapter(new ChatListViewAdapter(Main2Activity.this, R.layout.layout_for_listview, legendList));
        //Do something on click on ListView Click on Items
        chatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                //    ListView l = (ListView) arg0;
                //   l.
                Object o = chatListView.getItemAtPosition(arg2);
                if(arg2==0){
                    Toast.makeText(getApplicationContext(), "Great answer!!",
                            Toast.LENGTH_LONG).show();
                    BuggerService.setSYSTEM_GlobalPoints(1, "Choose me on best friend");
                }
                selectContact = o.toString();
                Toast.makeText(getBaseContext(), o.toString(), Toast.LENGTH_SHORT).show();
                //============================================
                // Display number of contact on click.
                //===========================================

                //   iSelectedNum = arg2;
                String[] vals = selectContact.split(":");
                String name = vals[0];
                String num = vals[1];
                // Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder1 = new AlertDialog.Builder(Main2Activity.this);
                builder1.setMessage("Is " + name + " is your best friend?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String[] vals = selectContact.split(":");
                                String name = vals[0];
                                String num = vals[1];
                                String msg = "Budd-E: Why does he love you more than he loves me?";
                                boolean answer = sendSMS(num, msg, name,Main2Activity.this);
                                if(answer) {
                                    Toast.makeText(getApplicationContext(), "I send \"" + msg + "\" to " + name + "!!",
                                            Toast.LENGTH_LONG).show();
                                    BuggerService.setSYSTEM_GlobalPoints(-1, "Didn't choose me on best friend");
                                }else{
                                    Toast.makeText(getApplicationContext(), "You should have picked me!!",
                                            Toast.LENGTH_LONG).show();
                                    BuggerService.setSYSTEM_GlobalPoints(-1,"Didn't choose me on best friend");
                                }
                                cleanListOptions();
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                cleanListOptions();
                                startSmsSession();
                                return;
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        if ((settings.getBoolean(SETTINGS_CALLED_MAIN_ONCE, false))) {

            if (!checkScreenAndLock(this)) {
                Functions.writeToSettings(SETTINGS_CALLED_MAIN_ONCE, false, this);
                return;
            } else {
                Toast.makeText(this, "Missed you", Toast.LENGTH_SHORT).show();
            }

        }
        if ((settings.getBoolean(JUST_WOKE_UP, false))) {

            BuggerService.getInstance().wakeUpJon();
            forceWakeUp();
        }
        if ((settings.getBoolean(SAY_LOVE, false))) {

            popUpForRequest();
        }


        if ((settings.getBoolean(SMS_SEND, false))) {

            startSmsSession();
        }

        if (allImageButtons != null) {
            for (int i = 0; i < allImageButtons.size(); i++)
                allImageButtons.get(i).setImageAlpha(128);
        }
        if (BuggerService.getInstance().getIsTrip()) {
            //   mCancelTrip.setVisibility(View.VISIBLE); //TODO
        }
        Intent intent = getIntent();
        if (intent != null && intent.getBooleanExtra(Constants.JonIntents.ACTION_MAIN_SET_NOTIFICATION, false)) {
            BuggerService.getInstance().setNotif();
        } else if (intent != null && intent.getStringExtra(JUST_WOKE_UP) != null) {
//            BuggerService.getInstance().wakeUpJon();

            //          talkAboutWakeUp();
        }
        invalidateOptionsMenu();
        if (readyToInvalidate && BuggerService.getIsServiceUP()) {
            invalidateRelationsData();
            invalidateStatusParam();
            //         BuggerService.getInstance().onRefresh(gifImageView, mainLayout, chatImage, this); //TODO
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
       /*
        if (!isMyServiceRunning(BuggerService.class)) {
            Log.d(TAG, "onNewIntent: rerunning bugger");
        }
        */

        if (intent != null && intent.getBooleanExtra(JUST_WOKE_UP, false)) {
            BuggerService.getInstance().wakeUpJon();

            talkAboutWakeUp();
        }

        if (intent.getBooleanExtra("sendJonToSleep", false)) {
            //    BuggerService.getInstance().sendJonToSleep(gifImageView, mainLayout, chatImage, MainActivity.this); //TODO

        }

        if (intent.getBooleanExtra("FROM_NOTIF", false)) {
            BuggerService.getInstance().setNotif();
        } else if (intent.getBooleanExtra("wakeUpOptions", false)) {
            //    forceWakeUp();                //TODO
        } else if (intent.getBooleanExtra("tutorial_done", false)) {
            SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
            createJonFolder();
            if (!settings.getBoolean(IS_INSTALLED, false)) {
                Functions.oneTimeFunctions.setUserName(this, TAG);
                //        BuggerService.getInstance().createLogger();
                takeScreenshot(this, "I was born");
                addCalendarMeeting(this);
                BuggerService.getInstance().writeToSettings(IS_INSTALLED, true);
            }
        }
    }


    private void initializeAnimations() {
        mAnimationIndex = 0;
        mOnAnimation = new AlphaAnimation(0.0f, 0.5f); // Change alpha from fully visible to invisible
        mOnAnimation.setDuration(animationTime); // duration - half a second
        mOnAnimation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        mOnAnimation.setRepeatCount(0); // Repeat animation infinitely
        mOffAnimation = new AlphaAnimation(0.5f, 0.0f);
        mOffAnimation.setDuration(animationTime);
        mOffAnimation.setInterpolator(new LinearInterpolator());
        mOffAnimation.setRepeatCount(0);
        mOffAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {
                allImageButtons.get(mAnimationIndex).setImageAlpha(128);
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                allImageButtons.get(mAnimationIndex).setVisibility(View.INVISIBLE);
                if (mAnimationIndex - 1 < 0) {
                    startedAnimation = false;
                    return;
                } else {
                    allImageButtons.get(mAnimationIndex).clearAnimation();
                    mAnimationIndex--;
                    allImageButtons.get(mAnimationIndex).startAnimation(mOffAnimation);
                    return;
                }
            }
        });
        mOnAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {
                allImageButtons.get(mAnimationIndex).setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                if (mAnimationIndex + 1 >= allImageButtons.size()) {
                    startedAnimation = false;
                    return;
                } else {
                    allImageButtons.get(mAnimationIndex).clearAnimation();
                    mAnimationIndex++;
                    allImageButtons.get(mAnimationIndex).startAnimation(mOnAnimation);
                    return;
                }
            }
        });
    }

    private void initializeGui() {
        openPoke = (ImageButton) findViewById(R.id.poke_image);
        openChat = (ImageButton) findViewById(R.id.open_chat_image);
        openGame = (ImageButton) findViewById(R.id.game_image);
        openTrip = (ImageButton) findViewById(R.id.trip_image);
        openMore = (ImageButton) findViewById(R.id.more_image);

        allImageButtons = new ArrayList<>();
        allImageButtons.add(openPoke);
        allImageButtons.add(openChat);
        allImageButtons.add(openGame);
        allImageButtons.add(openTrip);
        allImageButtons.add(openMore);
        for (int i = 0; i < allImageButtons.size(); i++) {
            allImageButtons.get(i).setVisibility(View.INVISIBLE);
            allImageButtons.get(i).setImageAlpha(128);
            allImageButtons.get(i).setOnTouchListener(imageButtonListner);

        }
        mGifImageView.setDrawingCacheEnabled(true);
        mGifImageView.setOnTouchListener(changeColorListener);
        chatListView = (ListView) findViewById(R.id.chat_list);
        //  chatListView.setVisibility(View.GONE);
        ViewTreeObserver vto = mainLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mainLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                if (!getApplicationContext().getSharedPreferences(PREFS_NAME, 0).getBoolean(IS_INSTALLED, false)) {
                    takeScreenshot(Main2Activity.this, "I was born");
                    String uName = getUserName(Main2Activity.this);
                    BuggerService.getInstance().sendMessage(uName + " Just joined and Budd-E's mood is "+BuggerService.getInstance().getMood().getClass().getSimpleName());
                    Functions.oneTimeFunctions.createShortcut(Main2Activity.this);
                    addCalendarMeeting(Main2Activity.this);
                    BuggerService.getInstance().writeToSettings(IS_INSTALLED, true);
                }
            }
        });
        readyToInvalidate = true;
    }


    @Override
    public void doPositive() {
        if (currendPressed != null) {
            currendPressed.setImageAlpha(HALF_INT_ALPHA);
        }
        BuggerService.getInstance().wakeUpJon();
        refreshLayout();
        talkAboutWakeUp();
    }

    @Override
    public void doNegative() {
        if (currendPressed != null) {
            currendPressed.setImageAlpha(HALF_INT_ALPHA);
        }
        Toast.makeText(Main2Activity.this, "...ZZZzzzZZZzzz!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showDialog() {
        DialogFragment newFragment = MyAlertDialogFragment
                .newInstance(R.string.alert_dialog_Wake_up_buttons_title, "Wake up!", "Shh...", getClass().getName(), null);
        newFragment.show(getSupportFragmentManager(), "dialog");
    }

    private void forceWakeUp() {
        if (chatListView == null) {
            chatListView = (ListView) findViewById(R.id.chat_list);
        }
        close();
        mGifImageView.setImageAlpha(HALF_INT_ALPHA);
        chatListView.setVisibility(View.VISIBLE);
        BuggerService.getInstance().wakeUpJon();
        List<String> legendList = new ArrayList<>();
        legendList.add("Put earplugs and go back to bad");
        legendList.add("Stay awake");
        legendList.add("Ohhh... Sorry");
        chatListView.setAdapter(new ChatListViewAdapter(this, R.layout.layout_for_listview, legendList));

        chatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Functions.writeToSettings(JUST_WOKE_UP, false, Main2Activity.this);
                        Toast.makeText(Main2Activity.this, "Ok.. But it keep it down!", Toast.LENGTH_LONG).show();
                        SharedPreferences settings1 = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
                        int noiseLevelSettings = settings1.getInt(SETTINGS_NOISE_LEVEL, INITIAL_NOISE_LEVEL);
                        Functions.writeToSettings(SETTINGS_NOISE_LEVEL, noiseLevelSettings + 5, Main2Activity.this);
                        BuggerService.setSYSTEM_GlobalPoints(-1, "You woke me up by making noise");
                        BuggerService.getInstance().sendJonToSleep();
                        Main2Activity.this.refreshLayout();
                        break;
                    case 1:
                        Functions.writeToSettings(JUST_WOKE_UP, false, Main2Activity.this);
                        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
                        int tired = settings.getInt(SETTINGS_TIRED_POINTS, SETTINGS_INITIAL_TIRED_POINTS);
                        if (tired >= SETTINGS_INITIAL_TIRED_POINTS) {
                            Toast.makeText(Main2Activity.this, "Ok", Toast.LENGTH_LONG).show();
                            BuggerService.setSYSTEM_GlobalPoints(-1, "You woke me up by making noise");
                        } else {
                            Toast.makeText(Main2Activity.this, "Nope, too tired", Toast.LENGTH_LONG).show();
                            BuggerService.getInstance().sendJonToSleep();
                            Main2Activity.this.refreshLayout();
                        }
                        chatListView.setAdapter(new ChatListViewAdapter(Main2Activity.this, R.layout.layout_for_listview, new ArrayList<String>()));
                        break;
                    case 2:
                        Functions.writeToSettings(JUST_WOKE_UP, false, Main2Activity.this);
                        Toast.makeText(Main2Activity.this, BuggerService.getInstance().getRandomInsult() + " I'm going back to sleep", Toast.LENGTH_LONG).show();
                        BuggerService.setSYSTEM_GlobalPoints(-2, "You woke me up by making noise");
                        BuggerService.getInstance().sendJonToSleep();
                        Main2Activity.this.refreshLayout();
                        chatListView.setAdapter(new ChatListViewAdapter(Main2Activity.this, R.layout.layout_for_listview, new ArrayList<String>()));
                        break;
                }
                cleanListOptions();
            }
        });
    }


    private void talkAboutWakeUp() {
        if (chatListView == null) {
            chatListView = (ListView) findViewById(R.id.chat_list);
        }
        close();
        mGifImageView.setImageAlpha(HALF_INT_ALPHA);
        chatListView.setVisibility(View.VISIBLE);
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
                        Toast.makeText(Main2Activity.this, "What happened? Checking Ynet..", Toast.LENGTH_LONG).show();
                        BuggerService.setSYSTEM_GlobalPoints(1, "Woke me up to tell me something important");
                        cleanListOptions();
                        talkAboutNews();
                        break;
                    case 1:
                        Toast.makeText(Main2Activity.this, BuggerService.getInstance().getRandomInsult() + " I'm going back to sleep", Toast.LENGTH_LONG).show();
                        BuggerService.setSYSTEM_GlobalPoints(-2, "You woke me up for nothing");
                        BuggerService.getInstance().sendJonToSleep();
                        Main2Activity.this.refreshLayout();
                        cleanListOptions();
                        break;
                    case 2:
                        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
                        int tired = settings.getInt(SETTINGS_TIRED_POINTS, SETTINGS_INITIAL_TIRED_POINTS);
                        if (tired > SETTINGS_INITIAL_TIRED_POINTS) {
                            Toast.makeText(Main2Activity.this, "It's ok, I'll stay with you", Toast.LENGTH_LONG).show();
                            cleanListOptions();
                            return;
                        } else {
                            Toast.makeText(Main2Activity.this, BuggerService.getInstance().getRandomInsult() + " I'm going back to sleep", Toast.LENGTH_LONG).show();
                            BuggerService.setSYSTEM_GlobalPoints(-1, "You woke me up for nothing");
                            BuggerService.getInstance().sendJonToSleep();
                            Main2Activity.this.refreshLayout();
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
        if (chatListView == null) {
            chatListView = (ListView) findViewById(R.id.chat_list);
        }
        close();
        mGifImageView.setImageAlpha(HALF_INT_ALPHA);
        chatListView.setVisibility(View.VISIBLE);
        chatListView.setAdapter(new ChatListViewAdapter(this, R.layout.layout_for_listview, newsList));
        chatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (BuggerService.getInstance().shouldIBeNice()) {
                    String toStartWith = chatListView.getItemAtPosition(position).toString() + ". What you think about it??";
                    Toast.makeText(Main2Activity.this, "Wow.. can't sleep now..", Toast.LENGTH_SHORT).show();
                    cleanListOptions();
                    startActivity(new Intent(Main2Activity.this,
                            ChatActivity.class).putExtra(CHAT_START_MESSAGE, toStartWith));
                } else {
                    Toast.makeText(Main2Activity.this, "I don't care, let me sleep.", Toast.LENGTH_SHORT).show();
                    BuggerService.getInstance().sendJonToSleep();
                    Main2Activity.this.refreshLayout();
                    cleanListOptions();
                }
            }
        });
    }

    private void cleanListOptions() {
        chatListView.setVisibility(View.GONE);
        chatListView.setAdapter(null);
        mGifImageView.setImageAlpha(FULL_INT_ALPHA);
    }
}
