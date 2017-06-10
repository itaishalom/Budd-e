package com.shalom.itai.theservantexperience.Activities;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shalom.itai.theservantexperience.R;
import com.shalom.itai.theservantexperience.Services.BuggerService;

import static com.shalom.itai.theservantexperience.Utils.Functions.throwRandom;

public class MatchesGameActivity extends ToolBarActivity implements DialogCaller {
    private MatchesGameActivity act;
    private boolean turnIsBegan = false;
    private int matchesCounterTurn = 0;
    private boolean takeFromHeapOne = false;
    private int heap1 = 12;
    private int heap2 = 12;
    private int randomNum;
    private boolean isFirstCase = false;
    private final int BURN_DIRECTION = -1;
    private final int UNBURN_DIRECTION = 1;
    private TextView heap1_text;
    private TextView heap2_text;
    private Button fire;
    private final String NUM_OF_MATCHES = "Number of matches: ";
    private MediaPlayer mediaPlayer;
    private Button newGame;
    private boolean isJonPlay = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState, R.layout.activity_matches_game);
        // setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar));

        heap1_text = (TextView) findViewById(R.id.heap1_data);
        heap2_text = (TextView) findViewById(R.id.heap2_data);
        newGame = (Button) findViewById(R.id.new_game);
        setBrightness(0.1f);
        newGame.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           turnIsBegan = false;
                                           matchesCounterTurn = 0;
                                           takeFromHeapOne = false;
                                           heap1 = 12;
                                           heap2 = 12;
                                           restoreMatches();
                                           setBrightness(0.5f);
                                           firstStep();
                                       }
                                   }
        );

        fire = (Button) findViewById(R.id.fire);
        arr = new View[]{fire, newGame};
        act = this;
        showDialog();
        //  firstStep();
    }

    public void setBrightness(float brightness) {
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


    private void endOfGame(boolean isJonWon) {
        if (isJonWon) {
            mediaPlayer = MediaPlayer.create(this, R.raw.jon_wins);
            setBrightness(0.9f);
        } else {
            setBrightness(0.1f);
            mediaPlayer = MediaPlayer.create(this, R.raw.jon_lose);
        }
        AudioManager am =
                (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        am.setStreamVolume(
                AudioManager.STREAM_MUSIC,
                am.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                0);
        mediaPlayer.start();

        new CountDownTimer(mediaPlayer.getDuration(), 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                mediaPlayer.stop();

            }
        }.start();

    }

    private void setUserTurn() {
        isJonPlay = false;
        fire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    toastThis("no matches selected!");
                }
            }
        });
    }

    private void setJonTurn() {
        isJonPlay = true;
        fire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toastThis("Jon's turn!");
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

    private void toastThis(String info) {
        Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
    }


    private void secondStep() {
        setJonTurn();
        heap1_text.setText(NUM_OF_MATCHES + heap1);
        heap2_text.setText(NUM_OF_MATCHES + heap2);
        if (isFirstCase) {
            if (heap1 <= 0) {
                toastThis("Jon beats the user");
                endOfGame(true);
                return;
            }
            if (heap2 <= 0) {
                toastThis("Jon beats the user");
                endOfGame(true);
                return;
            }

        } else {
            if (heap1 <= 0) {
                toastThis("Jon beats the user");
                endOfGame(true);
                return;
            }

            if (heap2 <= 0) {
                toastThis("Jon beats the user");
                endOfGame(true);
                return;
            }
        }
        if ((heap1 == 1) && (heap2 == 1)) {
            toastThis("Jon took 1 matches from heap number 2");
            jonBurn(R.id.heap2, 1);
            toastThis("The user beats Jon");
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
        randomNum = throwRandom(4, 1);

        if (heap1 > 1) {
            isFirstCase = true;
            if ((heap1 < 5) && (heap1 > 1) && ((heap1 - randomNum) <= 0)) {
                do {
                    randomNum = throwRandom(4, 1);
                }
                while (heap1 - randomNum < 1);
            }
            heap1 = heap1 - randomNum;
            toastThis("Jon took " + randomNum +
                    " matches from heap number 1");

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
            toastThis("Jon took " + randomNum + " matches from heap number 2");
            toastThis("Number of matches in the first heap is:" + heap1);
            toastThis("Number of matches in the second heap is:" + heap2);
            jonBurn(R.id.heap2, randomNum);
        }
        toastThis("Select number of matches to take:");
    }

    private void restoreMatches() {
        int[] ids = new int[]{R.id.heap1, R.id.heap2};
        for (int j = 0; j < ids.length; j++) {
            ConstraintLayout ll = (ConstraintLayout) findViewById(ids[j]);
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
            changeMatchesVisibility(ids[j], R.drawable.match, View.VISIBLE);
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

        BuggerService.setSYSTEM_GlobalPoints(1);
        setJonTurn();
        firstStep();
    }

    @Override
    public void doNegative() {
        BuggerService.setSYSTEM_GlobalPoints(-1);
        toastThis("Demit you!");
    }

    @Override
    public void showDialog() {
        DialogFragment newFragment = MyAlertDialogFragment
                .newInstance(R.string.alert_dialog_Game, "Game!", "No.", getClass().getName());
        newFragment.show(getSupportFragmentManager(), "dialog");
    }
}
