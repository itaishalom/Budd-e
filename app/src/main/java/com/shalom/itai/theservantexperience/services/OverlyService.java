package com.shalom.itai.theservantexperience.services;

/**
 * Created by Itai on 03/07/2017.
 */

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.res.ResourcesCompat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.shalom.itai.theservantexperience.R;
import com.shalom.itai.theservantexperience.activities.Main2Activity;
import com.shalom.itai.theservantexperience.activities.MainActivity;

public class OverlyService extends Service implements OnTouchListener, OnClickListener {

    private View topLeftView;
    private ProgressBar mProgress;
    private ImageButton overlayedButton;
    private float offsetX;
    private float offsetY;
    private int originalXPos;
    private int originalYPos;
    private boolean moving;
    private WindowManager wm;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        overlayedButton = new ImageButton(this);
        //  overlayedButton.setText("Aios");
        overlayedButton.setOnTouchListener(this);
        overlayedButton.setAlpha(1.0f);
        //   overlayedButton.setLayoutParams(wm.updateViewLayout().LayoutParams(10, 10));

        //overlayedButton.setImageResource(R.drawable.jon_png);
        //ResourcesCompat.getDrawable(getResources(), R.drawable.name, null);
        /*
        while(BuggerService.getInstance() ==null){
            try {
                stopService(new Intent(getBaseContext(), BuggerService.class));
                Thread.sleep(5000);
                    startService(new Intent(getBaseContext(), BuggerService.class));
                Thread.sleep(5000);


            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        */
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), BuggerService.getInstance().getMood().getPng(),null);
        drawable.setBounds(0, 0, (int)(drawable.getIntrinsicWidth()*0.5),
                (int)(drawable.getIntrinsicHeight()*0.5));
/*
        ScaleDrawable sd = new ScaleDrawable(drawable, 0, scaleWidth, scaleHeight);
     //   Button btn = findViewbyId(R.id.yourbtnID);
        overlayedButton.setCompoundDrawables(sd.getDrawable(), null, null, null); //set drawableLef
  */
        overlayedButton.setBackground(drawable);
        //overlayedButton.setTextColor(Color.BLACK);
        // overlayedButton.setBackgroundColor(Color.GRAY);
        overlayedButton.setOnClickListener(this);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_SYSTEM_ALERT, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.LEFT | Gravity.TOP;;
        params.x = 0;
        params.y = 0;
        /*android:layout_width="212dp"
        android:layout_height="225dp"*/
        params.width =(int) (drawable.getIntrinsicWidth()*0.3);
        params.height = (int)(drawable.getIntrinsicHeight()*0.3);
        wm.addView(overlayedButton, params);
        //    wm.updateViewLayout(overlayedButton,new LayoutParams(10,10));
        topLeftView = new View(this);
        WindowManager.LayoutParams topLeftParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_SYSTEM_ALERT, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSLUCENT);
        topLeftParams.gravity =  Gravity.LEFT | Gravity.TOP;;
        topLeftParams.x = 0;
        topLeftParams.y = 0;
        topLeftParams.width = 0;
        topLeftParams.height = 0;
        wm.addView(topLeftView, topLeftParams);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (overlayedButton != null) {
            wm.removeView(overlayedButton);
        }
        if (mProgress != null){
            wm.removeView(mProgress);
        }
        if (mProgress != null){
            wm.removeView(topLeftView);
        }
        overlayedButton = null;
        topLeftView = null;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getRawX();
            float y = event.getRawY();

            moving = false;

            int[] location = new int[2];
            overlayedButton.getLocationOnScreen(location);

            originalXPos = location[0];
            originalYPos = location[1];

            offsetX = originalXPos - x;
            offsetY = originalYPos - y;

        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            int[] topLeftLocationOnScreen = new int[2];
            topLeftView.getLocationOnScreen(topLeftLocationOnScreen);

            System.out.println("topLeftY="+topLeftLocationOnScreen[1]);
            System.out.println("originalY="+originalYPos);

            float x = event.getRawX();
            float y = event.getRawY();

            WindowManager.LayoutParams params = (LayoutParams) overlayedButton.getLayoutParams();

            int newX = (int) (offsetX + x);
            int newY = (int) (offsetY + y);

            if (Math.abs(newX - originalXPos) < 1 && Math.abs(newY - originalYPos) < 1 && !moving) {
                return false;
            }

            params.x = newX - (topLeftLocationOnScreen[0]);
            params.y = newY - (topLeftLocationOnScreen[1]);

            wm.updateViewLayout(overlayedButton, params);
            moving = true;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (moving) {
                return true;
            }
        }

        return false;
    }


    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, Main2Activity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));


        //overlayedButton.setVisibility(View.INVISIBLE);
        //   wm.removeViewImmediate(overlayedButton);
        overlayedButton.setVisibility(View.INVISIBLE);
        mProgress = new ProgressBar(this);


        WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_SYSTEM_ALERT, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_HORIZONTAL;
        params.x =  0;
        params.y =  0;
        /*android:layout_width="212dp"
        android:layout_height="225dp"*/
        params.width = 200;
        params.height = 200;
        wm.addView(mProgress, params);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopSelf();
            }
        },2000);

//        ProgressDialog pdLoading = new ProgressDialog(this);

        // Toast.makeText(this, "Coming right up!", Toast.LENGTH_SHORT).show();

    }

}