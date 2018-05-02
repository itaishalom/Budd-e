package com.shalom.itai.theservantexperience.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.shalom.itai.theservantexperience.R;
import com.shalom.itai.theservantexperience.services.BuggerService;
import com.shalom.itai.theservantexperience.utils.FontTextView;
import com.shalom.itai.theservantexperience.utils.Functions;

import java.util.concurrent.LinkedBlockingQueue;

import static android.view.View.VISIBLE;
import static com.shalom.itai.theservantexperience.utils.Constants.ENTITY_NAME;
import static com.shalom.itai.theservantexperience.utils.Constants.JonIntents.ASK_TO_PLAY;
import static com.shalom.itai.theservantexperience.utils.Constants.PREFS_NAME;
import static com.shalom.itai.theservantexperience.utils.Constants.SETTINGS_IS_NOTIF_ON;
import static com.shalom.itai.theservantexperience.utils.Constants.SETTINGS_USER_LOOSE;
import static com.shalom.itai.theservantexperience.utils.Constants.SETTINGS_USER_WINS;
import static com.shalom.itai.theservantexperience.utils.Constants.SETTING_SHOW_EXPLAIN_GAME;
import static com.shalom.itai.theservantexperience.utils.Functions.throwRandom;

public class MatchesGameActivity extends ToolBarActivityNew implements DialogCaller {
    private MatchesGameActivity act;
    private boolean turnIsBegan = false;
    private int matchesCounterTurn = 0;
    private boolean takeFromHeapOne = false;
    private int heap1 = 12;
    private int heap2 = 12;
    private boolean isFirstCase = false;
    private final int BURN_DIRECTION = -1;
    private final int UNBURN_DIRECTION = 1;
    private TextView heap1_text;
    private TextView heap2_text;
    private ImageButton fire;
    private final String NUM_OF_MATCHES = "TREES LEFT: ";
    private boolean isSnackShown;
    private MediaPlayer mediaPlayer;
    private boolean isJonPlay = true;
    private LinkedBlockingQueue<String> snacks;
    //  private GifImageView gifImageView;

    public String getHeader(){
        return  "Game";
    }
    public String getfConten(){
        return  "Budd-E wants to play a game";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState, R.layout.activity_matches_game_2, R.menu.tool_bar_game_options,false,-1);
        // setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar));
        Functions.writeToSettings(SETTINGS_IS_NOTIF_ON,false,this);
        snacks = new LinkedBlockingQueue<>();
        heap1_text = (TextView) findViewById(R.id.heap1_data);
        heap2_text = (TextView) findViewById(R.id.heap2_data);


