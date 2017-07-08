package com.shalom.itai.theservantexperience.activities;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Vibrator;
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


public class DancingActivity extends AppCompatActivity {

    private Button ButtenNo;
    private Button ButtenYes;
    private TextView infoTest;
    private MediaPlayer mediaPlayer;
    private boolean isDoneDancing = false;
    private Vibrator viber;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeListener mShakeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dancing);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mediaPlayer = MediaPlayer.create(this, R.raw.bach);
//        mediaPlayer.start();

        infoTest = (TextView) findViewById(R.id.InfoText);
        ButtenYes = (Button) findViewById(R.id.IDO);
        ButtenYes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                prepareMusic();
            }
        });

        ButtenNo = (Button) findViewById(R.id.IDONT);
        ButtenNo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {prepareMusic();
            }
        });
        mShakeListener = new ShakeListener();
        viber = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);

    }


    private void prepareMusic() {
        AudioManager am =
                (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        am.setStreamVolume(
                AudioManager.STREAM_MUSIC,
                am.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                0);
        ButtenYes.setVisibility(INVISIBLE);
        ButtenNo.setVisibility(INVISIBLE);
        mShakeListener.setOnShakeListener(new ShakeListener.OnShakeListener() {

            @Override
            public void onShake(int count) {
                handleShakeEvent();
            }
        });
        startMusic();

    }

    private void startMusic() {
        mediaPlayer.start();
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                infoTest.setText("Let's dance (Shake me) to Bach for " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                infoTest.setText("Thanks!");
                mSensorManager.unregisterListener(mShakeListener);
                mediaPlayer.stop();
                isDoneDancing = true;
                finish();
            }
        }.start();
    }

    private void handleShakeEvent() {
        // Vibrate for 500 milliseconds
        viber.vibrate(500);
        BuggerService.setSYSTEM_GlobalPoints(1,null);
        Toast.makeText(this, "I like it!!", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onPause() {
        super.onPause();
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




}
