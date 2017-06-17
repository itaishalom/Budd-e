package com.shalom.itai.theservantexperience.Introduction;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import static com.shalom.itai.theservantexperience.Introduction.TutorialActivity.idToImage;

/**
 * Created by Itai on 16/06/2017.
 */

public class MyViewPager extends ViewPager {
    public static String TAG = "MyViewPager";
    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



    static class SimpleOnPageChangeListener implements OnPageChangeListener {
        private ImageView mImageOfDots;
        private Context mContext;
        private AlphaAnimation mBlinkanimation;

        SimpleOnPageChangeListener(Context context, ImageView imageOfDots){
            super();
            mContext = context;
            mImageOfDots = imageOfDots;

            mBlinkanimation = new AlphaAnimation(0.0f, 1.0f); // Change alpha from fully visible to invisible
            mBlinkanimation.setDuration(1000); // duration - half a second
            mBlinkanimation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
            mBlinkanimation.setRepeatCount(2); // Repeat animation infinitely
            mBlinkanimation.setFillAfter(false);//to keep it at 0 when animation ends
            mBlinkanimation.setRepeatMode(Animation.REVERSE);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            int loc = position + 1;
            int imageResource = mContext.getResources().getIdentifier("@drawable/dots_" + loc, null, mContext.getPackageName());
            mImageOfDots.setImageResource(imageResource);
            ImageView img = idToImage.get(loc);
            if (img != null) {
                img.startAnimation(mBlinkanimation);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }
}
