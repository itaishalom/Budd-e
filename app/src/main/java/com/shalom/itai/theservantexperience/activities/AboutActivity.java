package com.shalom.itai.theservantexperience.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.shalom.itai.theservantexperience.R;
import com.shalom.itai.theservantexperience.moods.Board;
import com.shalom.itai.theservantexperience.services.BuggerService;

import pl.droidsonroids.gif.GifImageView;

import static com.shalom.itai.theservantexperience.utils.Functions.startOverlay;
import static com.shalom.itai.theservantexperience.utils.Functions.stopOverlay;

public class AboutActivity extends AppCompatActivity {
    CoordinatorLayout mainLayout;
    Window mWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        refreshLayout();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
      //  fab.setBackgroundTintList(ColorStateList.valueOf(0x000000));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                intent.putExtra(android.content.Intent.EXTRA_EMAIL,
                        new String[] { "itai.shalom2@gmail.com" ,"alonamillgram@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "About Budd-E");

                startActivity(Intent.createChooser(intent, "Send Email"));
            }
        });
        //Set a listener to know the current visible state of CollapseLayout
        AppBarLayout appBarLayout = (AppBarLayout)findViewById(R.id.app_bar);

        CollapsingToolbarLayout CollapsAppBarLayout = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);

        final Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Oswald-Regular.ttf");
        CollapsAppBarLayout.setCollapsedTitleTypeface(tf);
        CollapsAppBarLayout.setExpandedTitleTypeface(tf);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(final AppBarLayout appBarLayout, int verticalOffset) {
                //Initialize the size of the scroll
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                //Check if the view is collapsed
                if (scrollRange + verticalOffset == 0) {
                    toolbar.setBackgroundColor(Color.parseColor(BuggerService.getInstance().getMood().getTopBackgroundColor()));
                }else{
                    toolbar.setBackgroundColor(Color.parseColor(BuggerService.getInstance().getMood().getTopBackgroundColor()));
                }
            }
        });
    }

    protected void refreshLayout() {
        View lay = findViewById(R.id.main_layout);
        if (mainLayout == null) {

            mainLayout = (CoordinatorLayout) findViewById(R.id.main_layout);

            mWindow = getWindow();

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
        if (BuggerService.getInstance() == null)
            lay.setBackgroundResource(Board.getInstance().getGif());
        else
            lay.setBackgroundResource(BuggerService.getInstance().getMood().getBackground());
    }

    @Override
    protected void onResume(){
        super.onResume();
        stopOverlay(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        startOverlay(this);
    }
}