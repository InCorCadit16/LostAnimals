package com.pbl.animals.services;

import android.content.Context;

import com.pbl.animals.models.Post;
import com.pbl.animals.models.User;
import com.pbl.animals.models.contracts.requests.CreatePostRequest;

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

    public void createPost(CreatePostRequest createRequest, Callback<Long> callback) {
        api.createPost(createRequest).enqueue(callback);
    }

    public void getPostById(long id, Callback<Post> callback) {
        api.getPostById(id).enqueue(callback);
    }
}
