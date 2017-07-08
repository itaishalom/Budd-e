package com.shalom.itai.theservantexperience.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shalom.itai.theservantexperience.R;
import com.shalom.itai.theservantexperience.relations.RelationsStatus;
import com.shalom.itai.theservantexperience.services.BuggerService;

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
    View[] arr;
    protected BroadcastReceiver mReceiver;


    // --Commented out by Inspection (18/06/2017 00:18):private ConstraintLayout lLayout;
    protected final void onCreate(Bundle savedInstanceState, int layoutId) {
        super.onCreate(savedInstanceState);
        setContentView(layoutId);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);//0xf6b478);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setIcon(R.drawable.title);
        Window window = getWindow();
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(0xFFF6B478);
        }

        //   getSupportActionBar().setDisplayShowTitleEnabled(false);
        //       lLayout.setBackgroundColor(Color.parseColor("#04967D"));

        /*
        relationsLayout = (ConstraintLayout) findViewById(R.id.relation_layout);
        relationsLayout.bringToFront();
        moodLayout = (ConstraintLayout) findViewById(R.id.mood_layout);
        moodLayout.bringToFront();
*/
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tool_bar_options, menu);
        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).getIcon().setAlpha(128);
        }
        // MenuItem te = menu.findItem(R.id.action_favorite);
        //  if (te != null)
        //   te.setIcon(R.drawable.relations);
        //     te.setIcon(BuggerService.getInstance().getRelationsStatus().getIconId());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getIcon().getAlpha() == 255)
            item.getIcon().setAlpha(128);
        else
            item.getIcon().setAlpha(255);
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

    /*
        private void verification(){
            if(arr !=null){
                if(relationsLayout.getVisibility() == View.VISIBLE || moodLayout.getVisibility() == View.VISIBLE ) {
                    for (View anArr : arr) {
                        anArr.setVisibility(View.INVISIBLE);
                    }
                }
                else{
                    for (View anArr : arr) {
                        anArr.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    */
/*
    void invalidateStatusParam() {
        ((TextView) findViewById(R.id.reception_status_ind)).setText((getReceptionLevel(this).name()) );
        ((TextView) findViewById(R.id.battery_status_ind)).setText((getBatteryLevel(this).name()) );
        ((TextView) findViewById(R.id.temperature_status_ind)).setText((getBatteryTemperature(this).name()) );
    }


    void invalidateRelationsData() {
        RelationsStatus status = BuggerService.getInstance().getRelationsStatus();
        ProgressBar pb = (ProgressBar) findViewById(R.id.progressBarRelations);
        pb.setMax(status.getMaxValProgress()- status.getMinValProgress());
        pb.setProgress(BuggerService.getSYSTEM_GlobalPoints() - status.getMinValProgress());
        TextView friendshipLevel = (TextView) findViewById(R.id.friendship_level_ind);
        friendshipLevel.setText(status.getRelationStatus());
    }
*/
    @Override
    protected void onResume() {
        super.onResume();
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