        fire = (ImageButton) findViewById(R.id.fire);
        fire.setImageAlpha(128);
        //   arr = new View[]{fire, newGame};
        act = this;
        Intent intent = getIntent();
        if (intent == null) {
            showDialog();
        } else if (intent.getBooleanExtra(ASK_TO_PLAY, true)) {
            showDialog();
        } else {

            doPositive();
        }
        //  firstStep();
    }

    @Override
    protected void newGame() {
        turnIsBegan = false;
        matchesCounterTurn = 0;
        takeFromHeapOne = false;
        heap1 = 12;
        heap2 = 12;
        restoreMatches();
        setBrightness(0.5f);
        firstStep();
    }

    private void setBrightness(float brightness) {
/*
        //constrain the value of brightness
        if (brightness < 1)
            brightness = 1;
        else if (brightness > 255)
            brightness = 255;

        Settings.System.putInt(this.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS, brightness);
*/
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.screenBrightness = brightness;// 100 / 100.0f;
        getWindow().setAttributes(lp);

        // startActivity(new Intent(this,RefreshScreen.class));

    }
    @Override
    protected void invalidateGameStatus(int wins, int loose) {
        invalidateWins(wins);
        invalidateLooses(loose);

    }
    private void invalidateWins(int wins){
        FontTextView winsText = (FontTextView) findViewById(R.id.my_wins_ind);
        winsText.setText(String.valueOf(wins));
    }

    private void invalidateLooses(int loose){
        FontTextView looseText = (FontTextView) findViewById(R.id.my_loses_ind);
        looseText.setText(String.valueOf(loose));
    }

    private void endOfGame(boolean isJonWon) {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, 0);
        if (isJonWon) {
            BuggerService.setSYSTEM_GlobalPoints(1, "I won a game!");
            String JON_WINS = ENTITY_NAME + " beats the user";
            toastThis(JON_WINS);
            int newNum = preferences.getInt(SETTINGS_USER_LOOSE, 0)+1;
            Functions.writeToSettings(SETTINGS_USER_LOOSE,newNum,this);
            mediaPlayer = MediaPlayer.create(this, R.raw.jon_wins);
            invalidateLooses(newNum);
            setBrightness(0.9f);

        } else {
            BuggerService.setSYSTEM_GlobalPoints(-1, "I lost a game!");
            setBrightness(0.1f);
            int newNum = preferences.getInt(SETTINGS_USER_WINS, 0)+1;
            mediaPlayer = MediaPlayer.create(this, R.raw.jon_lose);
            Functions.writeToSettings(SETTINGS_USER_WINS,newNum,this);
            invalidateWins(newNum);
        }
        AudioManager am =
                (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        am.setStreamVolume(
                AudioManager.STREAM_MUSIC,
                am.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                0);
        mediaPlayer.start();
//mediaPlayer.getDuration()
        new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                mediaPlayer.stop();
                newGame();

            }
        }.start();

    }

    private void setUserTurn() {
        mGifImageView.setAlpha(0.3f);
        isJonPlay = false;
        fire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fire.setImageAlpha(255);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fire.setImageAlpha(128);
                    }
                }, 500);
                if (matchesCounterTurn != 0) {

                    if (takeFromHeapOne) {
                        changeMatchesVisibility(R.id.heap1, R.drawable.match_burned, View.INVISIBLE);
                        heap1 -= matchesCounterTurn;
                    } else {
                        changeMatchesVisibility(R.id.heap2, R.drawable.match_burned, View.INVISIBLE);
                        heap2 -= matchesCounterTurn;
                    }
                    matchesCounterTurn = 0;
                    turnIsBegan = false;
                    takeFromHeapOne = false;
                    secondStep();
                } else {
                    toastThis("no trees selected!");
                }
            }
        });
    }

    private void setJonTurn() {
        isJonPlay = true;
        mGifImageView.setAlpha(1.0f);
        fire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toastThis(ENTITY_NAME + "'s turn!");
            }
        });
    }


    private void changeMatchesVisibility(int id_of_heap, int status_of_match, int Visibility) {
        ConstraintLayout ll = (ConstraintLayout) findViewById(id_of_heap);
        final int childCount = ll.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View img = ll.getChildAt(i);
            if (img instanceof ImageView) {
                ImageView match = (ImageView) img;
                if (match.getDrawable().getConstantState() == ContextCompat.getDrawable(act, status_of_match).getConstantState()) {
                    match.setVisibility(Visibility);
                }
            }
        }
        heap1_text.setText(NUM_OF_MATCHES + heap1);
        heap2_text.setText(NUM_OF_MATCHES + heap2);
    }

    private void animateBurnMatch(final View view, int direction) {
        switch (direction) {
            case BURN_DIRECTION:
                view.animate().translationY(-50);
                break;
            case UNBURN_DIRECTION:
                view.animate().translationY(0);
        }
    }

    public void onImageClick(View view) {
        if (!isJonPlay) {
            if (!turnIsBegan) {
                ImageView match = (ImageView) view;
                if (match.getDrawable().getConstantState() == ContextCompat.getDrawable(this, R.drawable.match).getConstantState()) {
                    match.setImageResource(R.drawable.match_burned);
                    animateBurnMatch(match, BURN_DIRECTION);
                    matchesCounterTurn++;

                    int parentId = ((View) view.getParent()).getId();
                    if (parentId == R.id.heap1) {
                        takeFromHeapOne = true;
                    } else {
                        takeFromHeapOne = false;
                    }
                    turnIsBegan = true;

                } else {
                    match.setImageResource(R.drawable.match);
                    animateBurnMatch(match, UNBURN_DIRECTION);
                    matchesCounterTurn--;
                    if (matchesCounterTurn == 0) {
                        takeFromHeapOne = false;
                        turnIsBegan = false;
                    }
                }


            } else {

                int parentId = ((View) view.getParent()).getId();
                ImageView match = (ImageView) view;
                if (match.getDrawable().getConstantState() == ContextCompat.getDrawable(this, R.drawable.match).getConstantState()) {
                    if ((parentId == R.id.heap1 && takeFromHeapOne && matchesCounterTurn < 4) || (parentId == R.id.heap2 && !takeFromHeapOne && matchesCounterTurn < 4)) {
                        match.setImageResource(R.drawable.match_burned);
                        animateBurnMatch(match, BURN_DIRECTION);
                        matchesCounterTurn++;
                    }
                } else {
                    match.setImageResource(R.drawable.match);
                    animateBurnMatch(match, UNBURN_DIRECTION);
                    matchesCounterTurn--;
                    if (matchesCounterTurn == 0) {
                        takeFromHeapOne = false;
                        turnIsBegan = false;
                    }
                }
            }
        }
    }


    class myCallback extends Snackbar.Callback {
        @Override
        public void onDismissed(Snackbar snackbar, int event) {
            isSnackShown = false;
            String newInfo = snacks.poll();
            if (newInfo != null) {
                snackbar = Snackbar
                        .make(mainLayout, newInfo, Snackbar.LENGTH_SHORT);
                Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
                layout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                View snackbarView = snackbar.getView();
                TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.BLACK);

                snackbar.addCallback(new myCallback());
                snackbar.show();
            }
        }

        @Override
        public void onShown(Snackbar snackbar) {

        }
    }


    private void toastThis(String info) {
        if (!isSnackShown) {
            isSnackShown = true;
            Snackbar snackbar = Snackbar
                    .make(mainLayout, info, Snackbar.LENGTH_SHORT);
            Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
            layout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            View snackbarView = snackbar.getView();
            TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.BLACK);
            snackbar.addCallback(new myCallback());
            snackbar.show();
        } else {
            try {
                snacks.put(info);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //    Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
    }


    private void secondStep() {
        setJonTurn();
        heap1_text.setText(NUM_OF_MATCHES + heap1);
        heap2_text.setText(NUM_OF_MATCHES + heap2);
        if (isFirstCase) {
            if (heap1 <= 0) {
                endOfGame(true);
                return;
            }
            if (heap2 <= 0) {
                endOfGame(true);
                return;
            }

        } else {
            if (heap1 <= 0) {
                endOfGame(true);
                return;
            }

            if (heap2 <= 0) {

                endOfGame(true);
                return;
            }
        }
        if ((heap1 == 1) && (heap2 == 1)) {
            toastThis(ENTITY_NAME + " burned 1 tree from heap number 2");
            jonBurn(R.id.heap2, 1);
            toastThis("The user beats " + ENTITY_NAME);
            endOfGame(false);
            return;
        }
        if ((heap1 == 0) || (heap2 == 0)) {
            return;
        } else {
            firstStep();
        }
    }


    private void firstStep() {
        setJonTurn();
        heap1_text.setText(NUM_OF_MATCHES + heap1);
        heap2_text.setText(NUM_OF_MATCHES + heap2);
        int randomNum = throwRandom(4, 1);

        if (heap1 > 1) {
            isFirstCase = true;
            if ((heap1 < 5) && (heap1 > 1) && ((heap1 - randomNum) <= 0)) {
                do {
                    randomNum = throwRandom(4, 1);
                }
                while (heap1 - randomNum < 1);
            }
            heap1 = heap1 - randomNum;
            toastThis(ENTITY_NAME + " burned " + randomNum +
                    " trees from heap number 1");

            jonBurn(R.id.heap1, randomNum);

        } else {
            isFirstCase = false;
            if ((heap2 < 5) && (heap2 > 1)
                    && ((heap2 - randomNum) <= 0)) {
                do {
                    randomNum = throwRandom(4, 1);
                }
                while (heap2 - randomNum < 1);
            }

            heap2 = heap2 - randomNum;
            toastThis(ENTITY_NAME + " burned " + randomNum + " trees from heap number 2");
            //        toastThis("Number of matches in the first heap is:" + heap1);
            //      toastThis("Number of matches in the second heap is:" + heap2);
            jonBurn(R.id.heap2, randomNum);
        }
        toastThis("Select number of trees to burn:");
    }

    private void restoreMatches() {
        int[] ids = new int[]{R.id.heap1, R.id.heap2};
        for (int id : ids) {
            ConstraintLayout ll = (ConstraintLayout) findViewById(id);
            final int childCount = ll.getChildCount();
            for (int i = 0; i < childCount; i++) {

                View img = ll.getChildAt(i);
                if (img instanceof ImageView) {
                    final ImageView match = (ImageView) img;
                    if (match.getDrawable().getConstantState() == ContextCompat.getDrawable(act, R.drawable.match_burned).getConstantState()) {
                        match.setImageResource(R.drawable.match);
                        animateBurnMatch(match, UNBURN_DIRECTION);
                    }
                }
            }
            changeMatchesVisibility(id, R.drawable.match, VISIBLE);
        }
    }

    private void jonBurn(final int id_of_heap, int numOfBurns) {
        ConstraintLayout ll = (ConstraintLayout) findViewById(id_of_heap);
        final int childCount = ll.getChildCount();
        for (int i = 0; i < childCount && numOfBurns > 0; i++) {

            View img = ll.getChildAt(i);
            if (img instanceof ImageView) {
                final ImageView match = (ImageView) img;
                if (match.getDrawable().getConstantState() == ContextCompat.getDrawable(act, R.drawable.match).getConstantState()) {
                    match.setImageResource(R.drawable.match_burned);

                    animateBurnMatch(match, BURN_DIRECTION);
                    numOfBurns--;
                }
            }
        }
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                changeMatchesVisibility(id_of_heap, R.drawable.match_burned, View.INVISIBLE);
                setUserTurn();
            }
        }, 2000);

    }

    @Override
    public void doPositive() {

        BuggerService.setSYSTEM_GlobalPoints(1, "We played a game together!");
        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        if (settings.getBoolean(SETTING_SHOW_EXPLAIN_GAME, true)) {
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.custom_popup_msg);
            dialog.setTitle("Game rules");
            dialog.setCancelable(false);
            // set the custom dialog components - text, image and button
            //  TextView text = (TextView) dialog.findViewById(R.id.text);
            //   text.setText("Android custom dialog example!");

            Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
            // if button is clicked, close the custom dialog
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox checkBox = (CheckBox) dialog.findViewById(R.id.checkBox);
                    if (checkBox.isChecked()) {
                        BuggerService.getInstance().writeToSettings(SETTING_SHOW_EXPLAIN_GAME, false);
                    }
                    dialog.dismiss();
                    setJonTurn();
                    firstStep();
                }
            });
            //    CheckBox checkBox = (CheckBox) dialog.findViewById(R.id.dont_show_again_check);
            dialog.show();
        } else {
            setJonTurn();
            firstStep();
        }
    }

    @Override
    protected void showBeneath(){
        mGifImageView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void hideBeneath(ConstraintLayout layoutAppeared){
        mGifImageView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void doNegative() {
        BuggerService.setSYSTEM_GlobalPoints(-1, "You dont wanted to play a game with me");
        toastThis("Demit you!");
        finish();
    }

    @Override
    public void showDialog() {
        DialogFragment newFragment = MyAlertDialogFragment
                .newInstance(R.string.alert_dialog_Game, "Game!", "No.", getClass().getName(),null);
        newFragment.show(getSupportFragmentManager(), "dialog");
    }
}
