package com.shalom.itai.theservantexperience.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.Toast;

import com.shalom.itai.theservantexperience.R;
import com.shalom.itai.theservantexperience.services.BuggerService;
import com.shalom.itai.theservantexperience.utils.CameraPreview;

import java.io.File;

import static android.os.Environment.DIRECTORY_PICTURES;
import static com.shalom.itai.theservantexperience.utils.Constants.IMAGE_READY;
import static com.shalom.itai.theservantexperience.utils.Functions.copy;
import static com.shalom.itai.theservantexperience.utils.SilentCamera.saveMemory;

public class SelfieV2 extends AppCompatActivity implements DialogCaller {

    private CameraPreview surfaceView;
    private SelfieV2 inst;
    private BroadcastReceiver mReceiver;
    private ImageView jonPng;
    private ShareActionProvider mShareActionProvider;
    private boolean isBackFromShare = false;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selfie_v2);

        Button buttonStartCameraPreview = (Button) findViewById(R.id.startcamerapreview);

        getWindow().setFormat(PixelFormat.UNKNOWN);

        surfaceView = (CameraPreview) findViewById(R.id.camerapreview);

        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        jonPng = ((ImageView) findViewById(R.id.jon_in_slefie));

        surfaceHolder.addCallback(surfaceView);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        buttonStartCameraPreview.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    surfaceView.takeImage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        inst = this;
        showDialog();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(this.mReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isBackFromShare){
            finish();
        }
        IntentFilter intentFilter = new IntentFilter(
                IMAGE_READY);

        mReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Bitmap imageBitmap = surfaceView.getImageBitmap();
                mergeImages(imageBitmap);
                /*
                if (FaceOverlayView.isFaceInImage(imageBitmap, inst)) {
                    BuggerService.setSYSTEM_GlobalPoints(2);
                    mergeImages(imageBitmap);
                } else {
                    Toast.makeText(inst ,"You liar! Your'e face not on the image!", Toast.LENGTH_LONG).show();
                    BuggerService.setSYSTEM_GlobalPoints(-2);
                    finish();
                }
                */
            }
        };
        //registering our receiver
        this.registerReceiver(mReceiver, intentFilter);
    }

    private void mergeImages(Bitmap bottomImage) {
        // (ImageView) findViewById(R.id.jon_in_slefie);
        ImageView jonPng = ((ImageView) findViewById(R.id.jon_in_slefie));
        //Bitmap topImag2e = BitmapFactory.decodeResource(this.getResources(), R.drawable.jon_png);
        Bitmap topImage = ((BitmapDrawable)jonPng.getDrawable()).getBitmap();

        topImage = (Bitmap.createScaledBitmap(topImage, jonPng.getWidth()+jonPng.getWidth()/2, jonPng.getHeight(), false));

        Bitmap bmOverlay = Bitmap.createBitmap(bottomImage.getWidth(), bottomImage.getHeight(), bottomImage.getConfig());
        Canvas canvas = new Canvas(bmOverlay);

        canvas.drawBitmap(bottomImage, new Matrix(), null);
        canvas.drawBitmap(topImage, bottomImage.getWidth() - topImage.getWidth(), 300, null);
        String path = saveMemory(bmOverlay, "Our selfie!");
        Toast.makeText(inst ,"Thanks for the selfie!", Toast.LENGTH_LONG).show();
        surfaceView.clearRam();
      //  String tempFilePath = Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES);
        if (copy(new File(path),new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES)+ File.separator+"temporary_file.jpeg"))) {
            Uri uriToImage = Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES)+ File.separator+"temporary_file.jpeg"));
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, uriToImage);
            shareIntent.setType("image/jpeg");
            shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check me and Jon!");
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Check me and Jon!");
            setShareIntent(shareIntent);
            isBackFromShare = true;
            BuggerService.getInstance().unbug();
            Intent result = Intent.createChooser(shareIntent, getResources().getText(R.string.send_to));
            startActivity(result);
            //   finish();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu resource file.
        getMenuInflater().inflate(R.menu.share_menu, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);

        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) item.getActionProvider();

        // Return true to display menu
        return true;
    }

    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }


    @Override
    public void doPositive() {
        jonPng.setVisibility(View.VISIBLE);
    }

    @Override
    public void doNegative() {
        Toast.makeText(inst ,BuggerService.getInstance().getRandomInsult(), Toast.LENGTH_LONG).show();
        BuggerService.setSYSTEM_GlobalPoints(-2, "You refused doing a selfi with me");
        finish();
    }

    @Override
    public void showDialog() {
        DialogFragment newFragment = MyAlertDialogFragment
                .newInstance(R.string.alert_dialog_Selfie_buttons_title,"Yes!","No!",getClass().getName());
        newFragment.show(getSupportFragmentManager(),"dialog");
    }
}