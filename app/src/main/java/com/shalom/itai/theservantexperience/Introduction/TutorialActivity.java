package com.shalom.itai.theservantexperience.Introduction;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;

import com.shalom.itai.theservantexperience.R;

public class TutorialActivity extends FragmentActivity {

    public static SparseArray<ImageView> idToImage;

    public static final String TAG = "TutorialActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(0xDF04967D);
        MyViewPager mViewPager = (MyViewPager) findViewById(R.id.view_pager);
        SwipeAdapter swipeAdapter = new SwipeAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(swipeAdapter);

        ImageView imageDots = (ImageView) findViewById(R.id.dots);
        int imageResource = getResources().getIdentifier("@drawable/dots_1", null, this.getPackageName());
        imageDots.setImageResource(imageResource);
        mViewPager.setImageOfDots(imageDots);

        idToImage = new SparseArray<>();

        MyViewPager.SimpleOnPageChangeListener lis = new MyViewPager.SimpleOnPageChangeListener(this, imageDots);
        mViewPager.addOnPageChangeListener(lis);
    }
}
