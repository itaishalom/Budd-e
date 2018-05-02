package com.shalom.itai.theservantexperience.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.shalom.itai.theservantexperience.R;
import com.shalom.itai.theservantexperience.utils.Functions;

import static com.shalom.itai.theservantexperience.utils.Constants.SETTINGS_IS_NOTIF_ON;

public class BoardActivity extends TaskActivity {

    public String getHeader(){
        return  "Board";
    }
    public String getfConten(){
        return  "Budd-E is board";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        Functions.popUpMessage(this, "I am board!", "ChatActivity");
        finish();
    }
}
