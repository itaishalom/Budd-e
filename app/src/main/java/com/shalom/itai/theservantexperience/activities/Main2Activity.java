package com.shalom.itai.theservantexperience.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;

import com.shalom.itai.theservantexperience.R;
import com.shalom.itai.theservantexperience.chatBot.ChatActivity;
import com.shalom.itai.theservantexperience.services.BuggerService;

import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;

import static com.shalom.itai.theservantexperience.utils.Constants.CHAT_START_MESSAGE;
import static com.shalom.itai.theservantexperience.utils.Constants.JonIntents.ASK_TO_PLAY;

public class Main2Activity extends ToolBarActivityNew {
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
    ConstraintLayout mainLayout;
    private final View.OnTouchListener changeColorListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Bitmap bmp = Bitmap.createBitmap(v.getDrawingCache());
            int color = bmp.getPixel((int) event.getX(), (int) event.getY());
            if (color == Color.TRANSPARENT) {
                return false;
            } else {
                if (!startedAnimation) {
                    startedAnimation = !startedAnimation;
                    if (!isShown) {
                        allImageButtons.get(0).clearAnimation();
                        allImageButtons.get(0).startAnimation(mOnAnimation);
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
                ImageButton imageView = ((ImageButton) v);
                try {


                    Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

                    int pixel = bitmap.getPixel(x, y);

                    //now check alpha for transparency
                    int alpha = Color.alpha(pixel);
                    if (alpha != 0) {
                        if (((ImageButton) v).getImageAlpha() != 255) {
                            ((ImageButton) v).setImageAlpha(255);
                            if (v == openChat) {
                                //refreshLayout2();
                                Main2Activity.this.startActivity(new Intent(Main2Activity.this, ChatActivity.class).putExtra(CHAT_START_MESSAGE, "Jon is here"));
                                Main2Activity.this.overridePendingTransition(R.anim.slide_bottom_in, R.anim.slide_top_out);
                            }
                            if (v == openGame) {
                            //    refreshLayout3();
                               Main2Activity.this.startActivity(new Intent(Main2Activity.this, MatchesGameActivity.class).putExtra(ASK_TO_PLAY, false));
                                Main2Activity.this.overridePendingTransition(R.anim.slide_bottom_in, R.anim.slide_top_out);
                            }
                            if (v == openTrip) {
                                Main2Activity.this.startActivity(new Intent(Main2Activity.this, TripActivity.class));
                                Main2Activity.this.overridePendingTransition(R.anim.slide_bottom_in, R.anim.slide_top_out);
                            }
                            if (v == openPoke) {
                           //     BuggerService.setSYSTEM_GlobalPoints(-5,"check");
                            }
                            // MainActivity.getInstance().overridePendingTransition(R.anim.slide_bottom_in, R.anim.slide_top_out);
                            //   Main2Activity.this.overridePendingTransition(R.anim.slide_bottom_in, R.anim.slide_top_out);

                        } else {
                            ((ImageButton) v).setImageAlpha(128);
                        }
                    }
                } catch (IllegalArgumentException e) {
                    return true;
                }

            }
            return true; //we've handled the event
            /*
            Bitmap bmp = Bitmap.createBitmap(v.getDrawingCache());
            v.buildDrawingCache(false);
            int color = bmp.getPixel((int) event.getX(), (int) event.getY());
            if (color == Color.TRANSPARENT) {
                return false;
            } else {
                if (((ImageButton) v).getImageAlpha() != 255) {
                    ((ImageButton) v).setImageAlpha(255);
                } else {
                    ((ImageButton) v).setImageAlpha(128);
                }
                return true;
            }

        }*/
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        if (allImageButtons != null) {
            for (int i = 0; i < allImageButtons.size(); i++)
                allImageButtons.get(i).setImageAlpha(128);
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_main_new,R.menu.tool_bar_options);
        getSupportActionBar().setIcon(R.drawable.title);
        mAnimationIndex = 0;
        mOnAnimation = new AlphaAnimation(0.0f, 0.5f); // Change alpha from fully visible to invisible
        mOnAnimation.setDuration(animationTime); // duration - half a second
        mOnAnimation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        mOnAnimation.setRepeatCount(0); // Repeat animation infinitely

        mOffAnimation = new AlphaAnimation(0.5f, 0.0f); // Change alpha from fully visible to invisible
        mOffAnimation.setDuration(animationTime); // duration - half a second
        mOffAnimation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        mOffAnimation.setRepeatCount(0); // Repeat animation infinitely


        //     mBlinkanimation.setFillAfter(false);//to keep it at 0 when animation ends
        //     mBlinkanimation.setRepeatMode(Animation.REVERSE);
        openPoke = (ImageButton) findViewById(R.id.poke_image);
        openChat = (ImageButton) findViewById(R.id.chat_image);
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

        mOnAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {
                allImageButtons.get(mAnimationIndex).setVisibility(View.VISIBLE);
                //   allImageButtons.get(mAnimationIndex).setOnTouchListener(imageButtonListner);
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

        mOffAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {
                allImageButtons.get(mAnimationIndex).setImageAlpha(128);
                //       allImageButtons.get(mAnimationIndex).setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                allImageButtons.get(mAnimationIndex).setVisibility(View.INVISIBLE);

                //         allImageButtons.get(mAnimationIndex).setOnTouchListener(null);
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

    //    GifImageView gifImageView = (GifImageView) findViewById(R.id.GifImageView);

     //   gifImageView.setImageResource(R.drawable.optimistic_gif);
        mGifImageView.setDrawingCacheEnabled(true);
        mGifImageView.setOnTouchListener(changeColorListener);

        // openPoke.buildDrawingCache(true);
        //    openPoke.setOnTouchListener(imageButtonListner);


//        getActionBar().setIcon(R.drawable.title);
        //     super.onCreate(savedInstanceState);
        //       setContentView(R.layout.activity_main_new);
        mainLayout = (ConstraintLayout) findViewById(R.id.main_layout);


        //   BuggerService.getInstance().onRefresh2(gifImageView, mainLayout, openChat, this);

    }
}
