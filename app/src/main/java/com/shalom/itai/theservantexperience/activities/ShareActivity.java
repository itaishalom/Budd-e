package com.shalom.itai.theservantexperience.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.shalom.itai.theservantexperience.R;
import com.shalom.itai.theservantexperience.chatBot.ChatActivity;
import com.shalom.itai.theservantexperience.gallery.FullScreenMemory;
import com.shalom.itai.theservantexperience.gallery.MemoryPhoto;

import java.util.ArrayList;

import static com.shalom.itai.theservantexperience.utils.Constants.CHAT_QUICK_REPLY;

public class ShareActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            } else if (type.startsWith("image/")) {
                handleSendImage(intent); // Handle single image being sent
            }
        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                handleSendMultipleImages(intent); // Handle multiple images being sent
            }
        } else {
            // Handle other intents, such as being started from the home screen
        }
        finish();
    }

    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {

            Intent chatIntent = new Intent(this, ChatActivity.class);
         //   chatIntent.putExtra(CHAT_START_MESSAGE,textView.getText());
            chatIntent.putExtra(CHAT_QUICK_REPLY,sharedText);
            startActivity(chatIntent);
        }
    }

    void handleSendImage(Intent intent) {
        Uri imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            String text = intent.getStringExtra(Intent.EXTRA_TEXT);
            MemoryPhoto memoryPhoto =new MemoryPhoto(imageUri.toString(), "",true);

         if(text !=null ) {
             memoryPhoto.setTitle(text);
         }
            Intent newIntent = new Intent(this, FullScreenMemory.class);
            newIntent.putExtra(FullScreenMemory.EXTRA_SPACE_PHOTO, memoryPhoto);
       //     intent.putExtra(FullScreenMemory.CALLING_ACTIVITY, "GalleryActivity");
            startActivity(newIntent);
            // Update UI to reflect image being shared
        }
    }

    void handleSendMultipleImages(Intent intent) {
        ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if (imageUris != null) {
            // Update UI to reflect multiple images being shared
        }
    }
}
