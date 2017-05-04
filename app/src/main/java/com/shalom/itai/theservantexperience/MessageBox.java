package com.shalom.itai.theservantexperience;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.shalom.itai.theservantexperience.Activities.MainActivity;
import com.shalom.itai.theservantexperience.ChatBot.ChatActivity;

import static com.shalom.itai.theservantexperience.Utils.Constants.CHAT_QUICK_REPLY;
import static com.shalom.itai.theservantexperience.Utils.Constants.CHAT_START_MESSAGE;

public class MessageBox extends Activity {

        /** Called when the activity is first created. */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_message_box);

            Button btn = (Button) findViewById(R.id.ok);
            btn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    TextView textView = (TextView) findViewById(R.id.jons_text) ;
                    TextView textResponse = (TextView) findViewById(R.id.user_response) ;
                    Intent chatIntent = new Intent(MainActivity.getInstance(), ChatActivity.class);

                    chatIntent.putExtra(CHAT_START_MESSAGE,textView.getText());
                    chatIntent.putExtra(CHAT_QUICK_REPLY,textResponse.getText());
                    startActivity(chatIntent);
                    finish();
                }
            });
        }
    }