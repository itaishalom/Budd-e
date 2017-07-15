package com.shalom.itai.theservantexperience.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shalom.itai.theservantexperience.R;
import com.shalom.itai.theservantexperience.gallery.GalleryActivity;
import com.shalom.itai.theservantexperience.moods.Angry;
import com.shalom.itai.theservantexperience.moods.Board;
import com.shalom.itai.theservantexperience.moods.Mood;
import com.shalom.itai.theservantexperience.moods.MoodFactory;
import com.shalom.itai.theservantexperience.relations.RelationsStatus;
import com.shalom.itai.theservantexperience.services.BuggerService;
import com.shalom.itai.theservantexperience.utils.FontTextView;
import com.shalom.itai.theservantexperience.utils.Functions;

import org.apache.logging.log4j.core.config.plugins.validation.Constraint;

import pl.droidsonroids.gif.GifImageView;

import static com.shalom.itai.theservantexperience.utils.Constants.FULL_INT_ALPHA;
import static com.shalom.itai.theservantexperience.utils.Constants.HALF_INT_ALPHA;
import static com.shalom.itai.theservantexperience.utils.Constants.JonIntents.INPUT_TO_SPLASH_CLASS_NAME;
import static com.shalom.itai.theservantexperience.utils.Constants.JonIntents.JUST_WOKE_UP;
import static com.shalom.itai.theservantexperience.utils.Constants.PREFS_NAME;
import static com.shalom.itai.theservantexperience.utils.Constants.SETTINGS_INITIAL_TIRED_POINTS;
import static com.shalom.itai.theservantexperience.utils.Constants.SETTINGS_IS_ASLEEP;
import static com.shalom.itai.theservantexperience.utils.Constants.SETTINGS_IS_OPEN_VIDEO_DONE;
import static com.shalom.itai.theservantexperience.utils.Constants.SETTINGS_MAX_TIRED_POINTS;
import static com.shalom.itai.theservantexperience.utils.Constants.SETTINGS_TIRED_POINTS;
import static com.shalom.itai.theservantexperience.utils.Constants.SETTINGS_USER_LOOSE;
import static com.shalom.itai.theservantexperience.utils.Constants.SETTINGS_USER_WINS;
import static com.shalom.itai.theservantexperience.utils.Constants.STATUS_CHANGE_BROADCAST;
import static com.shalom.itai.theservantexperience.utils.Functions.getBatteryLevel;
import static com.shalom.itai.theservantexperience.utils.Functions.getBatteryTemperature;
import static com.shalom.itai.theservantexperience.utils.Functions.getReceptionLevel;
import static com.shalom.itai.theservantexperience.utils.Functions.startOverlay;
import static com.shalom.itai.theservantexperience.utils.Functions.stopOverlay;

/**
 * Created by Itai on 09/06/2017.
 */

public abstract class ToolBarActivityNew extends AppCompatActivity {
    private ConstraintLayout relationsLayout;
    private ConstraintLayout moodLayout;
    private ConstraintLayout gameWinsLayout;
    View[] arr;
    Window mWindow;
    protected BroadcastReceiver mReceiver;
    protected ViewGroup mainLayout;
    private Menu barMenu;
    protected int mOptionsId;
    GifImageView mGifImageView;
    protected ConstraintLayout hidingLayout;
    protected ConstraintLayout.LayoutParams originalParams;
    protected int mIdOfHidedView = -1;


