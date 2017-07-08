package com.shalom.itai.theservantexperience.introduction;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.shalom.itai.theservantexperience.activities.MainActivity;
import com.shalom.itai.theservantexperience.R;
import com.shalom.itai.theservantexperience.activities.PermissionActivity;
import com.shalom.itai.theservantexperience.utils.Functions;

import pl.droidsonroids.gif.GifImageView;

import static com.shalom.itai.theservantexperience.utils.Constants.SETTINGS_IS_TUTORIAL_DONE;

/**
 * A simple {@link Fragment} subclass.
 */
public class PageFragment extends android.support.v4.app.Fragment {

    public PageFragment() {
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
                view = loadJon(container, inflater, R.layout.fragment_page_1);
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

    private ImageView loadBlinkingImage(View view, String uri,int id, Activity activity){
        ImageView image = (ImageView) view.findViewById(id);
        int imageResource = getResources().getIdentifier(uri, null, activity.getPackageName());
        image.setImageResource(imageResource);
        return image;
    }

    private View loadJon(ViewGroup container, LayoutInflater inflater, int layout) {
        View view = inflater.inflate(layout, container, false);
        GifImageView image = (GifImageView) view.findViewById(R.id.jon_in_tutorial);
        image.setImageResource(R.drawable.jon_blinks);
        return view;
    }

    private View handlePage2(ViewGroup container, LayoutInflater inflater, Activity activity, int layout,boolean fromMain  ) {
        View view = loadJon(container, inflater, layout);
        ImageView img = loadBlinkingImage(view,"@drawable/chat",R.id.chat_icon_tutorial,activity);
        if(fromMain){
            TutorialActivity.idToImage.put(2,img);
        }
        return view;
    }

    private View handlePage3(ViewGroup container, LayoutInflater inflater, Activity activity, int layout ,boolean fromMain ) {
        View view = handlePage2(container, inflater, activity, layout,false);
        ImageView img  = loadBlinkingImage(view,"@drawable/mem",R.id.mem_tutorial,activity);
        if(fromMain){
            TutorialActivity.idToImage.put(3,img);
        }
        return view;
    }

    private View handlePage4(ViewGroup container, LayoutInflater inflater, Activity activity, int layout,boolean fromMain ) {
        View view = handlePage3(container, inflater, activity, layout,false);
        ImageView img = loadBlinkingImage(view,"@drawable/rel_stranger",R.id.rel_tutorial,activity);
        if(fromMain){
            TutorialActivity.idToImage.put(4,img);
        }
        return view;
    }

    private View handlePage5(ViewGroup container, LayoutInflater inflater, Activity activity, int layout,boolean fromMain ) {
        View view = handlePage4(container, inflater, activity, layout, false);
        ImageView img =loadBlinkingImage(view,"@drawable/mood",R.id.mood_tutorial,activity);
        if(fromMain){
            TutorialActivity.idToImage.put(5,img);
        }
        return view;
    }

    private View handlePage6(ViewGroup container, LayoutInflater inflater, final Activity activity, int layout) {
        View view = loadJon(container, inflater, layout);
        Button button = (Button) view.findViewById(R.id.ok_got_it);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Functions.writeToSettings(SETTINGS_IS_TUTORIAL_DONE,true,getContext());//(String settingString, Object data,Context context)
                Intent returnIntent = new Intent(activity, PermissionActivity.class);
                returnIntent.putExtra("tutorial_done", true);
                startActivity(returnIntent);
                activity.finish();
            }
        });
        return view;
    }
}
