package com.shalom.itai.theservantexperience;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.shalom.itai.theservantexperience.activities.MainActivity;
import com.shalom.itai.theservantexperience.ChatBot.ChatActivity;

import static com.shalom.itai.theservantexperience.Utils.Constants.CHAT_QUICK_REPLY;
import static com.shalom.itai.theservantexperience.Utils.Constants.CHAT_START_MESSAGE;
import static com.shalom.itai.theservantexperience.Utils.Constants.MESSAGE_BOX_START_ACTIVITY;

public class MessageBox extends Activity {
    private static MessageBox  instance;
    /** Called when the activity is first created.
     * Transparency defined on manifest!!! */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_message_box);
        final Intent startIntent = getIntent();
        Button btn = (Button) findViewById(R.id.ok);

        if(startIntent.getStringExtra(MESSAGE_BOX_START_ACTIVITY).equals("MainActivity")) {
            ((TextView) findViewById(R.id.jons_text_message_box)).setText(getIntent().getStringExtra("START_TEXT"));
            findViewById(R.id.user_response).setVisibility(View.INVISIBLE);
        }

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TextView textView = (TextView) findViewById(R.id.jons_text_message_box) ;
                TextView textResponse = (TextView) findViewById(R.id.user_response) ;
                Intent chatIntent = null;
                if(startIntent.getStringExtra(MESSAGE_BOX_START_ACTIVITY).equals("ChatActivity")) {
                    chatIntent = new Intent(MessageBox.this, ChatActivity.class);
                    chatIntent.putExtra(CHAT_START_MESSAGE,textView.getText());
                    chatIntent.putExtra(CHAT_QUICK_REPLY,textResponse.getText());
                    TextView tx = (TextView) findViewById(R.id.jon_text) ;
                    tx.setText("");
                }
                else{
                    chatIntent = new Intent(MessageBox.this, MainActivity.class);

                    chatIntent.putExtra("wakeUpOptions",true);
                }
                startActivity(chatIntent);
                finish();
            }
        });
    instance = this;
    }
    public static MessageBox getInstance(){
        return instance;
    }
}