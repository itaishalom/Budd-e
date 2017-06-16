package com.shalom.itai.theservantexperience.Introduction;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Itai on 16/06/2017.
 */

public class MyViewPager extends ViewPager {
    public static String TAG = "MyViewPager";
    public MyViewPager(Context context) {
        super(context);

      //  this.addOnPageChangeListener(lis);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

/*    @Override
    public boolean onTouchEvent(MotionEvent ev)  {
        boolean val = super.onTouchEvent(ev);
        Log.d("S", "onTouchEvent: here" + ev.getAxisValue(0));
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
              //  this.requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
           //     this.requestDisallowInterceptTouchEvent(false);
                break;
        }
        return val;
    }*/

    public static class SimpleOnPageChangeListener implements OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //    Log.d(TAG, "onPageScrolled: ");
            // This space for rent
        }

        @Override
        public void onPageSelected(int position) {
            // This space for rent
            TutorialActivity.getInstance().setDots(position);
     //       Log.d(TAG, "onPageSelected: ");
        }

        @Override
        public void onPageScrollStateChanged(int state) {
      //      Log.d(TAG, "onPageScrollStateChanged: ");
            // This space for rent
        }
    }
}
