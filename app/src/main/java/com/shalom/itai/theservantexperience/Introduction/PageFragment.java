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

import java.util.HashMap;

import pl.droidsonroids.gif.GifImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class PageFragment extends android.support.v4.app.Fragment {
    private AlphaAnimation blinkanimation;
    private static PageFragment mInstance;

    public PageFragment() {
        // Required empty public constructor

        Log.d("frag", "PageFragment: here");
        mInstance = this;
    }

    public static PageFragment getInstance(){
        return mInstance;
    }
/*
    public void startBlink(int page){
        switch (page){
            case 1:
                layoutToImage.get(R.id.chat_icon_tutorial).startAnimation(blinkanimation);
                break;
            case 2:
                layoutToImage.get(R.id.mem_tutorial).startAnimation(blinkanimation);
                break;
            case 3:
                layoutToImage.get(R.id.rel_tutorial).startAnimation(blinkanimation);
                break;
            case 4:
                layoutToImage.get(R.id.mood_tutorial).startAnimation(blinkanimation);
                break;
        }
     //   toBlinkImage.startAnimation(blinkanimation);
    }
*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
   //     setBlinkable(container, inflater,  this.getActivity());
        // Inflate the layout for this fragment
        Bundle bundle = getArguments();
        final Activity activity = this.getActivity();
        View view = null;
        switch (bundle.getInt("count")) {
            case 0:
                view = loadJon(container, inflater, activity, R.layout.fragment_page_1);
             //   loadBlinkingImage(view,"@drawable/dots_1",R.id.dots_location,activity,false);
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
                view = handlePage5(container, inflater, activity, R.layout.fragment_page_5,true
                );
                break;
            case 5:
                view = handlePage6(container, inflater, activity, R.layout.fragment_page_6);
                break;
        }

        return view;
    }

    /*
    private void setBlinkable(ViewGroup container, LayoutInflater inflater, Activity activity){
        View view = inflater.inflate( R.layout.fragment_page_2, container, false);
        loadBlinkingImage(view,"@drawable/chat",R.id.chat_icon_tutorial,activity,2);

        view = inflater.inflate( R.layout.fragment_page_3, container, false);
        loadBlinkingImage(view,"@drawable/mem",R.id.mem_tutorial,activity,3);

        view = inflater.inflate( R.layout.fragment_page_4, container, false);
        loadBlinkingImage(view,"@drawable/rel_stranger",R.id.rel_tutorial,activity,4);


        view = inflater.inflate( R.layout.fragment_page_5, container, false);
        loadBlinkingImage(view,"@drawable/mood",R.id.rel_tutorial,activity,5);
    }
    */

    private ImageView loadBlinkingImage(View view, String uri,int id, Activity activity){
        ImageView image = (ImageView) view.findViewById(id);
        int imageResource = getResources().getIdentifier(uri, null, activity.getPackageName());
        image.setImageResource(imageResource);
        return image;
       //     TutorialActivity.getInstance().setIdToImage(index,image);
        //   image.startAnimation(blinkanimation);
    }

    private View loadJon(ViewGroup container, LayoutInflater inflater, Activity activity, int layout) {
        View view = inflater.inflate(layout, container, false);
        GifImageView image = (GifImageView) view.findViewById(R.id.jon_in_tutorial);
    //    String uri = "@drawable/jon_png";  // where myresource (without the extension) is the file
      //  int imageResource = getResources().getIdentifier(uri, null, activity.getPackageName());
        image.setImageResource(R.drawable.jon_blinks);

      //  loadBlinkingImage(view,"@drawable/dots_1",R.id.dots_location,activity,false);

        return view;
    }

    //mem_tutorial
    private View handlePage2(ViewGroup container, LayoutInflater inflater, Activity activity, int layout,boolean fromMain  ) {
        View view = loadJon(container, inflater, activity, layout);
        ImageView img = loadBlinkingImage(view,"@drawable/chat",R.id.chat_icon_tutorial,activity);
        if(fromMain){
            TutorialActivity.getInstance().setIdToImage(2,img);
        }
   //     loadBlinkingImage(view,"@drawable/dots_2",R.id.dots_location,activity,false);
        return view;
    }

    private View handlePage3(ViewGroup container, LayoutInflater inflater, Activity activity, int layout ,boolean fromMain ) {
        View view = handlePage2(container, inflater, activity, layout,false);
        ImageView img  = loadBlinkingImage(view,"@drawable/mem",R.id.mem_tutorial,activity);
        if(fromMain){
            TutorialActivity.getInstance().setIdToImage(3,img);
        }
   //     loadBlinkingImage(view,"@drawable/dots_3",R.id.dots_location,activity,false);
        return view;
    }

    private View handlePage4(ViewGroup container, LayoutInflater inflater, Activity activity, int layout,boolean fromMain ) {
        View view = handlePage3(container, inflater, activity, layout,false);
        ImageView img = loadBlinkingImage(view,"@drawable/rel_stranger",R.id.rel_tutorial,activity);
        if(fromMain){
            TutorialActivity.getInstance().setIdToImage(4,img);
        }
    //    loadBlinkingImage(view,"@drawable/dots_4",R.id.dots_location,activity,false);
        return view;
    }

    private View handlePage5(ViewGroup container, LayoutInflater inflater, Activity activity, int layout,boolean fromMain ) {
        View view = handlePage4(container, inflater, activity, layout, false);
        ImageView img =loadBlinkingImage(view,"@drawable/mood",R.id.mood_tutorial,activity);
    //    loadBlinkingImage(view,"@drawable/dots_5",R.id.dots_location,activity,false);
        if(fromMain){
            TutorialActivity.getInstance().setIdToImage(5,img);
        }
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
