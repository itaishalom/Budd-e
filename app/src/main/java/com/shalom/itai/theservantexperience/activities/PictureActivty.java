package com.shalom.itai.theservantexperience.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.shalom.itai.theservantexperience.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import static com.shalom.itai.theservantexperience.utils.Constants.Directory;
import static com.shalom.itai.theservantexperience.utils.Constants.SAVE_IMAGE;

public class PictureActivty extends AppCompatActivity {
    private static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private Uri fileUri;
    private boolean toSave;
    private String mText;
    String mTextPath;

    // --Commented out by Inspection (18/06/2017 00:17):int width =0;
    // --Commented out by Inspection (18/06/2017 00:17):int height = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_activty);
        onCaptureImage();
        toSave = getIntent().getBooleanExtra(SAVE_IMAGE, false);
        mText = getIntent().getStringExtra("Text");
    }


    private void onCaptureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
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
        String mPath = Directory + "/" + now + ".jpeg";
        mTextPath = Directory + "/" + now + ".txt";
        //File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), NameActivity.programFolder+"\\Images");
        // Create a media file name

        if (type == MEDIA_TYPE_IMAGE) {
            return new File(mPath);
        }
        return null;
    }

    private void finalize(int status) {
        Intent returnIntent = new Intent();
        setResult(status, returnIntent);
        finish();
    }
/*
    private void finalize(int status, byte[] image) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(IMAGE_BYTE_ARRAY, image);
        setResult(status, returnIntent);
        finish();
    }
*/
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {


                if (fileUri != null) {
                    try {
                        //   ImageView newImage =  (ImageView)findViewById(R.id.ResultImage);
/*
                        final BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        options.inSampleSize = 2;
                        options.inJustDecodeBounds = false;
                        options.inTempStorage = new byte[16 * 1024];

                        Bitmap bmp = BitmapFactory.decodeFile(fileUri.getPath(),options);
                        Bitmap bMap = Bitmap.createScaledBitmap(bmp, 960, 730, false);

                       // Bitmap bMap = BitmapFactory.decodeFile(fileUri.getPath());
*/
                        //newImage.setImageBitmap(bMap);
                        if(mText !=null && !mText.isEmpty()){
                            FileOutputStream fileData = null;

                            File file = new File(mTextPath);
                            try {
                                fileData = new FileOutputStream(file);
                                fileData.write(mText.getBytes());
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                try {
                                    if (fileData != null) {
                                        fileData.close();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                       /*
                        File f = new File(fileUri.getPath());
                       ByteArrayOutputStream stream = new ByteArrayOutputStream();

                        Bitmap bmp = BitmapFactory.decodeFile(f.getAbsolutePath());
                        bmp.compress(Bitmap.CompressFormat.JPEG, 40, stream);
                        byte[] arr = stream.toByteArray();

                        stream.flush();
                        stream.close();
                        if (!toSave) {
                            File ff = new File(fileUri.getPath());
                            ff.delete();
                        }
                        */
                        finalize(Activity.RESULT_OK);
                    } catch (Exception e) {
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
