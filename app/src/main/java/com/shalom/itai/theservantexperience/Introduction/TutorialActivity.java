package com.shalom.itai.theservantexperience.Introduction;

import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.shalom.itai.theservantexperience.R;

import static android.view.View.GONE;

public class TutorialActivity extends FragmentActivity {
    private static TutorialActivity mInstance ;
    ViewPager mViewPager;
boolean ischanged = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        SwipeAdapter swipeAdapter = new SwipeAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(swipeAdapter);
        mInstance = this;
    }
public static TutorialActivity getInstance(){
    return mInstance;
}

    public void removeConstraint(){

        ischanged = true;
        mViewPager.getLayoutParams().height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
    }

    public void recreate(){
      //  onCreate(mSavedInstanceState);

        /*TextView textView = (TextView) findViewById(R.id.toolbar_like_text_view);
        textView.setVisibility(View.INVISIBLE);
       mViewPager.getLayoutParams().height = valueOfWrap;
    */
    }

}
