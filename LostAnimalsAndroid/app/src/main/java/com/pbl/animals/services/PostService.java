package com.pbl.animals.services;

import android.content.Context;

import com.pbl.animals.models.Post;
import com.pbl.animals.models.User;

import java.util.List;

import retrofit2.Callback;

public class PostService {
    private static PostService instance;
    private static LostAnimalsApi api;


    private PostService() {}

    public static PostService getPostService(Context ctx) {
        if (instance == null) {
            instance = new PostService();
        }

        if (api == null) {
            api = LostAnimalsApiService.getApiService(ctx);
        }

        return instance;
    }

    public void getPosts(boolean forMap, Callback<List<Post>> callback) {
        api.getPosts(forMap).enqueue(callback);
    }

    public void makePost(Post post, Callback<Void> callback) {
        api.makePost(post).enqueue(callback);
    }
}
