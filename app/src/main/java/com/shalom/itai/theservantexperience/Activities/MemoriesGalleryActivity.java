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
import android.widget.TextView;

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
    TextView memoData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memories_gallery);
        final ImageView diplayImage = (ImageView) findViewById(R.id.displayImage);
         memoData = (TextView) findViewById(R.id.data);
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
                if(files[i].getAbsolutePath().endsWith(".jpg")) {
                    final BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    options.inSampleSize = 2;
                    options.inJustDecodeBounds = false;
                    options.inTempStorage = new byte[16 * 1024];

                    Bitmap bmp = BitmapFactory.decodeFile(files[i].getAbsolutePath(),options);
                    final Bitmap bitmap = Bitmap.createScaledBitmap(bmp, 960, 730, false);
                   // final Bitmap bitmap = BitmapFactory.decodeStream(fis);
                    String text = "";
                    ImageView imageView = new ImageView(getApplicationContext());
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imageView.setImageBitmap(bitmap);
                    String fileName =  files[i].getAbsolutePath();
                    String imageNoExt =fileName.substring(0,fileName.lastIndexOf('.'));
                    String dataFile = imageNoExt+".txt";
                    File f = new File(dataFile);
                    if (f.exists()){
                        int length = (int)f.length();

                        byte[] bytes = new byte[length];

                        FileInputStream in = new FileInputStream(f);
                        try {
                            in.read(bytes);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            in.close();
                        }

                        text = new String(bytes);
                        String temp = "On: " + imageNoExt.substring(imageNoExt.lastIndexOf('/')+1,imageNoExt.length())+ "\n" + text;
                        text =temp;
                    }
                    final String memoText = text;
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            diplayImage.setImageBitmap(bitmap);
                            diplayImage.setScaleType(ImageView.ScaleType.FIT_XY);
                            memoData.setText(memoText);

                        }
                    });
                    imageView.setLayoutParams(new ViewGroup.LayoutParams(params.width / 3, params.height / 4));
                    myGallery.addView(imageView);
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }


}

