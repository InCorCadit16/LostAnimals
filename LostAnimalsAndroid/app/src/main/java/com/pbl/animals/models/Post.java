package com.pbl.animals.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.pbl.animals.models.base.ModelBase;
import com.pbl.animals.models.inner.BreedLookup;
import com.pbl.animals.models.inner.ColorLookup;
import com.pbl.animals.models.inner.Size;
import com.pbl.animals.models.inner.SpeciesLookup;

import java.util.Date;

public class Post extends ModelBase {
    public User author;
    public SpeciesLookup species;
    public BreedLookup breed;
    public ColorLookup color;
    public Size size;
    public String content;
    public Location address;
    public Date lostTime;
    public Date postTime;
    public byte[] imageSource;

    //public List<Comment> comments;

    public Bitmap getImage() {
        return BitmapFactory.decodeByteArray(imageSource, 0, imageSource.length);
    }
}
