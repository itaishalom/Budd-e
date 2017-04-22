package com.shalom.itai.theservantexperience;

import android.content.Context;
import android.os.Environment;

import com.shalom.itai.theservantexperience.Utils.SilentCamera;

import java.io.File;

/**
 * Created by Itai on 21/04/2017.
 */

public class TakeImageThread implements Runnable {
    Context mContext;
    public TakeImageThread(Context context) {
        mContext = context;
    }
    public void run() {
        SilentCamera c = new SilentCamera(mContext);
        c.getCameraInstance();
       // String path = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"2.jpg";
        c.takePicture();
    }


}