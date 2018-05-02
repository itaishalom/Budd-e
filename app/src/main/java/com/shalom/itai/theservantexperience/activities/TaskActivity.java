package com.shalom.itai.theservantexperience.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.shalom.itai.theservantexperience.R;
import com.shalom.itai.theservantexperience.utils.Functions;

import static com.shalom.itai.theservantexperience.utils.Constants.SETTINGS_IS_NOTIF_ON;

public class TaskActivity extends AppCompatActivity {

    public String notifHeader;
    public String notifContent;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Functions.writeToSettings(SETTINGS_IS_NOTIF_ON,false,this);
     //   setContentView(R.layout.activity_task);
    }

    public String getHeader(){
        return  "";
    }
    public String getfConten(){
        return  "";
    }
}
