package com.pbl.animals.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.pbl.animals.models.base.ModelBase;
import com.pbl.animals.models.inner.BreedLookup;
import com.pbl.animals.models.inner.ColorLookup;
import com.pbl.animals.models.inner.PostType;
import com.pbl.animals.models.inner.Size;
import com.pbl.animals.models.inner.SpeciesLookup;

import java.util.Date;
import java.util.List;

public class Post extends ModelBase {
    public User author;
    public SpeciesLookup species;
    public BreedLookup breed;
    public ColorLookup color;
    public Size size;
    public PostType postType;
    public String content;
    public Location location;
    public Date lostTime;
    public Date postTime;
    public byte[] imageSource;
    public List<Comment> comments;

    public Bitmap getImage() {
        if (imageSource == null) {
            return null;
        }

        return BitmapFactory.decodeByteArray(imageSource, 0, imageSource.length);
    }
}
