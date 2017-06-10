package com.shalom.itai.theservantexperience.Gallery;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

import static com.shalom.itai.theservantexperience.Utils.Constants.Directory;

/**
 * Created by Itai on 10/06/2017.
 */

public class MemoryPhoto implements Parcelable {
    private String mUrl;
    private String mTitle;

    public MemoryPhoto(String url, String title) {
        mUrl = url;
        mTitle = title;
    }

    protected MemoryPhoto(Parcel in) {
        mUrl = in.readString();
        mTitle = in.readString();
    }

    public static final Parcelable.Creator<MemoryPhoto> CREATOR = new Creator<MemoryPhoto>() {
        @Override
        public MemoryPhoto createFromParcel(Parcel in) {
            return new MemoryPhoto(in);
        }

        @Override
        public MemoryPhoto[] newArray(int size) {
            return new MemoryPhoto[size];
        }
    };

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public static  MemoryPhoto[] getSpacePhotos() {

        String galleryDirectoryName = Directory;
        File directory = new File(galleryDirectoryName);
        File[] files = directory.listFiles();
        ArrayList<MemoryPhoto> mems = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            if((files[i].getAbsolutePath().endsWith(".jpg"))||files[i].getAbsolutePath().endsWith(".jpeg")) {
                mems.add(new MemoryPhoto(files[i].getAbsolutePath(), "check"));
            }
           /* if(mems.size()==6){
                break;
            }*/
        }
        MemoryPhoto[] allMems = new MemoryPhoto[mems.size()];
        allMems = mems.toArray(allMems);
        return allMems;
/*
        return new MemoryPhoto[]{
                new MemoryPhoto("http://i.imgur.com/zuG2bGQ.jpg", "Galaxy"),
                new MemoryPhoto("http://i.imgur.com/ovr0NAF.jpg", "Space Shuttle"),
                new MemoryPhoto("http://i.imgur.com/n6RfJX2.jpg", "Galaxy Orion"),
                new MemoryPhoto("http://i.imgur.com/qpr5LR2.jpg", "Earth"),
                new MemoryPhoto("http://i.imgur.com/pSHXfu5.jpg", "Astronaut"),
                new MemoryPhoto("http://i.imgur.com/3wQcZeY.jpg", "Satellite"),
        };
    */
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mUrl);
        parcel.writeString(mTitle);
    }
}
