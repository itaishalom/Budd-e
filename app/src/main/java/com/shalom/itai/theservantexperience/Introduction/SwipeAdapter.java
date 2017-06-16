package com.shalom.itai.theservantexperience.Introduction;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;

import com.shalom.itai.theservantexperience.R;

/**
 * Created by Itai on 14/06/2017.
 */

public class SwipeAdapter extends FragmentStatePagerAdapter {
    FragmentManager mfm;
    public SwipeAdapter(FragmentManager fm) {
        super(fm);
        mfm = fm;
    }

    @Override
    public Fragment getItem(int position) {
        // TODO Auto-generated method stub

        Fragment fragment = new PageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("count",position );
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return 6;
    }
}
