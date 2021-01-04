package com.pbl.animals.models;

import com.pbl.animals.models.base.ModelBase;

import java.util.Date;

public class Comment extends ModelBase {
    public User author;
    public String content;
    public long postId;
    public Post post;
    public Location location;
    public Date SeenTime;
    public Date CommentTime;
}