    // --Commented out by Inspection (18/06/2017 00:18):private ConstraintLayout lLayout;
    protected final void onCreate(Bundle savedInstanceState, int layoutId, int optionsId, boolean addIconToStatusBar, int hideIdView) {
        super.onCreate(savedInstanceState);
        Functions.isMyServiceRunning(BuggerService.class, this);
        mOptionsId = optionsId;
        setContentView(layoutId);
        //   RL.setBackgroundColor(Color.parseColor("#01ff90"));
        refreshLayout();
        mOptionsId = optionsId;
        mIdOfHidedView = hideIdView;

        mainLayout = (ViewGroup) findViewById(R.id.main_layout);
        //    layout.setBackgroundResource(BuggerService.getInstance().getMood().getBackground());
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);//0xf6b478);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (addIconToStatusBar) {
            getSupportActionBar().setIcon(R.drawable.title);
        }

        //  getSupportActionBar().setIcon(R.drawable.title);
        relationsLayout = (ConstraintLayout) findViewById(R.id.relation_layout);
        //   relationsLayout.bringToFront();
        moodLayout = (ConstraintLayout) findViewById(R.id.mood_layout);


        gameWinsLayout = (ConstraintLayout) findViewById(R.id.game_wins_layout);

        //   moodLayout.bringToFront();
        mGifImageView = (GifImageView) findViewById(R.id.GifImageView);

        //   getSupportActionBar().setDisplayShowTitleEnabled(false);
        //       lLayout.setBackgroundColor(Color.parseColor("#04967D"));

        /*
        relationsLayout = (ConstraintLayout) findViewById(R.id.relation_layout);
        relationsLayout.bringToFront();
        moodLayout = (ConstraintLayout) findViewById(R.id.mood_layout);
        moodLayout.bringToFront();
*/
    }

    protected void newGame() {
    }
