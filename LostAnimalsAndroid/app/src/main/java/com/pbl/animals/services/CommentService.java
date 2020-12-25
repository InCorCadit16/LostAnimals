package com.pbl.animals.services;

import android.content.Context;

import com.pbl.animals.models.Comment;

import retrofit2.Callback;

public class CommentService {
    private static CommentService instance;
    private static LostAnimalsApi api;


    private CommentService() {}

    public static CommentService getCommentService(Context ctx) {
        if (instance == null) {
            instance = new CommentService();
        }

        if (api == null) {
            api = LostAnimalsApiService.getApiService(ctx);
        }

        return instance;
    }

    public void getCommentsByPostId(long postId, Callback<Comment[]> callback) {
        api.getCommentsByPostId(postId).enqueue(callback);
    }
}
