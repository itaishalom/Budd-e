package com.shalom.itai.theservantexperience.activities;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.shalom.itai.theservantexperience.R;
import com.shalom.itai.theservantexperience.services.BuggerService;
import com.shalom.itai.theservantexperience.utils.ShakeListener;

import static android.view.View.INVISIBLE;


public class DancingActivity extends ToolBarActivityNew implements DialogCaller{

    private MediaPlayer mediaPlayer;
    private boolean isDoneDancing = false;
    private Vibrator viber;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeListener mShakeListener;
    private int moveDirection = 1;
    private int shakeCounter = 0;
    private boolean isStartedDancing = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_dancing, R.menu.tool_bar_options, true, -1);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mediaPlayer = MediaPlayer.create(this, R.raw.bach);
//        mediaPlayer.start();


        mShakeListener = new ShakeListener();
        viber = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        showDialog();

    }


    private void prepareMusic() {
        AudioManager am =
                (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        am.setStreamVolume(
                AudioManager.STREAM_MUSIC,
                am.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                0);

        mShakeListener.setOnShakeListener(new ShakeListener.OnShakeListener() {

            @Override
            public void onShake(int count) {
                handleShakeEvent();
            }
        });
        startMusic();

    }


    private void animateBurnMatch(final View view, int direction) {
        switch (direction) {
            case -1:
                view.animate().translationY(-50);
                break;
            case 1:
                view.animate().translationY(0);
        }
    }

    private void startMusic() {
        mediaPlayer.start();
        isStartedDancing = true;
        Toast.makeText(this,"Let's dance (Shake me) to Bach for 30 seconds, shake me!",Toast.LENGTH_LONG);
        new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                moveDirection *= -1;
                animateBurnMatch (mGifImageView,moveDirection);
             //   Toast.makeText(this,"Let's dance (Shake me) to Bach for " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                Toast.makeText(DancingActivity.this,"Thanks!",Toast.LENGTH_LONG);
                mSensorManager.unregisterListener(mShakeListener);
                mediaPlayer.stop();
                isDoneDancing = true;
                BuggerService.setSYSTEM_GlobalPoints(shakeCounter,"We danced together");
                finish();
            }
        }.start();
    }

    private void handleShakeEvent() {
        // Vibrate for 500 milliseconds
        viber.vibrate(500);
        shakeCounter++;
     //   BuggerService.setSYSTEM_GlobalPoints(1,null);
        Toast.makeText(this, "I like it!!", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onPause() {
        super.onPause();
        if(!isStartedDancing){
            return;
        }
        mediaPlayer.stop();
        if (!isDoneDancing) {
            mediaPlayer = MediaPlayer.create(this, R.raw.buzz);
            mediaPlayer.start();
            Toast.makeText(this, "Demit you!!", Toast.LENGTH_SHORT).show();
            viber.vibrate(5500);
            BuggerService.setSYSTEM_GlobalPoints(-5,"You stopped our dance");
            mSensorManager.unregisterListener(mShakeListener);
        }
        finish();
      //  BuggerService.isMainActivityUp = false;
    }

    protected void onResume() {
        super.onResume();
    //    mediaPlayer.stop();
        mSensorManager.registerListener(mShakeListener, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }


    @Override
    public void onBackPressed() {

        //    Intent intent = new Intent(getBaseContext(), LoginActivity.class);
        //      startActivity(intent);
        //   finish();
        //  super.onBackPressed();
    }


    @Override
    public void doPositive() {
        prepareMusic();
    }

    @Override
    public void doNegative() {
        prepareMusic();
    }

    @Override
    public void showDialog() {
        DialogFragment newFragment = MyAlertDialogFragment
                .newInstance(R.string.alert_dialog_dance, "Yes!", "No!", getClass().getName(),"Let us dance (Shake me) to Bach for 30 seconds, shake me!");
        newFragment.show(getSupportFragmentManager(), "dialog");
    }
}
