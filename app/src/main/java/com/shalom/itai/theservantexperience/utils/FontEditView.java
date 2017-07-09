package com.shalom.itai.theservantexperience.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Itai on 16/06/2017.
 * This is a FontTextView for my introduction App
 */
public class FontEditView extends EditText {
    public FontEditView(Context context) {
        super(context);
        setFont();
    }

    public FontEditView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public FontEditView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFont();
    }

    public FontEditView(Context context, @Nullable AttributeSet attrs, int defStyleAttr,
                        int defStyleRes) {
        super( context,  attrs, defStyleAttr);
        setFont();
    }
    private void setFont(){
        Typeface face= Typeface.createFromAsset(getContext().getAssets(),
                "fonts/Oswald-Regular.ttf");
        this.setTypeface(face);
    }
}


