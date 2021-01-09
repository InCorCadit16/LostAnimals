package com.pbl.animals.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pbl.animals.models.base.ModelBase;

import java.util.List;

public class User extends ModelBase {
    public String email;
    public String firstName;
    public String lastName;
    public String phoneNumber;
    public byte[] imageSource;

    public String getFullName() {
        return lastName == null? firstName : firstName + " " + lastName;
    }

    public Bitmap getImage() {
        if (imageSource == null) {
            return null;
        }

        return BitmapFactory.decodeByteArray(imageSource, 0, imageSource.length);
    }
}
