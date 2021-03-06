package com.shalom.itai.theservantexperience.gallery;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.shalom.itai.theservantexperience.activities.ToolBarActivity;
import com.shalom.itai.theservantexperience.R;
import com.shalom.itai.theservantexperience.activities.ToolBarActivityNew;
import com.squareup.picasso.Picasso;

import java.io.File;

public class GalleryActivity extends ToolBarActivityNew {
  //  private HashMap<String,Bitmap> cacheBitmaps;
    public static boolean closeOnReturn = false;
    private ConstraintLayout hidingLayout;
    ConstraintLayout.LayoutParams originalParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_gallery,R.menu.tool_bar_options,true,R.id.relative_layout_to_hide);
        // setContentView(R.layout.activity_gallery);
 //       cacheBitmaps = new HashMap<>()
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_images);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        ImageGalleryAdapter adapter = new ImageGalleryAdapter(this, MemoryPhoto.getSpacePhotos());
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (closeOnReturn) {
            closeOnReturn = false;
            finish();
        }
    }

    private class ImageGalleryAdapter extends RecyclerView.Adapter<ImageGalleryAdapter.MyViewHolder> {
        Context context;
        @Override
        public ImageGalleryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

             context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View photoView = inflater.inflate(R.layout.item_photo, parent, false);
            return new MyViewHolder(photoView);
        }

        @Override
        public void onBindViewHolder(ImageGalleryAdapter.MyViewHolder holder, int position) {

            MemoryPhoto memoryPhoto = mMemoryPhoto[position];
            final ImageView imageView = holder.mPhotoImageView;
            final ProgressBar progressBar = holder.mProgressBar;
            imageView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            /*
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(memoryPhoto.getUrl(), options);

            imageView.setImageBitmap(
                    decodeSampledBitmapFromResource(memoryPhoto.getUrl(), 50, 50));
*/


            Picasso.with(context).load(new File(memoryPhoto.getUrl())).resize(300, 300).centerCrop().into(imageView, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    if (progressBar != null) {
                        progressBar.setVisibility(View.GONE);
                        imageView.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onError() {

                }
            });

            // Show progress bar


        }

        public Bitmap decodeSampledBitmapFromResource(String path,
                                                      int reqWidth, int reqHeight) {

            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeFile(path, options);
        }

        public int calculateInSampleSize(
                BitmapFactory.Options options, int reqWidth, int reqHeight) {
            // Raw height and width of image
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {

                final int halfHeight = height / 2;
                final int halfWidth = width / 2;

                // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                // height and width larger than the requested height and width.
                while ((halfHeight / inSampleSize) > reqHeight
                        && (halfWidth / inSampleSize) > reqWidth) {
                    inSampleSize *= 2;
                }
            }

            return inSampleSize;
        }

        @Override
        public int getItemCount() {
            return (mMemoryPhoto.length);
        }

        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public ImageView mPhotoImageView;
            public ProgressBar mProgressBar;
            public MyViewHolder(View itemView) {

                super(itemView);
                mPhotoImageView = (ImageView) itemView.findViewById(R.id.iv_photo);
                mProgressBar = (ProgressBar) itemView.findViewById(R.id.iv_progress);

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {

                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    MemoryPhoto memoryPhotos = mMemoryPhoto[position];
                    Intent intent = new Intent(mContext, FullScreenMemory.class);
                    intent.putExtra(FullScreenMemory.EXTRA_SPACE_PHOTO, memoryPhotos);
                    intent.putExtra(FullScreenMemory.CALLING_ACTIVITY, "GalleryActivity");
                    startActivity(intent);
                }
            }
        }

        private MemoryPhoto[] mMemoryPhoto;
        private Context mContext;

        public ImageGalleryAdapter(Context context, MemoryPhoto[] memoryPhotos) {
            mContext = context;
            mMemoryPhoto = memoryPhotos;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    //    overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
        overridePendingTransition(R.anim.slide_bottom_in, R.anim.slide_top_out);
    }


/*    @Override
    protected void hideBeneath(ConstraintLayout layoutAppeared) {
        int val = 0;
        if (hidingLayout != null) {
            val = hidingLayout.getHeight();
        }
        ConstraintSet set = new ConstraintSet();
        RelativeLayout list = (RelativeLayout) findViewById(R.id.relative_layout_to_hide);
        //   LinearLayout.LayoutParams params =  new LinearLayout.LayoutParams(list.getWidth(), list.getHeight() - layoutAppeared.getHeight());
        //   layout.setLayoutParams(params);
      originalParams = (ConstraintLayout.LayoutParams) list.getLayoutParams();

        ConstraintLayout.LayoutParams newParams = new ConstraintLayout.LayoutParams(originalParams);
              *//*
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                list.getHeight() + val - layoutAppeared.getHeight());
*//*
        newParams.height = list.getHeight() + val - layoutAppeared.getHeight();
     //   newParams.setMargins(16, 16, 16, 16);

        layout.removeView(list);
        layout.addView(list, -1, newParams);
        set.clone(layout);
        set.connect(list.getId(), ConstraintSet.LEFT, layout.getId(), ConstraintSet.LEFT, 8);
        set.connect(list.getId(), ConstraintSet.RIGHT, layout.getId(), ConstraintSet.RIGHT, 8);
        set.connect(list.getId(), ConstraintSet.TOP, layoutAppeared.getId(), ConstraintSet.BOTTOM, 8);
        set.applyTo(layout);
        hidingLayout = layoutAppeared;
    }

    @Override
    protected void showBeneath(ConstraintLayout layoutAppeared) {
        ConstraintSet set = new ConstraintSet();
        RelativeLayout list = (RelativeLayout) findViewById(R.id.relative_layout_to_hide);
        layout.removeView(list);
        layout.addView(list, -1, originalParams);
        set.clone(layout);
        set.connect(list.getId(), ConstraintSet.TOP, R.id.my_toolbar, ConstraintSet.BOTTOM, 8);
        set.connect(list.getId(), ConstraintSet.LEFT, layout.getId(), ConstraintSet.LEFT, 8);
        set.connect(list.getId(), ConstraintSet.RIGHT, layout.getId(), ConstraintSet.RIGHT, 8);
        set.applyTo(layout);
        hidingLayout = null;
    }*/
}