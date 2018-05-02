package com.shalom.itai.theservantexperience.introduction;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;

import com.shalom.itai.theservantexperience.R;
import com.shalom.itai.theservantexperience.moods.Optimistic;

public class TutorialActivity extends FragmentActivity {

    public static SparseArray<ImageView> idToImage;

    @SuppressWarnings("unused")
    public static final String TAG = "TutorialActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor(Optimistic.getInstance().getTopBackgroundColor()));
        }
        MyViewPager mViewPager = (MyViewPager) findViewById(R.id.view_pager);
        SwipeAdapter swipeAdapter = new SwipeAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(swipeAdapter);

        ImageView imageDots = (ImageView) findViewById(R.id.dots);
        int imageResource = getResources().getIdentifier("@drawable/dots_1", null, this.getPackageName());
        imageDots.setImageResource(imageResource);

        idToImage = new SparseArray<>();

        MyViewPager.SimpleOnPageChangeListener lis = new MyViewPager.SimpleOnPageChangeListener(this, imageDots);
        mViewPager.addOnPageChangeListener(lis);
    }
}
