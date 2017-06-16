package com.shalom.itai.theservantexperience.Utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Itai on 16/06/2017.
 */

public class FontTextView extends TextView {
    public FontTextView(Context context) {
        super(context);
        setFont();
    }

    public FontTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public FontTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFont();
    }

    public FontTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setFont();
    }
    private void setFont(){
        Typeface face= Typeface.createFromAsset(getContext().getAssets(), "fonts/Oswald-Regular.ttf");
        this.setTypeface(face);
    }

}
