package com.shalom.itai.theservantexperience.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.shalom.itai.theservantexperience.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.shalom.itai.theservantexperience.Utils.Constants.Directory;

public class PictureActivty extends AppCompatActivity {
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private Uri fileUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_activty);
        onCaptureImage();
    }


    public void onCaptureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra( MediaStore.EXTRA_OUTPUT,  fileUri);
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    private Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted

            // this works for Android 2.2 and above
            Date now = new Date();
            android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
            String mPath =Directory + "/" + now + ".jpg";
            //File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), NameActivity.programFolder+"\\Images");
            // Create a media file name

            if (type == MEDIA_TYPE_IMAGE) {
                File imageFile = new File(mPath);;
                return imageFile;
            }
        return null;
    }

    private void finalize(int status){
        Intent returnIntent = new Intent();
        setResult(status,returnIntent);
        finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {


                if(fileUri != null) {
                    try {
                        //   ImageView newImage =  (ImageView)findViewById(R.id.ResultImage);
                        final BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        options.inSampleSize = 2;
                        options.inJustDecodeBounds = false;
                        options.inTempStorage = new byte[16 * 1024];

                        Bitmap bmp = BitmapFactory.decodeFile(fileUri.getPath(),options);
                        Bitmap bMap = Bitmap.createScaledBitmap(bmp, 960, 730, false);

                       // Bitmap bMap = BitmapFactory.decodeFile(fileUri.getPath());

                        //newImage.setImageBitmap(bMap);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();

                        bMap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

                        stream.flush();

                        stream.close();
                        finalize(Activity.RESULT_OK);
                    } catch (IOException e) {
                        finalize(Activity.RESULT_CANCELED);
                        e.printStackTrace();
                    }

                }

            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
                finalize(Activity.RESULT_CANCELED);
            } else {
                // Image capture failed, advise user
                finalize(Activity.RESULT_CANCELED);
            }
        }
    }


}
