package com.shalom.itai.theservantexperience.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.Toast;

import com.shalom.itai.theservantexperience.R;
import com.shalom.itai.theservantexperience.chatBot.ChatActivity;
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
import com.shalom.itai.theservantexperience.utils.Client;
import com.shalom.itai.theservantexperience.utils.Constants;
import com.shalom.itai.theservantexperience.utils.Functions;

import java.util.ArrayList;
import java.util.function.Function;

import static com.shalom.itai.theservantexperience.utils.Constants.CHAT_START_MESSAGE;
import static com.shalom.itai.theservantexperience.utils.Constants.ENTITY_NAME;
import static com.shalom.itai.theservantexperience.utils.Constants.HALF_INT_ALPHA;
import static com.shalom.itai.theservantexperience.utils.Constants.IS_INSTALLED;
import static com.shalom.itai.theservantexperience.utils.Constants.JonIntents.ASK_TO_PLAY;
import static com.shalom.itai.theservantexperience.utils.Constants.JonIntents.JUST_WOKE_UP;
import static com.shalom.itai.theservantexperience.utils.Constants.PREFS_NAME;
import static com.shalom.itai.theservantexperience.utils.Constants.SETTINGS_CALLED_MAIN_ONCE;
import static com.shalom.itai.theservantexperience.utils.Constants.SETTINGS_IS_ASLEEP;
import static com.shalom.itai.theservantexperience.utils.Functions.checkScreenAndLock;
import static com.shalom.itai.theservantexperience.utils.Functions.createJonFolder;
import static com.shalom.itai.theservantexperience.utils.Functions.oneTimeFunctions.addCalendarMeeting;
import static com.shalom.itai.theservantexperience.utils.Functions.getUserName;
import static com.shalom.itai.theservantexperience.utils.Functions.takeScreenshot;

public class Main2Activity extends ToolBarActivityNew implements DialogCaller {
    public final static String TAG = "Main2Activity";
    private ImageButton openPoke;
    private ImageButton openChat;
    private ImageButton openGame;
    private ImageButton openTrip;
    private ImageButton openMore;
    private int animationTime = 70;
    private boolean isShown = false;
    private int mAnimationIndex;
    AlphaAnimation mOnAnimation;
    AlphaAnimation mOffAnimation;
    ArrayList<ImageButton> allImageButtons;
    boolean startedAnimation = false;
    private ImageButton currendPressed;
    private boolean wasClosedFromOutside = false;
    private boolean readyToInvalidate;
    private int moodIndex = 0;
    private Mood[] moodArr = new Mood[]{Sad.getInstance(), Angry.getInstance(), Fine.getInstance(), Optimistic.getInstance(), Board.getInstance(), Calm.getInstance(), Happy.getInstance(), Excited.getInstance(), Sleep.getInstance()};
    private final View.OnTouchListener changeColorListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int color;// = Color.CYAN;
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
                ImageButton imageView =null;//= ((ImageButton) v);
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
                                if (moodIndex == moodArr.length)
                                    moodIndex = 0;

                                Client.getInstance(Functions.getUserName(Main2Activity.this)).sendMessage("Budd-E's mood at itai is now "+moodArr[moodIndex].getClass().getSimpleName().toLowerCase());


                                BuggerService.getInstance().setCurrntMood(moodArr[moodIndex]);
                                moodIndex++;
                                refreshLayout();
                                return true;

                            }

                            if (imageView == openChat) {
                                //refreshLayout2();


                                Main2Activity.this.startActivity(new Intent(Main2Activity.this, ChatActivity.class).putExtra(CHAT_START_MESSAGE, ENTITY_NAME+" is here"));
                                Main2Activity.this.overridePendingTransition(R.anim.slide_bottom_in, R.anim.slide_top_out);
                            }
                            if (imageView == openGame) {
                                //    refreshLayout3();
                                Main2Activity.this.startActivity(new Intent(Main2Activity.this, MatchesGameActivity.class).putExtra(ASK_TO_PLAY, false));
                                Main2Activity.this.overridePendingTransition(R.anim.slide_bottom_in, R.anim.slide_top_out);
                            }
                            if (imageView == openTrip) {
                                Main2Activity.this.startActivity(new Intent(Main2Activity.this, TripActivity.class));
                                Main2Activity.this.overridePendingTransition(R.anim.slide_bottom_in, R.anim.slide_top_out);
                            }
                            if (imageView == openPoke) {
                                //     BuggerService.setSYSTEM_GlobalPoints(-5,"check");
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


    @Override
    protected void hideBeneath(ConstraintLayout layoutAppeared) {
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_main_new, R.menu.tool_bar_options, true, -1);
        // getSupportActionBar().setIcon(R.drawable.title);
        Functions.oneTimeFunctions.setUserName(this,TAG);
        String uName = getUserName(this);
        Client.getInstance(uName.replace(" ","_")).sendMessage(uName + " Just joined");
        initializeAnimations();
        initializeGui();

        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        createJonFolder();
        if (!settings.getBoolean(IS_INSTALLED, false)) {
            Functions.oneTimeFunctions.setUserName(this, TAG);

        }
        if (settings.getBoolean(SETTINGS_IS_ASLEEP, false))
            BuggerService.getInstance().sendJonToSleep();
        else
            BuggerService.getInstance().wakeUpJon();
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        if((settings.getBoolean(SETTINGS_CALLED_MAIN_ONCE, false)) ){
            Functions.writeToSettings(SETTINGS_CALLED_MAIN_ONCE,false,this);
            if(checkScreenAndLock(this)){
                return;
            }
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
        } else if (intent != null && intent.getBooleanExtra(JUST_WOKE_UP, false)) {
            BuggerService.getInstance().wakeUpJon();

            //    forceWakeUp();                //TODO
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

        ViewTreeObserver vto = mainLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mainLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                if (!getApplicationContext().getSharedPreferences(PREFS_NAME, 0).getBoolean(IS_INSTALLED, false)) {
                    takeScreenshot(Main2Activity.this, "I was born");
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
        //   talkAboutWakeUp();
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
                .newInstance(R.string.alert_dialog_Wake_up_buttons_title, "Wake up!", "Shh...", getClass().getName());
        newFragment.show(getSupportFragmentManager(), "dialog");
    }
}
