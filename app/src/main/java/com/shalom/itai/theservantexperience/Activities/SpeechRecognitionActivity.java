package com.shalom.itai.theservantexperience.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.shalom.itai.theservantexperience.Activities.MainActivity;
import com.shalom.itai.theservantexperience.R;

import java.util.ArrayList;

public class SpeechRecognitionActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1234;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_reconition);
        textView = (TextView) findViewById(R.id.textView);
      //  Intent intent = getIntent();
        prepareUser();
    }

    public void startListen() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        startActivityForResult(intent, REQUEST_CODE);
    }


    private void prepareUser()
    {
        new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {
                textView.setText("Tell me \"I love you\" in " + millisUntilFinished / 1000 + " seconds");
            }

            public void onFinish() {
                startListen();
            }
        }.start();

    }


    public  boolean isConnected()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = cm.getActiveNetworkInfo();
        if (net!=null && net.isAvailable() && net.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList arrayList = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if( !arrayList.get(0).equals("I love you"))
            {
                Toast.makeText(getApplicationContext(), "you can do better",Toast.LENGTH_LONG).show();
                startListen();
            }else {
                Toast.makeText(getApplicationContext(), "I love you too!!!", Toast.LENGTH_LONG).show();
                finish();
                /*   Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);*/
            }
        }
    }
}
