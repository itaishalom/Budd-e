/*
package com.shalom.itai.theservantexperience.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.vision.face.Face;
import com.shalom.itai.theservantexperience.FaceOverlayView;
import com.shalom.itai.theservantexperience.R;
import com.shalom.itai.theservantexperience.services.BuggerService;


public class ImageActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1888;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        Button take_image = (Button) findViewById(R.id.take_image);
        take_image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startCamera();
            }
        });
    }
    private void startCamera()
    {
        //Start camera to caputre image.
        Intent m_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(m_intent, CAMERA_REQUEST);
    }

    //Close eyes and smile for example


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAMERA_REQUEST && resultCode == RESULT_OK){
            Bitmap photo=(Bitmap) data.getExtras().get("data");
            FaceOverlayView mFaceOverlayView;  mFaceOverlayView = (FaceOverlayView) findViewById(R.id.face_overlay);
            SparseArray<Face> mFaces= mFaceOverlayView.setBitmap(photo);


            if(mFaces.size()==0){
                Toast.makeText(this, "I don't see your face!",
                        Toast.LENGTH_LONG).show();
                BuggerService.setSYSTEM_GlobalPoints(-2);
            }else {
                for (int i = 0; i < mFaces.size(); i++) {
                    Face face = mFaces.valueAt(i);

                    float smilingProbability = face.getIsSmilingProbability();


                    if(smilingProbability<0.8) {
                        Toast.makeText(this, "you don't smile",
                                Toast.LENGTH_LONG).show();
                        BuggerService.setSYSTEM_GlobalPoints(-1);
                    }else
                    {
                        Toast.makeText(this, "you  smile!",
                                Toast.LENGTH_LONG).show();
                        BuggerService.setSYSTEM_GlobalPoints(1);
                    }
                }
            }
        }
    }
}
*/
