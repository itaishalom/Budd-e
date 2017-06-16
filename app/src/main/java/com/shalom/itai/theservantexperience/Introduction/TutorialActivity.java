package com.shalom.itai.theservantexperience.Introduction;

import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.shalom.itai.theservantexperience.R;

import java.util.HashMap;

import static android.view.View.GONE;

public class TutorialActivity extends FragmentActivity {
    private static TutorialActivity mInstance;
    MyViewPager mViewPager;
    ImageView imageDots;
    AlphaAnimation mBlinkanimation;
    public HashMap<Integer,ImageView> idToImage;
    public static final String TAG = "TutorialActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        mViewPager = (MyViewPager) findViewById(R.id.view_pager);
        SwipeAdapter swipeAdapter = new SwipeAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(swipeAdapter);
        imageDots = (ImageView) findViewById(R.id.dots);
        int imageResource = getResources().getIdentifier("@drawable/dots_1", null, this.getPackageName());
        imageDots.setImageResource(imageResource);
        idToImage = new HashMap<>();

        MyViewPager.SimpleOnPageChangeListener lis = new MyViewPager.SimpleOnPageChangeListener();
        mViewPager.addOnPageChangeListener(lis);

        mBlinkanimation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        mBlinkanimation.setDuration(1000); // duration - half a second
        mBlinkanimation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        mBlinkanimation.setRepeatCount(2); // Repeat animation infinitely
        mBlinkanimation.setRepeatMode(Animation.REVERSE);

        mInstance = this;
    }

    public static TutorialActivity getInstance() {
        return mInstance;
    }

    public void setIdToImage(int id, ImageView image){
     //   if(!idToImage.containsKey(id))
            idToImage.put(id,image);
    }

    public void setDots(int i) {
        int loc = i + 1;
        int imageResource = getResources().getIdentifier("@drawable/dots_" + loc, null, this.getPackageName());
        imageDots.setImageResource(imageResource);
        if(idToImage.containsKey(loc)) {

            ImageView imageView = idToImage.get(loc);
            imageView.clearAnimation();
            mBlinkanimation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
            mBlinkanimation.setDuration(1000); // duration - half a second
            mBlinkanimation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
            mBlinkanimation.setRepeatCount(2); // Repeat animation infinitely
            mBlinkanimation.setRepeatMode(Animation.REVERSE);
            imageView.startAnimation(mBlinkanimation);

        }
    }
}
