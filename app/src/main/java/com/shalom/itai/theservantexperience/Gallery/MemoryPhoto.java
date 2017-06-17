package com.shalom.itai.theservantexperience.Gallery;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import static com.shalom.itai.theservantexperience.Utils.Constants.Directory;

/**
 * Created by Itai on 10/06/2017.
 */

class MemoryPhoto implements Parcelable {
    private String mUrl;
    private String mTitle;

    private MemoryPhoto(String url, String title) {
        mUrl = url;
        mTitle = title;
    }

    private MemoryPhoto(Parcel in) {
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

    String getUrl() {
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

    static MemoryPhoto[] getSpacePhotos() {

        String galleryDirectoryName = Directory;
        File directory = new File(galleryDirectoryName);
        File[] files = directory.listFiles();
        ArrayList<MemoryPhoto> memories = new ArrayList<>();
        for (File file : files) {
            if ((file.getAbsolutePath().endsWith(".jpg")) || file.getAbsolutePath().endsWith(".jpeg")) {
                memories.add(new MemoryPhoto(file.getAbsolutePath(), readFile(file.getAbsolutePath())));
            }
        }
        MemoryPhoto[] allMems = new MemoryPhoto[memories.size()];
        allMems = memories.toArray(allMems);
        return allMems;
    }


    private static String readFile(String fileName) {
        String imageNoExt = fileName.substring(0, fileName.lastIndexOf('.'));
        String dataFile = imageNoExt + ".txt";
        File f = new File(dataFile);
        if (f.exists()) {
            int length = (int) f.length();

            byte[] bytes = new byte[length];

            FileInputStream in = null;
            try {
                in = new FileInputStream(f);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                if (in != null) {
                    int size = in.read(bytes);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }

            String text = new String(bytes);
            return "On: " + imageNoExt.substring(imageNoExt.lastIndexOf('/') + 1, imageNoExt.length()) + "\n" + text;
        }
        return "";
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
