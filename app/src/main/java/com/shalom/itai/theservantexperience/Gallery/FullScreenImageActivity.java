/*
package com.shalom.itai.theservantexperience.Gallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.shalom.itai.theservantexperience.Activities.ToolBarActivity;
import com.shalom.itai.theservantexperience.R;

public class FullScreenImageActivity extends ToolBarActivity {

    public static final String EXTRA_SPACE_PHOTO = "SpacePhotoActivity.SPACE_PHOTO";
    public static final String CALLING_ACTIVITY = "Caller";
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState,(R.layout.activity_full_screen_image));
       // setContentView(R.layout.activity_full_screen_image);
        lLayout.setBackgroundColor(Color.parseColor("#000000"));
        mImageView = (ImageView) findViewById(R.id.image);

        MemoryPhoto memoryPhoto = getIntent().getParcelableExtra(EXTRA_SPACE_PHOTO);
        Bitmap bmp = BitmapFactory.decodeFile(memoryPhoto.getUrl());
        mImageView.setImageBitmap(bmp);
        Glide.with(this)
                .load(spacePhoto.getUrl())
                .asBitmap()
                .error(R.drawable.ic_cloud_off_red)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(mImageView);

    }
}





*/