/*



    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(this.mReceiver);
    }
    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(
                IMAGE_READY);

        mReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                //extract our message from intent
                Bitmap imageBitmap = mCamera.getImageBitmap();
                putResponseButtons(imageBitmap);
            }
        };
        //registering our receiver
        this.registerReceiver(mReceiver, intentFilter);
    }
 */

    protected void turnOfAllActionsBar() {
        for (int i = 0; i < barMenu.size(); i++) {
            barMenu.getItem(i).getIcon().setAlpha(HALF_INT_ALPHA);
        }
        if (moodLayout.getVisibility() != View.GONE)
            moodLayout.setVisibility(View.GONE);
        if (relationsLayout.getVisibility() != View.GONE)
            relationsLayout.setVisibility(View.GONE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(mOptionsId, menu);
        for (int i = 0; i < menu.size(); i++) {
            if (this instanceof GalleryActivity) {
                if (menu.getItem(i).getItemId() == R.id.actions_mems) {
                    menu.getItem(i).getIcon().setAlpha(FULL_INT_ALPHA);
                } else {
                    menu.getItem(i).getIcon().setAlpha(HALF_INT_ALPHA);
                }
            } else {
                menu.getItem(i).getIcon().setAlpha(HALF_INT_ALPHA);
            }
        }
        barMenu = menu;
        // MenuItem te = menu.findItem(R.id.action_favorite);
        //  if (te != null)
        //   te.setIcon(R.drawable.relations);
        //     te.setIcon(BuggerService.getInstance().getRelationsStatus().getIconId());
        return true;
    }

    void invalidateStatusParam() {
        ((TextView) findViewById(R.id.reception_status_ind)).setText((getReceptionLevel(this).name()));
        ((TextView) findViewById(R.id.battery_status_ind)).setText((getBatteryLevel(this).name()));
        ((TextView) findViewById(R.id.temperature_status_ind)).setText((getBatteryTemperature(this).name()));
        ProgressBar energyBar = (ProgressBar) findViewById(R.id.progressEnergy);
        SharedPreferences settings = this.getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        int tired = settings.getInt(SETTINGS_TIRED_POINTS, SETTINGS_INITIAL_TIRED_POINTS);
        energyBar.setMax(SETTINGS_MAX_TIRED_POINTS);
        energyBar.setProgress(tired);
    }

    protected void hideBeneath(ConstraintLayout layoutAppeared) {
        if (!(mainLayout instanceof ConstraintLayout))
            return;
        if (mIdOfHidedView != -1) {
            int val = 0;
            if (hidingLayout != null) {
                val = hidingLayout.getHeight();
            }
            ConstraintSet set = new ConstraintSet();
            View list = findViewById(mIdOfHidedView);
            //   LinearLayout.LayoutParams params =  new LinearLayout.LayoutParams(list.getWidth(), list.getHeight() - layoutAppeared.getHeight());
            //   layout.setLayoutParams(params);
            if (val == 0)
                originalParams = (ConstraintLayout.LayoutParams) list.getLayoutParams();

            ConstraintLayout.LayoutParams newParams = new ConstraintLayout.LayoutParams(originalParams);
              /*
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                list.getHeight() + val - layoutAppeared.getHeight());
*/
            newParams.height = list.getHeight() + val - layoutAppeared.getHeight();
            //   newParams.setMargins(16, 16, 16, 16);

            mainLayout.removeView(list);
            mainLayout.addView(list, -1, newParams);
            set.clone((ConstraintLayout) mainLayout);
            set.connect(list.getId(), ConstraintSet.LEFT, mainLayout.getId(), ConstraintSet.LEFT, 8);
            set.connect(list.getId(), ConstraintSet.RIGHT, mainLayout.getId(), ConstraintSet.RIGHT, 8);
            set.connect(list.getId(), ConstraintSet.TOP, layoutAppeared.getId(), ConstraintSet.BOTTOM, 8);
            set.applyTo((ConstraintLayout) mainLayout);
            hidingLayout = layoutAppeared;
        }
    }

    protected void showBeneath() {
        if (!(mainLayout instanceof ConstraintLayout))
            return;
        if (mIdOfHidedView != -1) {
            ConstraintSet set = new ConstraintSet();
            View list = findViewById(mIdOfHidedView);
            mainLayout.removeView(list);
            mainLayout.addView(list, -1, originalParams);
            set.clone((ConstraintLayout) mainLayout);
            set.connect(list.getId(), ConstraintSet.TOP, R.id.my_toolbar, ConstraintSet.BOTTOM, 8);
            set.connect(list.getId(), ConstraintSet.LEFT, mainLayout.getId(), ConstraintSet.LEFT, 8);
            set.connect(list.getId(), ConstraintSet.RIGHT, mainLayout.getId(), ConstraintSet.RIGHT, 8);
            set.applyTo((ConstraintLayout) mainLayout);
            hidingLayout = null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean isTurnedOn;// = false;

        if (item.getIcon().getAlpha() == FULL_INT_ALPHA) {
            item.getIcon().setAlpha(HALF_INT_ALPHA);
            isTurnedOn = false;
        } else {
            item.getIcon().setAlpha(FULL_INT_ALPHA);
            isTurnedOn = true;
        }
        for (int i = 0; i < barMenu.size(); i++) {
            if (item != barMenu.getItem(i)) {
                if (this instanceof GalleryActivity && barMenu.getItem(i).getItemId() == R.id.actions_mems) {
                    continue;
                }
                barMenu.getItem(i).getIcon().setAlpha(HALF_INT_ALPHA);
            }
        }
        switch (item.getItemId()) {
            case R.id.action_mood:
                if (isTurnedOn) {
                    invalidateStatusParam();
                    moodLayout.setVisibility(View.VISIBLE);
                    hideBeneath(moodLayout);
                    if (relationsLayout.getVisibility() != View.GONE)
                        relationsLayout.setVisibility(View.GONE);
                } else {
                    moodLayout.setVisibility(View.GONE);
                    showBeneath();
                }
                return true;
            case R.id.action_favorite:
//                ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar3);
                if (isTurnedOn) {
                    invalidateRelationsData();
                    relationsLayout.setVisibility(View.VISIBLE);
                    if (moodLayout.getVisibility() != View.GONE)
                        moodLayout.setVisibility(View.GONE);
                    hideBeneath(relationsLayout);
                } else {
                    relationsLayout.setVisibility(View.GONE);
                    showBeneath();
                }
                return true;
            case R.id.actions_mems:
                if (this instanceof GalleryActivity) {
                    finish();
                    return true;
                }
                moodLayout.setVisibility(View.GONE);
                relationsLayout.setVisibility(View.GONE);
                startActivity(new Intent(this, SplashActivity.class).putExtra(INPUT_TO_SPLASH_CLASS_NAME, GalleryActivity.class.getSimpleName()));
                overridePendingTransition(R.anim.slide_top_in, R.anim.slide_bottom_out);
                return true;
            case R.id.action_new_game:
                newGame();
                return true;
            case R.id.action_wins:
                SharedPreferences preferences = getSharedPreferences(PREFS_NAME, 0);
                int wins = preferences.getInt(SETTINGS_USER_WINS, 0);
                int losses = preferences.getInt(SETTINGS_USER_LOOSE, 0);
                invalidateGameStatus(wins, losses);
                if (gameWinsLayout.getVisibility() == View.VISIBLE) {
                    gameWinsLayout.setVisibility(View.GONE);
                    showBeneath();
                } else {
                    gameWinsLayout.setVisibility(View.VISIBLE);
                    hideBeneath(gameWinsLayout);
                }
                return true;
        }

        return super.onOptionsItemSelected(item);

        /*
        switch (item.getItemId()) {
            case R.id.action_settings:
                //       startActivity(new Intent(MainActivity.this, GameActivity.class));
                return true;
            case R.id.action_mood:
                if (moodLayout.getVisibility() == View.VISIBLE)
                    moodLayout.setVisibility(View.INVISIBLE);
                else {
                    moodLayout.setVisibility(View.VISIBLE);
                    relationsLayout.setVisibility(View.INVISIBLE);
                    invalidateStatusParam();
                }
                verification();
                return true;
            case R.id.action_favorite:
//                ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar3);
                if (relationsLayout.getVisibility() == View.VISIBLE)
                    relationsLayout.setVisibility(View.INVISIBLE);
                else {
                    invalidateRelationsData();
                    relationsLayout.setVisibility(View.VISIBLE);
                    moodLayout.setVisibility(View.INVISIBLE);
                }
                verification();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
        */
    }

    void invalidateRelationsData() {
        RelationsStatus status = BuggerService.getInstance().getRelationsStatus();
        ProgressBar pb = (ProgressBar) findViewById(R.id.progressBarRelations);
        pb.setMax(status.getMaxValProgress() - status.getMinValProgress());
        pb.setProgress(BuggerService.getSYSTEM_GlobalPoints() - status.getMinValProgress());
        TextView friendshipLevel = (TextView) findViewById(R.id.friendship_level_ind);
        friendshipLevel.setText(status.getRelationStatus());
    }

    protected void invalidateGameStatus(int wins, int loose) {
    }


    protected void refreshLayout() {
        View lay = findViewById(R.id.main_layout);
        if (mainLayout == null) {

            mainLayout = (ViewGroup) findViewById(R.id.main_layout);

            mWindow = getWindow();
            mGifImageView = (GifImageView) findViewById(R.id.GifImageView);
        }
        mWindow.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //   int color = BuggerService.getInstance().getMood().getTopBackgroundColor();
            //   if(color !=0)
            if (mWindow == null)
                return;
            if (BuggerService.getInstance() == null)
                mWindow.setStatusBarColor(Color.parseColor(Board.getInstance().getTopBackgroundColor()));
            else
                mWindow.setStatusBarColor(Color.parseColor(BuggerService.getInstance().getMood().getTopBackgroundColor()));

            // else
            //     mWindow.setStatusBarColor(Color.parseColor("#000000"));
        }
        if (mGifImageView != null) {//Optional
            if (BuggerService.getInstance() == null)
                mGifImageView.setImageResource(Board.getInstance().getGif());
            else
                mGifImageView.setImageResource(BuggerService.getInstance().getMood().getGif());

        }
        if (BuggerService.getInstance() == null)
            lay.setBackgroundResource(Board.getInstance().getGif());
        else
            lay.setBackgroundResource(BuggerService.getInstance().getMood().getBackground());
    }


    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        Log.d("onResume", "onResume: " + intent.getBooleanExtra(JUST_WOKE_UP, false));
        Functions.isMyServiceRunning(BuggerService.class, this);
        refreshLayout();
        stopOverlay(this);
        IntentFilter intentFilter = new IntentFilter(
                STATUS_CHANGE_BROADCAST);

        mReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                ToolBarActivityNew.this.invalidateOptionsMenu();
            }
        };
        //registering our receiver
        this.registerReceiver(mReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        startOverlay(this);
        this.unregisterReceiver(this.mReceiver);
    }


}
