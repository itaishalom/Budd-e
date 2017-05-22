package com.shalom.itai.theservantexperience.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.shalom.itai.theservantexperience.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.shalom.itai.theservantexperience.Utils.Constants.Directory;

public class MemoriesGalleryActivity extends AppCompatActivity {
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final String TAG = "MemoriesGalleryActivity";
     LinearLayout myGallery;// = (LinearLayout) findViewById(R.id.mygallery);
    private Uri fileUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memories_gallery);
        final ImageView diplayImage = (ImageView) findViewById(R.id.displayImage);
          myGallery = (LinearLayout) findViewById(R.id.mygallery);

/*
        String PACKAGE_NAME = getApplicationContext().getPackageName();
        imageIDs = new ArrayList<>();
        String path = Directory;
        File directory = new File(path);
        File[] files = directory.listFiles();
        for (int i = 0; i < files.length; i++)
        {
            String fileName = files[i].getAbsolutePath();
  */
        ViewGroup.LayoutParams params = myGallery.getLayoutParams();
        params.width = getResources().getDisplayMetrics().widthPixels;
        params.height = getResources().getDisplayMetrics().heightPixels;



        FileInputStream fis = null;
        try {
            String galleryDirectoryName = Directory;
            File directory = new File(galleryDirectoryName);
            File[] files = directory.listFiles();
            for (int i = 0; i < files.length; i++) {
                fis = new FileInputStream(files[i]);
                final Bitmap bitmap = BitmapFactory.decodeStream(fis);

                ImageView imageView = new ImageView(getApplicationContext());

       //         imageView.setLayoutParams(new ViewGroup.LayoutParams(70, 70));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setImageBitmap(bitmap);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        diplayImage.setImageBitmap(bitmap);
                    }
                });


     //           imageView.setLayoutParams(new ViewGroup.LayoutParams(70, 70));
                imageView.setLayoutParams(new ViewGroup.LayoutParams(params.width /3 ,params.height /4 ));
                myGallery.addView(imageView);
            }
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
    }



    public static void takeScreenshot(AppCompatActivity activity) {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath =Directory + "/" + now + ".jpg";
            // create bitmap screen capture
            View v1 = activity.getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);
            File imageFile = new File(mPath);
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Throwable e) {
            // Several error may come out with file handling or OOM
            e.printStackTrace();
        }
    }
}

