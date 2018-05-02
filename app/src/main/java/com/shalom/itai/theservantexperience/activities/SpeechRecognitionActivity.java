package com.shalom.itai.theservantexperience.activities;

import android.content.Intent;
import android.os.CountDownTimer;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.shalom.itai.theservantexperience.R;
import com.shalom.itai.theservantexperience.utils.Functions;

import java.util.ArrayList;

import static com.shalom.itai.theservantexperience.utils.Constants.SAY_LOVE;
import static com.shalom.itai.theservantexperience.utils.Constants.SETTINGS_IS_NOTIF_ON;

public class SpeechRecognitionActivity extends TaskActivity  {
    private static final int REQUEST_CODE = 1234;
    private TextView textView;

    public String getHeader(){
        return  "Love";
    }
    public String getfConten(){
        return  "Budd-E wants love";
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_reconition);

        textView = (TextView) findViewById(R.id.textView);
      //  Intent intent = getIntent();

        Functions.writeToSettings(SAY_LOVE,true,this);
        startActivity(new Intent(this,Main2Activity.class));
        finish();
       // prepareUser();
    }

    private void startListen() {

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


// --Commented out by Inspection START (18/06/2017 00:18):
//    public  boolean isConnected()
//    {
//        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo net = cm.getActiveNetworkInfo();
//        if (net!=null && net.isAvailable() && net.isConnected()) {
//            return true;
//        } else {
//            return false;
//        }
//    }
// --Commented out by Inspection STOP (18/06/2017 00:18)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList arrayList = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if( !arrayList.get(0).equals("I love you")) {
                Toast.makeText(getApplicationContext(), "you can do better",Toast.LENGTH_LONG).show();
                startListen();
            }else {
                Toast.makeText(getApplicationContext(), "I love you too!!!", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

}
