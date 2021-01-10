package com.pbl.animals.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.pbl.animals.models.base.ModelBase;

public class Shelter extends ModelBase {
    public String name;
    public String description;
    public Location location;
    public byte[] imageSource;

    public Bitmap getImage() {
        if (imageSource == null) {
            return null;
        }

        return BitmapFactory.decodeByteArray(imageSource, 0, imageSource.length);
    }
}
