package com.shalom.itai.theservantexperience;

/**
 * Created by Itai on 01/05/2017.
 */

import java.util.List;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class ChatListViewAdapter extends ArrayAdapter<String> {
    private Context mContext;
    private int id;
    private List<String> items ;

    public ChatListViewAdapter(Context context, int textViewResourceId , List<String> list )
    {
        super(context, textViewResourceId, list);
        mContext = context;
        id = textViewResourceId;
        items = list ;
    }


    @Override
    public View getView(int position, View v, ViewGroup parent)
    {
        View mView = v ;
        if(mView == null){
            LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = vi.inflate(id, null);
        }

        TextView text = (TextView) mView.findViewById(R.id.chat_option);

        if(items.get(position) != null )
        {
            text.setTextColor(Color.WHITE);
            text.setText(items.get(position));
        }

        return mView;
    }
}