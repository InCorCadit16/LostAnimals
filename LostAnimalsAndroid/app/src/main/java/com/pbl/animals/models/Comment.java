package com.pbl.animals.models;

import java.util.Date;

public class Comment {
    public User author;
    public String content;
    public long postId;
    public Post post;
    public Location location;
    public Date SeenTime;
    public Date CommentTime;
}
