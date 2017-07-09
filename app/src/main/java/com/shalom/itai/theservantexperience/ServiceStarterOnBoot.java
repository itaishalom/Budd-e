package com.shalom.itai.theservantexperience;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.shalom.itai.theservantexperience.services.BuggerService;
import com.shalom.itai.theservantexperience.utils.Constants;

/**
 * Created by Itai on 13/04/2017.
 */

public class ServiceStarterOnBoot extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, Constants.ENTITY_NAME+" WAKE UP!", Toast.LENGTH_LONG).show();
        Intent service = new Intent(context, BuggerService.class);
        context.startService(service);
    }
}
