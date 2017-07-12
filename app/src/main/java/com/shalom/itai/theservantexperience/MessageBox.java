package com.shalom.itai.theservantexperience;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;

import com.shalom.itai.theservantexperience.activities.Main2Activity;
import com.shalom.itai.theservantexperience.activities.MainActivity;
import com.shalom.itai.theservantexperience.chatBot.ChatActivity;
import com.shalom.itai.theservantexperience.services.BuggerService;
import com.shalom.itai.theservantexperience.utils.Functions;

import static com.shalom.itai.theservantexperience.utils.Constants.CHAT_QUICK_REPLY;
import static com.shalom.itai.theservantexperience.utils.Constants.CHAT_START_MESSAGE;
import static com.shalom.itai.theservantexperience.utils.Constants.IS_INSTALLED;
import static com.shalom.itai.theservantexperience.utils.Constants.JonIntents.JUST_WOKE_UP;
import static com.shalom.itai.theservantexperience.utils.Constants.LED_ID;
import static com.shalom.itai.theservantexperience.utils.Constants.MESSAGE_BOX_START_ACTIVITY;
import static com.shalom.itai.theservantexperience.utils.Constants.PREFS_NAME;
import static com.shalom.itai.theservantexperience.utils.Constants.SETTINGS_USER_WINS;
import static com.shalom.itai.theservantexperience.utils.Functions.oneTimeFunctions.addCalendarMeeting;
import static com.shalom.itai.theservantexperience.utils.Functions.takeScreenshot;

public class MessageBox extends Activity {
    private static MessageBox instance;
    private boolean isWokeUp = false;
    /**
     * Called when the activity is first created.
     * Transparency defined on manifest!!!
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_message_box);
        final ConstraintLayout mainLayout = (ConstraintLayout) findViewById(R.id.message_box_layout);
        ViewTreeObserver vto = mainLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mainLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                MediaPlayer mediaPlayer = MediaPlayer.create(MessageBox.this, R.raw.ring_message);
                AudioManager am =
                        (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                am.setStreamVolume(
                        AudioManager.STREAM_MUSIC,
                        am.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                        0);
                mediaPlayer.start();
                if(isWokeUp){
                    BuggerService.getInstance().wakeUpJon();
                }
            }
        });

        //

        final Intent startIntent = getIntent();
        Button btn = (Button) findViewById(R.id.ok);

        if (startIntent.getStringExtra(MESSAGE_BOX_START_ACTIVITY).equals("MainActivity")) {
            ((TextView) findViewById(R.id.jons_text_message_box)).setText(getIntent().getStringExtra("START_TEXT"));
            findViewById(R.id.user_response).setVisibility(View.INVISIBLE);
            isWokeUp= true;
        } else if (startIntent.getStringExtra(MESSAGE_BOX_START_ACTIVITY).equals("ChatActivity")) {
            ((TextView) findViewById(R.id.jons_text_message_box)).setText(getIntent().getStringExtra("START_TEXT"));
        }

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TextView textView = (TextView) findViewById(R.id.jons_text_message_box);
                TextView textResponse = (TextView) findViewById(R.id.user_response);
                Intent chatIntent ;
                if (startIntent.getStringExtra(MESSAGE_BOX_START_ACTIVITY).equals("ChatActivity")) {
                    chatIntent = new Intent(MessageBox.this, ChatActivity.class);
                    chatIntent.putExtra(CHAT_START_MESSAGE, textView.getText());
                    chatIntent.putExtra(CHAT_QUICK_REPLY, textResponse.getText().toString());
                    //  TextView tx = (TextView) findViewById(R.id.jon_text) ;  //TODO ??
                    // tx.setText("");
                } else {
                    chatIntent = new Intent(MessageBox.this, Main2Activity.class);
                    Functions.writeToSettings(JUST_WOKE_UP,true,MessageBox.this);
                    chatIntent.putExtra(JUST_WOKE_UP, "yes");
                }
                startActivity(chatIntent);
                finish();
            }
        });
        instance = this;
    }

    public static MessageBox getInstance() {
        return instance;
    }

}