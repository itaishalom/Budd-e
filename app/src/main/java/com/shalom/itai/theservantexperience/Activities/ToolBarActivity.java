package com.shalom.itai.theservantexperience.Activities;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shalom.itai.theservantexperience.R;
import com.shalom.itai.theservantexperience.Relations.RelationsStatus;
import com.shalom.itai.theservantexperience.Services.BuggerService;

import static com.shalom.itai.theservantexperience.Utils.Functions.getBatteryLevel;
import static com.shalom.itai.theservantexperience.Utils.Functions.getReceptionLevel;

/**
 * Created by Itai on 09/06/2017.
 */

public abstract class ToolBarActivity extends AppCompatActivity {
    protected ConstraintLayout relationsLayout;
    protected ConstraintLayout moodLayout;
    protected View[] arr;
    protected final void onCreate(Bundle savedInstanceState, int layoutId) {
        super.onCreate(savedInstanceState);
        setContentView(layoutId);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        //   getSupportActionBar().setDisplayShowTitleEnabled(false);
        relationsLayout = (ConstraintLayout) findViewById(R.id.relation_layout);
        relationsLayout.bringToFront();
        moodLayout = (ConstraintLayout) findViewById(R.id.mood_layout);
        moodLayout.bringToFront();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options, menu);
        MenuItem te = menu.findItem(R.id.action_favorite);
        if (te != null)
            te.setIcon(BuggerService.getInstance().getRelationsStatus().getIconId());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                //       startActivity(new Intent(MainActivity.getInstance(), GameActivity.class));
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
    }

    protected void verification(){
        if(arr !=null){
            if(relationsLayout.getVisibility() == View.VISIBLE || moodLayout.getVisibility() == View.VISIBLE ) {
                for (int i = 0; i < arr.length; i++) {
                    arr[i].setVisibility(View.INVISIBLE);
                }
            }
            else{
                for(int i = 0;i<arr.length;i++) {
                    arr[i].setVisibility(View.VISIBLE);
                }
            }
        }
    }

    protected void invalidateStatusParam() {
        ((TextView) findViewById(R.id.reception_status_ind)).setText(String.valueOf(getReceptionLevel(this)) + " / 5");
        ((TextView) findViewById(R.id.battery_status_ind)).setText(String.valueOf(getBatteryLevel(this)) + " / 5");
    }


    protected void invalidateRelationsData() {
        RelationsStatus status = BuggerService.getInstance().getRelationsStatus();
        ProgressBar pb = (ProgressBar) findViewById(R.id.progressBarRelations);
        pb.setMax(status.getMaxValProgress());
        pb.setProgress(BuggerService.getInstance().getSYSTEM_GlobalPoints());
        TextView friendshipLevel = (TextView) findViewById(R.id.friendship_level_ind);
        friendshipLevel.setText(status.getRelationStatus());
    }
}
