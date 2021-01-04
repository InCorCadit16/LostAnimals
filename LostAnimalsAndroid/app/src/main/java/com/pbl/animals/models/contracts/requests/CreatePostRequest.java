package com.pbl.animals.models.contracts.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pbl.animals.models.Comment;
import com.pbl.animals.models.Location;
import com.pbl.animals.models.User;
import com.pbl.animals.models.inner.BreedLookup;
import com.pbl.animals.models.inner.ColorLookup;
import com.pbl.animals.models.inner.PostType;
import com.pbl.animals.models.inner.Size;
import com.pbl.animals.models.inner.SpeciesLookup;

import java.util.Date;

public class CreatePostRequest {
    public SpeciesLookup species;
    public BreedLookup breed;
    public ColorLookup color;
    public Size size;
    public PostType postType;
    public String content;
    public Location location;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date lostTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date postTime;

    public byte[] imageSource;
}
