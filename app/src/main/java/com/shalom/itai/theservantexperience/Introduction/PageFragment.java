package com.shalom.itai.theservantexperience.Introduction;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.shalom.itai.theservantexperience.Activities.MainActivity;
import com.shalom.itai.theservantexperience.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PageFragment extends android.support.v4.app.Fragment {
    TextView text;
    AlphaAnimation blinkanimation;

    public PageFragment() {
        // Required empty public constructor
        blinkanimation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        blinkanimation.setDuration(1500); // duration - half a second
        blinkanimation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        blinkanimation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
        blinkanimation.setRepeatMode(Animation.REVERSE);
        Log.d("frag", "PageFragment: here");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        Bundle bundle = getArguments();
        final Activity activity = this.getActivity();
        View view = null;
        switch (bundle.getInt("count")) {
            case 0:
                view = loadJon(container, inflater, activity, R.layout.fragment_page_1);
                break;
            case 1:
                view = handlePage2(container, inflater, activity, R.layout.fragment_page_2,true);
                break;
            case 2:
                view = handlePage3(container, inflater, activity, R.layout.fragment_page_3,true);
                break;
            case 3:
                view = handlePage4(container, inflater, activity, R.layout.fragment_page_4,true);
                break;
            case 4:
                view = handlePage5(container, inflater, activity, R.layout.fragment_page_5,true);
                break;
            case 5:
                view = handlePage6(container, inflater, activity, R.layout.fragment_page_6);
                break;
        }

        return view;
    }

    private void loadBlinkingImage(View view, String uri,int id, Activity activity, boolean toBlink){
        ImageView image = (ImageView) view.findViewById(id);
        int imageResource = getResources().getIdentifier(uri, null, activity.getPackageName());
        image.setImageResource(imageResource);
        if(toBlink)
            image.startAnimation(blinkanimation);
    }

    private View loadJon(ViewGroup container, LayoutInflater inflater, Activity activity, int layout) {
        View view = inflater.inflate(layout, container, false);
        ImageView image = (ImageView) view.findViewById(R.id.jon_in_tutorial);
        String uri = "@drawable/jon_png";  // where myresource (without the extension) is the file
        int imageResource = getResources().getIdentifier(uri, null, activity.getPackageName());
        image.setImageResource(imageResource);
        return view;
    }

    //mem_tutorial
    private View handlePage2(ViewGroup container, LayoutInflater inflater, Activity activity, int layout,boolean toBlink) {
        View view = loadJon(container, inflater, activity, layout);
        loadBlinkingImage(view,"@drawable/chat",R.id.chat_icon_tutorial,activity,toBlink);
        return view;
    }

    private View handlePage3(ViewGroup container, LayoutInflater inflater, Activity activity, int layout,boolean toBlink) {
        View view = handlePage2(container, inflater, activity, layout, false);
        loadBlinkingImage(view,"@drawable/mem",R.id.mem_tutorial,activity,toBlink);
        return view;
    }

    private View handlePage4(ViewGroup container, LayoutInflater inflater, Activity activity, int layout,boolean toBlink) {
        View view = handlePage3(container, inflater, activity, layout,false);
        loadBlinkingImage(view,"@drawable/rel_stranger",R.id.rel_tutorial,activity,toBlink);
        return view;
    }

    private View handlePage5(ViewGroup container, LayoutInflater inflater, Activity activity, int layout,boolean toBlink) {
        View view = handlePage4(container, inflater, activity, layout,false);
        loadBlinkingImage(view,"@drawable/mood",R.id.mood_tutorial,activity,toBlink);
        return view;
    }

    private View handlePage6(ViewGroup container, LayoutInflater inflater, final Activity activity, int layout) {
        View view = loadJon(container, inflater, activity, layout);
        Button button = (Button) view.findViewById(R.id.ok_got_it);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent(activity, MainActivity.class);
                returnIntent.putExtra("tutorial_done", true);
                startActivity(returnIntent);
                activity.finish();
                return;
            }
        });
        return view;
    }

}
