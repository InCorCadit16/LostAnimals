package com.pbl.animals.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pbl.animals.models.base.ModelBase;

import java.util.Date;

public class Comment extends ModelBase {
    public User author;
    public long userId;
    public String content;
    public long postId;
    public Post post;
    public Location location;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    public Date seenTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    public Date commentTime;
}
