package com.shalom.itai.theservantexperience.Gallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.shalom.itai.theservantexperience.R;

public class FullScreenImageActivity extends AppCompatActivity {

    public static final String EXTRA_SPACE_PHOTO = "SpacePhotoActivity.SPACE_PHOTO";
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        mImageView = (ImageView) findViewById(R.id.image);

        MemoryPhoto memoryPhoto = getIntent().getParcelableExtra(EXTRA_SPACE_PHOTO);
        Bitmap bmp = BitmapFactory.decodeFile(memoryPhoto.getUrl());
        mImageView.setImageBitmap(bmp);
/*        Glide.with(this)
                .load(spacePhoto.getUrl())
                .asBitmap()
                .error(R.drawable.ic_cloud_off_red)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(mImageView);*/
    }
}





